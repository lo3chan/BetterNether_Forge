package org.betterx.betternether.mixin.client;

import org.betterx.betternether.client.ClientOptions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(MusicManager.class)
public class MusicTrackerMixin {
    @Unique private static final float FADE_SPEED = 0.2f; // Units per second (0.2f -> Fade across 5 seconds)
    @Unique private static final float TICK_DELTA = 0.05f;
    @Unique private static final RandomSource BN_FALLBACK_RANDOM = RandomSource.create();
    // Note: Assume game is at a constant 20 tps since MC doesn't have getTPS()
    // The use of currentTimeMillis() is ditched since it is overly complex for this system
    // The difference from this constant will only be noticeable if the game's TPS is extremely low
    // If the game is lagging to that extent, smooth music blending is the least of the player's worries

    @Unique private final MusicManager bn_thisObj = (MusicManager)(Object)this;
    @Unique private boolean bn_waitChange = false;
    @Unique private float bn_volume = 1.0f;

    @Unique private static Field bn_minecraftField;
    @Unique private static Field bn_randomField;
    @Unique private static Field bn_currentMusicField;
    @Unique private static Field bn_nextSongDelayField;

    @Unique
    private boolean bn_isCorrectDimension(Minecraft minecraft) {
        return minecraft.player != null && minecraft.level != null
                && minecraft.level.dimension() == Level.NETHER;
    }

    @Unique
    private boolean bn_shouldChangeMusic(Music toMusic) {
        ResourceLocation currentMusicLocation = bn_getCurrentMusicLocation();
        return currentMusicLocation == null || !toMusic.getEvent().value().getLocation().equals(currentMusicLocation);
    }

    @Unique
    private ResourceLocation bn_getCurrentMusicLocation() {
        SoundInstance currentMusic = bn_getCurrentMusic();
        if (currentMusic instanceof AbstractSoundInstanceAccessor accessor) {
            return accessor.getLocation();
        }
        return null;
    }

    @Unique
    private Minecraft bn_getMinecraft() {
        Minecraft minecraft = bn_getField(bn_getMinecraftField(), Minecraft.class);
        return minecraft == null ? Minecraft.getInstance() : minecraft;
    }

    @Unique
    private RandomSource bn_getRandom() {
        RandomSource random = bn_getField(bn_getRandomField(), RandomSource.class);
        return random == null ? BN_FALLBACK_RANDOM : random;
    }

    @Unique
    private SoundInstance bn_getCurrentMusic() {
        return bn_getField(bn_getCurrentMusicField(), SoundInstance.class);
    }

    @Unique
    private void bn_setCurrentMusic(SoundInstance currentMusic) {
        bn_setField(bn_getCurrentMusicField(), currentMusic);
    }

    @Unique
    private int bn_getNextSongDelay() {
        Integer nextSongDelay = bn_getField(bn_getNextSongDelayField(), Integer.class);
        return nextSongDelay == null ? 0 : nextSongDelay;
    }

    @Unique
    private void bn_setNextSongDelay(int nextSongDelay) {
        bn_setField(bn_getNextSongDelayField(), nextSongDelay);
    }

    @Unique
    private static Field bn_getMinecraftField() {
        if (bn_minecraftField == null) {
            bn_minecraftField = bn_findField(Minecraft.class);
        }
        return bn_minecraftField;
    }

    @Unique
    private static Field bn_getRandomField() {
        if (bn_randomField == null) {
            bn_randomField = bn_findField(RandomSource.class);
        }
        return bn_randomField;
    }

    @Unique
    private static Field bn_getCurrentMusicField() {
        if (bn_currentMusicField == null) {
            bn_currentMusicField = bn_findField(SoundInstance.class);
        }
        return bn_currentMusicField;
    }

    @Unique
    private static Field bn_getNextSongDelayField() {
        if (bn_nextSongDelayField == null) {
            bn_nextSongDelayField = bn_findField(int.class);
        }
        return bn_nextSongDelayField;
    }

    @Unique
    private static Field bn_findField(Class<?> type) {
        for (Field field : MusicManager.class.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType()) || type == field.getType()) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    @Unique
    private <T> T bn_getField(Field field, Class<T> type) {
        if (field == null) {
            return null;
        }
        try {
            Object value = field.get(bn_thisObj);
            return type.isInstance(value) ? type.cast(value) : null;
        } catch (IllegalAccessException ignored) {
            return null;
        }
    }

