package com.cactuscreative.dupedocs.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;
import net.minecraft.client.font.TextRenderer;
import com.cactuscreative.dupedocs.client.util.Exploit;

public class MainPanel implements Drawable, Element, Selectable {
    private int x;
    private int y;
    private final int width;
    private final int height;
    private final Text title;
    private final TextRenderer textRenderer;

    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    private final int headerHeight = 16;
    private final MainScrollFrame scrollFrame;
    private ExploitDetailPanel detailPanel = null;

    public MainPanel(TextRenderer textRenderer, int x, int y, int width, int height, Text title) {
        this.textRenderer = textRenderer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;

        this.scrollFrame = new MainScrollFrame(
                textRenderer, x + 4, y + headerHeight + 4, width - 8, height - headerHeight - 8,
                this::openDetailWindow
        );

        if (PinnedHudOverlay.pinnedExploit != null) {
            this.detailPanel = new ExploitDetailPanel(
                    textRenderer, PinnedHudOverlay.savedX, PinnedHudOverlay.savedY,
                    PinnedHudOverlay.pinnedExploit, () -> {
                this.detailPanel = null;
                PinnedHudOverlay.pinnedExploit = null;
            }
            );
            this.detailPanel.setPinned(true);
        }
    }

    private void openDetailWindow(Exploit exploit) {
        this.detailPanel = new ExploitDetailPanel(
                this.textRenderer, this.x + this.width + 10, this.y, exploit,
                () -> {
                    this.detailPanel = null;
                    PinnedHudOverlay.pinnedExploit = null;
                }
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x2 = x + width;
        int y2 = y + height;

        context.fill(x - 1, y - 1, x2 + 1, y2 + 1, 0xFF334155);
        context.fill(x, y, x2, y2, 0xF00F172A);
        context.fill(x, y, x2, y + headerHeight, 0xFF1E293B);
        context.fill(x, y + headerHeight - 1, x2, y + headerHeight, 0x80475569);
        context.drawText(this.textRenderer, this.title, x + 6, y + 4, 0xFFF8FAFC, false);

        this.scrollFrame.render(context, mouseX, mouseY, delta);

        if (this.detailPanel != null) {
            this.detailPanel.render(context, mouseX, mouseY, delta);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return this.scrollFrame.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.detailPanel != null && this.detailPanel.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (this.scrollFrame.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (button == 0 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + headerHeight) {
            this.isDragging = true;
            this.dragOffsetX = (int) (mouseX - x);
            this.dragOffsetY = (int) (mouseY - y);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.detailPanel != null && this.detailPanel.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }

        if (this.scrollFrame.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }

        if (this.isDragging && button == 0) {
            this.x = (int) (mouseX - this.dragOffsetX);
            this.y = (int) (mouseY - this.dragOffsetY);

            this.scrollFrame.setX(this.x + 4);
            this.scrollFrame.setY(this.y + headerHeight + 4);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.detailPanel != null) this.detailPanel.mouseReleased(mouseX, mouseY, button);
        this.scrollFrame.mouseReleased(mouseX, mouseY, button);
        if (button == 0) {
            this.isDragging = false;
        }
        return true;
    }

    @Override public void setFocused(boolean focused) {}
    @Override public boolean isFocused() { return false; }
    @Override public SelectionType getType() { return SelectionType.NONE; }
    @Override public void appendNarrations(NarrationMessageBuilder builder) {}
}
