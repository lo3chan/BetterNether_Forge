package org.betterx.betternether.config.screen;

import org.betterx.betternether.BetterNether;
import org.betterx.betternether.client.ClientOptions;
import org.betterx.betternether.config.Config;
import org.betterx.betternether.config.Configs;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parrent;
    private Component header;

    public ConfigScreen(Screen parrent) {
        super(Component.translatable("bn_config"));
        this.parrent = parrent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                new OnPress() {
                    @Override
                    public void onPress(Button button) {
                        Config.save();
                        Configs.CLIENT_CONFIG.saveChanges();
                        ConfigScreen.this.minecraft.setScreen(parrent);
                    }
                }
        ).bounds(this.width / 2 - 100, this.height - 27, 200, 20).build());

        header = Component.translatable("\u00A7b* ")
                          .append(Component.translatable("config.betternether.mod_reload").getString());

        // Thin Armor //
        final String varArmour = "smaller_armor_offset";
        boolean hasArmour = Configs.MAIN.getBoolean("improvement", varArmour, true);

        AbstractWidget armorButton = Button.builder(
                Component.translatable("config.betternether.armour"),
                new OnPress() {
                    @Override
                    public void onPress(Button button) {
                        boolean value = !Configs.MAIN.getBoolean(
                                "improvement",
                                varArmour,
                                true
                        );
                        Configs.MAIN.setBoolean("improvement", varArmour, value);
                        String color = value ? ": \u00A7a" : ": \u00A7c";
                        button.setMessage(Component.translatable(
                                                           "config.betternether.armour")
                                                   .append(color + CommonComponents.optionStatus(
                                                           value).getString()));
                    }
                }
        ).bounds(this.width / 2 - 100, 27, 150, 20).build();
        String color = hasArmour ? ": \u00A7a" : ": \u00A7c";
        armorButton.setMessage(Component.translatable("config.betternether.armour")
                                        .append(color + CommonComponents.optionStatus(hasArmour).getString()));
        this.addRenderableWidget(armorButton);

        this.addRenderableWidget(Button.builder(
                Component.translatable("config.betternether.reset"),
                new OnPress() {
                    @Override
                    public void onPress(Button button) {
                        Configs.MAIN.setBoolean("improvement", varArmour, true);
                        BetterNether.setThinArmor(true);
                        armorButton.setMessage(Component.translatable(
                                                                "config.betternether.armour")
                                                        .append(": \u00A7a" + CommonComponents.optionStatus(
                                                                true).getString()));
                    }
                }
        ).bounds(this.width / 2 + 40 + 20, 27, 40, 20).build());

        // Lavafalls //
        final String varLava = "lavafall_particles";
        boolean hasLava = Configs.MAIN.getBoolean("improvement", varLava, true);

        AbstractWidget lavaButton = Button.builder(
                Component.translatable("config.betternether.armour"),
                new OnPress() {
                    @Override
                    public void onPress(Button button) {
                        boolean value = !Configs.MAIN.getBoolean(
                                "improvement",
                                varLava,
                                true
                        );
                        Configs.MAIN.setBoolean("improvement", varLava, value);
                        String color = value ? ": \u00A7a" : ": \u00A7c";
                        button.setMessage(Component.translatable(
                                                           "config.betternether.lavafalls")
                                                   .append(color + CommonComponents.optionStatus(
                                                           value).getString()));
                    }
                }
        ).bounds(this.width / 2 - 100, 27 * 2, 150, 20).build();
        color = hasLava ? ": \u00A7a" : ": \u00A7c";
        lavaButton.setMessage(Component.translatable("config.betternether.lavafalls")
                                       .append(color + CommonComponents.optionStatus(hasLava).getString()));
        this.addRenderableWidget(lavaButton);

        this.addRenderableWidget(Button.builder(
                Component.translatable("config.betternether.reset"),
                new OnPress() {
                    @Override
                    public void onPress(Button button) {
                        Configs.MAIN.setBoolean("improvement", varLava, true);
                        BetterNether.setThinArmor(true);
                        lavaButton.setMessage(Component.translatable(
                                                               "config.betternether.lavafalls")
                                                       .append(": \u00A7a" + CommonComponents.optionStatus(
                                                               true).getString()));
                    }
                }
        ).bounds(this.width / 2 + 40 + 20, 27 * 2, 40, 20).build());

        // Biome Music //
        final String varBlendBiomeMusic = "blendBiomeMusic";
        boolean hasBlendBiomeMusic = Configs.CLIENT_CONFIG.getBooleanRoot(varBlendBiomeMusic, true);

        AbstractWidget biomeMusicButton = Button.builder(
                Component.translatable("config.betternether.blend_biome_music"),
                new OnPress() {
                    @Override
                    public void onPress(Button button) {
                        boolean value = !Configs.CLIENT_CONFIG.getBooleanRoot(varBlendBiomeMusic, true);
                        Configs.CLIENT_CONFIG.setBooleanRoot(varBlendBiomeMusic, value);
                        ClientOptions.setBlendBiomeMusic(value);
                        String color = value ? ": \u00A7a" : ": \u00A7c";
                        button.setMessage(Component.translatable("config.betternether.blend_biome_music")
                                                   .append(color + CommonComponents.optionStatus(value).getString()));
                    }
                }
        ).bounds(this.width / 2 - 100, 27 * 3, 150, 20).build();
        color = hasBlendBiomeMusic ? ": \u00A7a" : ": \u00A7c";
        biomeMusicButton.setMessage(Component.translatable("config.betternether.blend_biome_music")
                                             .append(color + CommonComponents.optionStatus(hasBlendBiomeMusic)
                                                                             .getString()));
        this.addRenderableWidget(biomeMusicButton);

        this.addRenderableWidget(Button.builder(
                Component.translatable("config.betternether.reset"),
                new OnPress() {
                    @Override
                    public void onPress(Button button) {
                        Configs.CLIENT_CONFIG.setBooleanRoot(varBlendBiomeMusic, true);
                        ClientOptions.setBlendBiomeMusic(true);
                        biomeMusicButton.setMessage(Component.translatable("config.betternether.blend_biome_music")
                                                             .append(": \u00A7a" + CommonComponents.optionStatus(true)
                                                                                                   .getString()));
                    }
                }
        ).bounds(this.width / 2 + 40 + 20, 27 * 3, 40, 20).build());
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);

        guiGraphics.drawCenteredString(this.font, header, this.width / 2, 14, 16777215);
    }
}
