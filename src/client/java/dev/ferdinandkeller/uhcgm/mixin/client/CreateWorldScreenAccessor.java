package dev.ferdinandkeller.uhcgm.mixin.client;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.resource.DataConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreateWorldScreen.class)
public abstract interface CreateWorldScreenAccessor {
    @Accessor("worldCreator")
    WorldCreator getWorldCreator();

    @Invoker("openExperimentsScreen")
    void openExperimentsScreenInvoker(DataConfiguration dataConfiguration);

    @Accessor("GAME_MODE_TEXT")
    Text GAME_MODE_TEXT();

    @Accessor("ENTER_NAME_TEXT")
    Text ENTER_NAME_TEXT();

    @Accessor("EXPERIMENTS_TEXT")
    Text EXPERIMENTS_TEXT();

    @Accessor("ALLOW_COMMANDS_INFO_TEXT")
    Text ALLOW_COMMANDS_INFO_TEXT();
}