    @Unique
    private void bn_setField(Field field, Object value) {
        if (field == null) {
            return;
        }
        try {
            field.set(bn_thisObj, value);
        } catch (IllegalAccessException ignored) {
        }
    }

    @Inject(method = "startPlaying", at = @At("TAIL"))
    public void bn_startPlaying(Music music, CallbackInfo ci) {
        bn_volume = 0.0f; // Mostly to fix issues when the blending system becomes desynced due to other dims
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void bn_onTick(CallbackInfo ci) {
        Minecraft minecraft = bn_getMinecraft();
        if (!ClientOptions.blendBiomeMusic() || minecraft == null || !bn_isCorrectDimension(minecraft)) {
            bn_waitChange = false;
            bn_volume = 1.0f;
            return;
        }

        Music targetMusic = minecraft.getSituationalMusic();
        if (targetMusic == null || !targetMusic.replaceCurrentMusic()) {
            bn_waitChange = false;
            bn_volume = 1.0f;
            return; // If the target music cannot replace the current, let vanilla handle it
        }

        RandomSource random = bn_getRandom();
        SoundInstance currentMusic = bn_getCurrentMusic();
        int nextSongDelay = bn_getNextSongDelay();
        if (currentMusic != null && !minecraft.getSoundManager().isActive(currentMusic)) {
            currentMusic = null;
            bn_setCurrentMusic(null);
            nextSongDelay = Math.min(
                    nextSongDelay,
                    Mth.nextInt(random, targetMusic.getMinDelay(), targetMusic.getMaxDelay())
            );
        }
        nextSongDelay = Math.min(nextSongDelay, targetMusic.getMaxDelay());
        bn_setNextSongDelay(nextSongDelay);

        if (currentMusic == null) {
            bn_setNextSongDelay(nextSongDelay - 1);
            if (nextSongDelay <= 0) {
                bn_waitChange = false;
                bn_thisObj.startPlaying(targetMusic);
                currentMusic = bn_getCurrentMusic();
                if (currentMusic instanceof AbstractSoundInstanceAccessor accessor) {
                    accessor.setVolume(0.0f);
                    minecraft.getSoundManager().updateSourceVolume(
                            currentMusic.getSource(),
                            0.0f
                    );
                }
            }
            ci.cancel();
            return;
        }

        boolean volumeChanged = false;
        if (bn_waitChange || bn_shouldChangeMusic(targetMusic)) {
            if (!bn_waitChange) {
                nextSongDelay = random.nextInt(0, Math.max(targetMusic.getMinDelay() / 2, 1));
                bn_setNextSongDelay(nextSongDelay);
                bn_waitChange = true;
            }
            if (bn_volume > 0.0f) {
                // Fade out current music
                volumeChanged = true;
                bn_volume -= FADE_SPEED * TICK_DELTA;
                if (bn_volume <= 0.0f) {
                    bn_volume = 0.0f;
                    minecraft.getSoundManager().stop(currentMusic);
                    currentMusic = null;
                    bn_setCurrentMusic(null);
                }
            } else if (nextSongDelay > 0) {
                // In-between music delay
                nextSongDelay -= 1;
                bn_setNextSongDelay(nextSongDelay);
            } else {
                // Start new music
                bn_waitChange = false;
                bn_thisObj.startPlaying(targetMusic);
                currentMusic = bn_getCurrentMusic();
                if (currentMusic instanceof AbstractSoundInstanceAccessor accessor) {
                    accessor.setVolume(0.0f);
                    minecraft.getSoundManager().updateSourceVolume(
                            currentMusic.getSource(),
                            0.0f
                    );
                }
            }
        } else if (bn_volume < 1.0f) {
            // Fade in new music
            volumeChanged = true;
            bn_volume += FADE_SPEED * TICK_DELTA;
        }

        if (volumeChanged) {
            bn_volume = Mth.clamp(bn_volume, 0.0f, 1.0f);
            if (currentMusic instanceof AbstractSoundInstanceAccessor accessor) {
                accessor.setVolume(bn_volume);
                minecraft.getSoundManager().updateSourceVolume(currentMusic.getSource(), bn_volume);
            }
        }

        ci.cancel();
    }
}
