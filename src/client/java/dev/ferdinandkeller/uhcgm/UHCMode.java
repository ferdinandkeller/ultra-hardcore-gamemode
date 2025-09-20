package dev.ferdinandkeller.uhcgm;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;


@Environment(EnvType.CLIENT)
enum UHCMode {
    SURVIVAL("survival", GameMode.SURVIVAL),
    HARDCORE("hardcore", GameMode.SURVIVAL),
    ULTRA_HARDCORE("ultra_hardcore", GameMode.SURVIVAL),
    CREATIVE("creative", GameMode.CREATIVE),
    DEBUG("spectator", GameMode.SPECTATOR);

    public final GameMode defaultGameMode;
    public final Text name;
    private final Text info;

    UHCMode(final String name, final GameMode defaultGameMode) {
        this.defaultGameMode = defaultGameMode;
        this.name = Text.translatable("selectWorld.gameMode." + name);
        this.info = Text.translatable("selectWorld.gameMode." + name + ".info");
    }

    public Text getInfo() {
        return this.info;
    }

    public WorldCreator.Mode toStandardMode() {
        return switch (this) {
            case SURVIVAL -> WorldCreator.Mode.SURVIVAL;
            case HARDCORE -> WorldCreator.Mode.HARDCORE;
            case ULTRA_HARDCORE -> WorldCreator.Mode.HARDCORE;
            case CREATIVE -> WorldCreator.Mode.CREATIVE;
            case DEBUG -> WorldCreator.Mode.DEBUG;
        };
    }

    public static UHCMode fromStandardMode(WorldCreator.Mode mode, Boolean regen_enabled) {
        return switch (mode) {
            case WorldCreator.Mode.SURVIVAL -> SURVIVAL;
            case WorldCreator.Mode.HARDCORE -> regen_enabled ? HARDCORE : ULTRA_HARDCORE;
            case WorldCreator.Mode.CREATIVE -> CREATIVE;
            case WorldCreator.Mode.DEBUG -> DEBUG;
        };
    }
}
