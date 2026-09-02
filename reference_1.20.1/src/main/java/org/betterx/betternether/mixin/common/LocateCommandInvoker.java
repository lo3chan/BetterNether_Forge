package org.betterx.betternether.mixin.common;

import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.world.level.levelgen.structure.Structure;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(LocateCommand.class)
public interface LocateCommandInvoker {
    @Invoker("getHolders")
    static Optional<? extends HolderSet.ListBacked<Structure>> callGetHolders(
            ResourceOrTagKeyArgument.Result<Structure> result,
            Registry<Structure> registry
    ) {
        throw new AssertionError("@Invoker dummy body called");
    }
}
