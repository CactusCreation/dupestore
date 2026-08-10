package com.cactuscreative.dupedocs.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class MainScreen extends Screen {
    private MainPanel mainPanel;

    protected MainScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.mainPanel = new MainPanel(this.textRenderer, 100, 50, 280, 240, Text.literal("Dupe Docs"));
        this.addDrawableChild(this.mainPanel);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.mainPanel != null && this.mainPanel.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
