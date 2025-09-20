package dev.ferdinandkeller.uhcgm.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
@Environment(EnvType.CLIENT)
public interface ScreenAccessor {
    @Accessor("textRenderer")
    TextRenderer getTextRenderer();

    @Invoker("setInitialFocus")
    void setInitialFocusInvoker(Element element);
}
