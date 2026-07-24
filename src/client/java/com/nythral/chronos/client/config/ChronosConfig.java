package com.nythral.chronos.client.config;

public final class ChronosConfig {

	public static final ChronosConfig INSTANCE =
		new ChronosConfig();

	private static final int DEFAULT_OPACITY = 100;

	private boolean enabled;
	private ChronosAnchor anchor;
	private ChronosSide side;
	private ChronosLayout layout;
	private ChronosDisplayMode displayMode;
	private ChronosAttachment attachment;
	private int opacity;

	private ChronosConfig() {
		reset();
	}

	public boolean enabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void toggleEnabled() {
		this.enabled = !this.enabled;
	}

	public ChronosAnchor anchor() {
		return this.anchor;
	}

	public void setAnchor(ChronosAnchor anchor) {
		this.anchor =
			anchor != null
				? anchor
				: ChronosAnchor.TOP;
	}

	public void nextAnchor() {
		this.anchor = this.anchor.next();
	}

	public ChronosSide side() {
		return this.side;
	}

	public void setSide(ChronosSide side) {
		this.side =
			side != null
				? side
				: ChronosSide.RIGHT;
	}

	public void nextSide() {
		this.side = this.side.next();
	}

	public ChronosLayout layout() {
		return this.layout;
	}

	public void setLayout(ChronosLayout layout) {
		this.layout =
			layout != null
				? layout
				: ChronosLayout.VERTICAL;
	}

	public void nextLayout() {
		this.layout = this.layout.next();
	}

	public ChronosDisplayMode displayMode() {
		return this.displayMode;
	}

	public void setDisplayMode(
		ChronosDisplayMode displayMode
	) {
		this.displayMode =
			displayMode != null
				? displayMode
				: ChronosDisplayMode.TIMER;
	}

	public void nextDisplayMode() {
		this.displayMode =
			this.displayMode.next();
	}

	public ChronosAttachment attachment() {
		return this.attachment;
	}

	public void setAttachment(
		ChronosAttachment attachment
	) {
		this.attachment =
			attachment != null
				? attachment
				: ChronosAttachment.SIDE;
	}

	public void nextAttachment() {
		this.attachment =
			this.attachment.next();
	}

	public int opacity() {
		return this.opacity;
	}

	public void setOpacity(int opacity) {
		this.opacity =
			clamp(
				opacity,
				0,
				100
			);
	}

	public void reset() {
		this.enabled = true;
		this.anchor = ChronosAnchor.TOP;
		this.side = ChronosSide.RIGHT;
		this.layout = ChronosLayout.VERTICAL;
		this.displayMode = ChronosDisplayMode.TIMER;
		this.attachment = ChronosAttachment.SIDE;
		this.opacity = DEFAULT_OPACITY;
	}

	private static int clamp(
		int value,
		int minimum,
		int maximum
	) {
		return Math.max(
			minimum,
			Math.min(
				maximum,
				value
			)
		);
	}
}