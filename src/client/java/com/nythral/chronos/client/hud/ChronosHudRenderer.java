package com.nythral.chronos.client.hud;

import com.nythral.chronos.client.config.ChronosAnchor;
import com.nythral.chronos.client.config.ChronosAttachment;
import com.nythral.chronos.client.config.ChronosConfig;
import com.nythral.chronos.client.config.ChronosLayout;
import com.nythral.chronos.client.config.ChronosSide;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class ChronosHudRenderer {

	private static final int ICON_BOX_SIZE = 24;
	private static final int TOP_TEXTURE_HEIGHT = 10;
	private static final int TEXTURE_OVERLAP = 1;
	private static final int HORIZONTAL_ELEMENT_GAP = 1;
	private static final int SCREEN_MARGIN_X = 1;

	private ChronosHudRenderer() {
	}

	public static void render(
		GuiGraphics graphics,
		Minecraft minecraft,
		List<ChronosEffectEntry> entries,
		ChronosConfig config
	) {
		if (
			config.layout()
				== ChronosLayout.VERTICAL
		) {
			renderVertical(
				graphics,
				minecraft,
				entries,
				config
			);
			return;
		}

		renderHorizontal(
			graphics,
			minecraft,
			entries,
			config
		);
	}

	private static void renderVertical(
		GuiGraphics graphics,
		Minecraft minecraft,
		List<ChronosEffectEntry> entries,
		ChronosConfig config
	) {
		int verticalStep =
			ChronosLayoutCalculator.verticalStep(
				config
			);

		int elementHeight =
			ChronosLayoutCalculator.verticalElementHeight(
				config
			);

		int totalHeight =
			elementHeight
				+ Math.max(
					0,
					entries.size() - 1
				)
				* verticalStep;

		int firstIconY =
			ChronosLayoutCalculator.verticalStartY(
				graphics.guiHeight(),
				totalHeight,
				config.anchor()
			);

		if (
			config.anchor()
				== ChronosAnchor.TOP
				&& config.attachment()
					== ChronosAttachment.TOP
		) {
			firstIconY +=
				TOP_TEXTURE_HEIGHT
					- TEXTURE_OVERLAP;
		}

		int iconX =
			ChronosLayoutCalculator.verticalIconX(
				graphics.guiWidth(),
				config.side()
			);

		for (
			int index = 0;
			index < entries.size();
			index++
		) {
			int iconY =
				firstIconY
					+ index * verticalStep;

			ChronosEffectRenderer.render(
				graphics,
				minecraft,
				entries.get(index),
				iconX,
				iconY,
				config
			);
		}
	}

	private static void renderHorizontal(
		GuiGraphics graphics,
		Minecraft minecraft,
		List<ChronosEffectEntry> entries,
		ChronosConfig config
	) {
		List<ChronosEffectEntry> upperEntries =
			new ArrayList<>();

		List<ChronosEffectEntry> harmfulEntries =
			new ArrayList<>();

		for (
			ChronosEffectEntry entry
				: entries
		) {
			if (
				ChronosEffectCollector.isHarmful(
					entry.effect()
				)
			) {
				harmfulEntries.add(
					entry
				);
			} else {
				upperEntries.add(
					entry
				);
			}
		}

		int rowCount = 0;

		if (
			!upperEntries.isEmpty()
		) {
			rowCount++;
		}

		if (
			!harmfulEntries.isEmpty()
		) {
			rowCount++;
		}

		int rowStep =
			ChronosLayoutCalculator.verticalStep(
				config
			);

		int totalHeight =
			ChronosLayoutCalculator.verticalElementHeight(
				config
			)
				+ Math.max(
					0,
					rowCount - 1
				)
				* rowStep;

		int firstRowIconY =
			ChronosLayoutCalculator.verticalStartY(
				graphics.guiHeight(),
				totalHeight,
				config.anchor()
			);

		if (
			config.anchor()
				== ChronosAnchor.TOP
				&& config.attachment()
					== ChronosAttachment.TOP
		) {
			firstRowIconY +=
				TOP_TEXTURE_HEIGHT
					- TEXTURE_OVERLAP;
		}

		if (
			!upperEntries.isEmpty()
		) {
			renderHorizontalRow(
				graphics,
				minecraft,
				upperEntries,
				firstRowIconY,
				config
			);
		}

		if (
			!harmfulEntries.isEmpty()
		) {
			int harmfulRowIconY =
				firstRowIconY;

			if (
				!upperEntries.isEmpty()
			) {
				harmfulRowIconY +=
					rowStep;
			}

			renderHorizontalRow(
				graphics,
				minecraft,
				harmfulEntries,
				harmfulRowIconY,
				config
			);
		}
	}

	private static void renderHorizontalRow(
		GuiGraphics graphics,
		Minecraft minecraft,
		List<ChronosEffectEntry> entries,
		int iconY,
		ChronosConfig config
	) {
		int elementWidth =
			ChronosLayoutCalculator.horizontalElementWidth(
				config
			);

		int step =
			elementWidth
				+ HORIZONTAL_ELEMENT_GAP;

		if (
			config.side()
				== ChronosSide.RIGHT
		) {
			int firstIconX =
				graphics.guiWidth()
					- SCREEN_MARGIN_X
					- ICON_BOX_SIZE;

			for (
				int index = 0;
				index < entries.size();
				index++
			) {
				int iconX =
					firstIconX
						- index * step;

				ChronosEffectRenderer.render(
					graphics,
					minecraft,
					entries.get(index),
					iconX,
					iconY,
					config
				);
			}

			return;
		}

		int firstIconX =
			SCREEN_MARGIN_X;

		for (
			int index = 0;
			index < entries.size();
			index++
		) {
			int iconX =
				firstIconX
					+ index * step;

			ChronosEffectRenderer.render(
				graphics,
				minecraft,
				entries.get(index),
				iconX,
				iconY,
				config
			);
		}
	}
}