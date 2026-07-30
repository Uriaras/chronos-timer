package com.nythral.chronos.client.config;

public final class ChronosConfig {

    public static final ChronosConfig INSTANCE =
        new ChronosConfig();

    private static final int DEFAULT_OPACITY = 100;

    private boolean enabled = true;
    private ChronosAnchor anchor = ChronosAnchor.TOP;
    private ChronosSide side = ChronosSide.RIGHT;
    private ChronosLayout layout = ChronosLayout.HORIZONTAL;
    private ChronosDisplayMode displayMode = ChronosDisplayMode.TIMER;
    private ChronosAttachment attachment = ChronosAttachment.BOTTOM;
    private int opacity = DEFAULT_OPACITY;

    public ChronosConfig() {
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
                : ChronosLayout.HORIZONTAL;
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
                : ChronosAttachment.BOTTOM;
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
            Math.max(
                0,
                Math.min(
                    100,
                    opacity
                )
            );
    }

    public void copyFrom(
        ChronosConfig source
    ) {
        if (source == null) {
            reset();
            return;
        }

        setEnabled(source.enabled);
        setAnchor(source.anchor);
        setSide(source.side);
        setLayout(source.layout);
        setDisplayMode(source.displayMode);
        setAttachment(source.attachment);
        setOpacity(source.opacity);
    }

    public void reset() {
        this.enabled = true;
        this.anchor = ChronosAnchor.TOP;
        this.side = ChronosSide.RIGHT;
        this.layout = ChronosLayout.HORIZONTAL;
        this.displayMode = ChronosDisplayMode.TIMER;
        this.attachment = ChronosAttachment.BOTTOM;
        this.opacity = DEFAULT_OPACITY;
    }
}