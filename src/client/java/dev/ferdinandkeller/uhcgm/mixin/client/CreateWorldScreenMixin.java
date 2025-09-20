package dev.ferdinandkeller.uhcgm.mixin.client;

import dev.ferdinandkeller.uhcgm.UHCGameTab;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.tab.Tab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(CreateWorldScreen.class)
@Environment(EnvType.CLIENT)
public class CreateWorldScreenMixin {
    private static CreateWorldScreen current_instance;

    @Inject(method = "init", at = @At("HEAD"))
    private void captureInstance(CallbackInfo ci) {
        current_instance = (CreateWorldScreen)(Object)this;
    }

    @ModifyArgs(
        method = "init",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/widget/TabNavigationWidget$Builder;tabs([Lnet/minecraft/client/gui/tab/Tab;)Lnet/minecraft/client/gui/widget/TabNavigationWidget$Builder;"
        )
    )
    private void tabs(Args args) {
        Tab[] tabs = args.get(0); // get the array of tabs
        tabs[0] = new UHCGameTab(current_instance); // update the game tab
        args.set(0, tabs); // replace the argument
    }
}
