package dev.ferdinandkeller.uhcgm;

import dev.ferdinandkeller.uhcgm.mixin.client.CreateWorldScreenAccessor;
import dev.ferdinandkeller.uhcgm.mixin.client.ScreenAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

@Environment(EnvType.CLIENT)
public class UHCGameTab extends GridScreenTab {
    private static final Text GAME_TAB_TITLE_TEXT = Text.translatable("createWorld.tab.game.title");
    private static final Text ALLOW_COMMANDS_TEXT = Text.translatable("selectWorld.allowCommands");
    private final TextFieldWidget worldNameField;

    public void setNaturalRegenerationGamerule(WorldCreator world_creator, Boolean value) {
        GameRules gamerules = world_creator.getGameRules();
        gamerules.get(GameRules.NATURAL_REGENERATION).set(value, null);
        world_creator.setGameRules(gamerules);
    }

    Boolean getNaturalRegenerationGamerule(WorldCreator world_creator) {
        GameRules gamerules = world_creator.getGameRules();
        return gamerules.getBoolean(GameRules.NATURAL_REGENERATION);
    }

    public UHCGameTab(CreateWorldScreen screen) {
        super(GAME_TAB_TITLE_TEXT);

        TextRenderer text_renderer = ((ScreenAccessor)screen).getTextRenderer();
        WorldCreator world_creator = ((CreateWorldScreenAccessor)screen).getWorldCreator();

        Text GAME_MODE_TEXT = ((CreateWorldScreenAccessor)screen).GAME_MODE_TEXT();
        Text ENTER_NAME_TEXT = ((CreateWorldScreenAccessor)screen).ENTER_NAME_TEXT();
        Text EXPERIMENTS_TEXT =  ((CreateWorldScreenAccessor)screen).EXPERIMENTS_TEXT();
        Text ALLOW_COMMANDS_INFO_TEXT = ((CreateWorldScreenAccessor)screen).ALLOW_COMMANDS_INFO_TEXT();

        GridWidget.Adder adder = this.grid.setRowSpacing(8).createAdder(1);
        Positioner positioner = adder.copyPositioner();
        this.worldNameField = new TextFieldWidget(text_renderer, 208, 20, Text.translatable("selectWorld.enterName"));
        this.worldNameField.setText(world_creator.getWorldName());
        this.worldNameField.setChangedListener(world_creator::setWorldName);
        world_creator
            .addListener(
                creator -> this.worldNameField
                    .setTooltip(
                        Tooltip.of(Text.translatable("selectWorld.targetFolder", new Object[]{Text.literal(creator.getWorldDirectoryName()).formatted(Formatting.ITALIC)}))
                    )
            );
        ((ScreenAccessor)screen).setInitialFocusInvoker(this.worldNameField);
        adder.add(
            LayoutWidgets.createLabeledWidget(text_renderer, this.worldNameField, ENTER_NAME_TEXT),
            adder.copyPositioner().alignHorizontalCenter()
        );
        CyclingButtonWidget<UHCMode> cyclingButtonWidget = adder.add(
            CyclingButtonWidget.<UHCMode>builder(value -> value.name)
                .values(UHCMode.SURVIVAL, UHCMode.HARDCORE, UHCMode.ULTRA_HARDCORE, UHCMode.CREATIVE)
                .build(0, 0, 210, 20, GAME_MODE_TEXT, (button, uhc_mode) -> {
                    world_creator.setGameMode(uhc_mode.toStandardMode());
                    setNaturalRegenerationGamerule(world_creator, uhc_mode != UHCMode.ULTRA_HARDCORE);
                }),
            positioner
        );
        world_creator.addListener(creator -> {
            UHCMode uhc_mode = UHCMode.fromStandardMode(
                creator.getGameMode(),
                getNaturalRegenerationGamerule(creator)
            );
            cyclingButtonWidget.setValue(uhc_mode);
            cyclingButtonWidget.active = !creator.isDebug();
            cyclingButtonWidget.setTooltip(Tooltip.of(uhc_mode.getInfo()));
        });
        CyclingButtonWidget<Difficulty> cyclingButtonWidget2 = adder.add(
            CyclingButtonWidget.<Difficulty>builder(Difficulty::getTranslatableName)
                .values(Difficulty.values())
                .build(0, 0, 210, 20, Text.translatable("options.difficulty"), (button, value) -> world_creator.setDifficulty(value)),
            positioner
        );
        world_creator.addListener(creator -> {
            cyclingButtonWidget2.setValue(world_creator.getDifficulty());
            cyclingButtonWidget2.active = !world_creator.isHardcore();
            cyclingButtonWidget2.setTooltip(Tooltip.of(world_creator.getDifficulty().getInfo()));
        });
        CyclingButtonWidget<Boolean> cyclingButtonWidget3 = adder.add(
            CyclingButtonWidget.onOffBuilder()
                .tooltip(value -> Tooltip.of(ALLOW_COMMANDS_INFO_TEXT))
                .build(0, 0, 210, 20, ALLOW_COMMANDS_TEXT, (button, value) -> world_creator.setCheatsEnabled(value))
        );
        world_creator.addListener(creator -> {
            cyclingButtonWidget3.setValue(world_creator.areCheatsEnabled());
            cyclingButtonWidget3.active = !world_creator.isDebug() && !world_creator.isHardcore();
        });
        if (!SharedConstants.getGameVersion().isStable()) {
            adder.add(
                ButtonWidget.builder(
                        EXPERIMENTS_TEXT,
                        button -> ((CreateWorldScreenAccessor)screen).openExperimentsScreenInvoker(world_creator.getGeneratorOptionsHolder().dataConfiguration())
                    )
                    .width(210)
                    .build()
            );
        }
    }
}
