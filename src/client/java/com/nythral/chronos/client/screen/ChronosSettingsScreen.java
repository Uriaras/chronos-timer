package com.nythral.chronos.client.screen;

import com.nythral.chronos.client.config.ChronosConfig;
import com.nythral.chronos.client.config.ChronosConfigManager;
import com.nythral.lib.client.screen.NythralStyledScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ChronosSettingsScreen extends NythralStyledScreen {

    private final Screen parent;

    public ChronosSettingsScreen(Screen parent) {
        super(Component.literal("Chronos Timer"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ChronosConfig config = ChronosConfig.INSTANCE;

        addLeftButton(
            0,
            enabledLabel(),
            button -> update(
                config::toggleEnabled,
                button,
                enabledLabel()
            )
        );

        addRightButton(
            0,
            anchorLabel(),
            button -> update(
                config::nextAnchor,
                button,
                anchorLabel()
            )
        );

        addLeftButton(
            1,
            sideLabel(),
            button -> update(
                config::nextSide,
                button,
                sideLabel()
            )
        );

        addRightButton(
            1,
            layoutLabel(),
            button -> update(
                config::nextLayout,
                button,
                layoutLabel()
            )
        );

        addLeftButton(
            2,
            displayLabel(),
            button -> update(
                config::nextDisplayMode,
                button,
                displayLabel()
            )
        );

        addRightButton(
            2,
            attachmentLabel(),
            button -> update(
                config::nextAttachment,
                button,
                attachmentLabel()
            )
        );

        addRenderableWidget(
            new ChronosOpacitySlider(
                leftColumnX(),
                rowY(3),
                CONTROL_WIDTH,
                CONTROL_HEIGHT,
                config.opacity(),
                value -> {
                    config.setOpacity(value);
                    ChronosConfigManager.save();
                }
            )
        );

        addLeftFooterButton(
            Component.literal("Reset Settings"),
            button -> {
                config.reset();
                ChronosConfigManager.save();
                rebuildWidgets();
            }
        );

        addRightFooterButton(
            Component.literal("Done"),
            button -> onClose()
        );
    }

    private void update(
        Runnable change,
        Button button,
        Component message
    ) {
        change.run();
        button.setMessage(message);
        ChronosConfigManager.save();
    }

    private Component enabledLabel() {
        return optionLabel(
            "Enabled",
            ChronosConfig.INSTANCE.enabled()
                ? "On"
                : "Off"
        );
    }

    private Component anchorLabel() {
        return optionLabel(
            "Position",
            ChronosConfig.INSTANCE
                .anchor()
                .displayName()
        );
    }

    private Component sideLabel() {
        return optionLabel(
            "Side",
            ChronosConfig.INSTANCE
                .side()
                .displayName()
        );
    }

    private Component layoutLabel() {
        return optionLabel(
            "Layout",
            ChronosConfig.INSTANCE
                .layout()
                .displayName()
        );
    }

    private Component displayLabel() {
        return optionLabel(
            "Display",
            ChronosConfig.INSTANCE
                .displayMode()
                .displayName()
        );
    }

    private Component attachmentLabel() {
        return optionLabel(
            "Attachment",
            ChronosConfig.INSTANCE
                .attachment()
                .displayName()
        );
    }

    private static Component optionLabel(
        String name,
        String value
    ) {
        return Component.literal(
            name + ": " + value
        );
    }

    @Override
    public void onClose() {
        ChronosConfigManager.save();

        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}