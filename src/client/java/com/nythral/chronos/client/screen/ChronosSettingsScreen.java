package com.nythral.chronos.client.screen;

import com.nythral.chronos.client.config.ChronosConfig;
import com.nythral.chronos.client.config.ChronosConfigManager;
import com.nythral.lib.client.screen.NythralStyledScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ChronosSettingsScreen extends NythralStyledScreen {

	private final Screen parent;

	private Button enabledButton;
	private Button anchorButton;
	private Button sideButton;
	private Button layoutButton;
	private Button displayButton;
	private Button attachmentButton;

	public ChronosSettingsScreen(Screen parent) {
		super(Component.literal("Chronos Timer"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		ChronosConfig config = ChronosConfig.INSTANCE;

		this.enabledButton = addLeftButton(
			0,
			enabledLabel(),
			button -> {
				config.toggleEnabled();
				button.setMessage(enabledLabel());
				ChronosConfigManager.save();
			}
		);

		this.anchorButton = addRightButton(
			0,
			anchorLabel(),
			button -> {
				config.nextAnchor();
				button.setMessage(anchorLabel());
				ChronosConfigManager.save();
			}
		);

		this.sideButton = addLeftButton(
			1,
			sideLabel(),
			button -> {
				config.nextSide();
				button.setMessage(sideLabel());
				ChronosConfigManager.save();
			}
		);

		this.layoutButton = addRightButton(
			1,
			layoutLabel(),
			button -> {
				config.nextLayout();
				button.setMessage(layoutLabel());
				ChronosConfigManager.save();
			}
		);

		this.displayButton = addLeftButton(
			2,
			displayLabel(),
			button -> {
				config.nextDisplayMode();
				button.setMessage(displayLabel());
				ChronosConfigManager.save();
			}
		);

		this.attachmentButton = addRightButton(
			2,
			attachmentLabel(),
			button -> {
				config.nextAttachment();
				button.setMessage(attachmentLabel());
				ChronosConfigManager.save();
			}
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

	private Component enabledLabel() {
		return Component.literal(
			"Enabled: "
				+ (
					ChronosConfig.INSTANCE.enabled()
						? "On"
						: "Off"
				)
		);
	}

	private Component anchorLabel() {
		return Component.literal(
			"Position: "
				+ ChronosConfig.INSTANCE
					.anchor()
					.displayName()
		);
	}

	private Component sideLabel() {
		return Component.literal(
			"Side: "
				+ ChronosConfig.INSTANCE
					.side()
					.displayName()
		);
	}

	private Component layoutLabel() {
		return Component.literal(
			"Layout: "
				+ ChronosConfig.INSTANCE
					.layout()
					.displayName()
		);
	}

	private Component displayLabel() {
		return Component.literal(
			"Display: "
				+ ChronosConfig.INSTANCE
					.displayMode()
					.displayName()
		);
	}

	private Component attachmentLabel() {
		return Component.literal(
			"Attachment: "
				+ ChronosConfig.INSTANCE
					.attachment()
					.displayName()
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