package com.cactuscreative.dupestore.client.gui;

import com.cactuscreative.dupestore.client.DupeStoreClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.text.Text;
import com.cactuscreative.dupestore.client.util.Exploit;
import java.util.ArrayList;
import java.util.List;

public class MainScrollFrame extends ScrollableWidget {
    private final TextRenderer textRenderer;
    private final List<ScrollItem> items = new ArrayList<>();
    private final java.util.function.Consumer<Exploit> onExploitSelected;

    private final int itemHeight = 48;
    private final int padding = 8;
    private final int gap = 6;

    public MainScrollFrame(TextRenderer textRenderer, int x, int y, int width, int height, java.util.function.Consumer<Exploit> onExploitSelected) {
        super(x, y, width, height, Text.empty());
        this.textRenderer = textRenderer;
        this.onExploitSelected = onExploitSelected;
        this.refreshItems();
    }

    public void refreshItems() {
        this.items.clear();
        if (DupeStoreClient.exploits != null) {
            for (Exploit exploit : DupeStoreClient.exploits) {
                String title = exploit.title() != null ? exploit.title() : "Unknown Exploit";
                String desc = exploit.description() != null ? exploit.description() : "No description.";
                String detailsLabel = "#" + exploit.id();

                this.items.add(new ScrollItem(title, detailsLabel, desc, exploit));
            }
        }
        this.setScrollY(this.getScrollY());
    }

    @Override
    protected int getContentsHeight() {
        if (items.isEmpty()) return 0;
        return (items.size() * itemHeight) + ((items.size() - 1) * gap) + (padding * 2);
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 9.0;
    }

    protected boolean getScrollbarPresence() {
        return false;
    }

    @Override
    protected void renderOverlay(DrawContext context) {
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        int currentX = this.getX() + padding;
        int currentY = this.getY() + padding;
        int rowWidth = this.getWidth() - (padding * 2);

        int correctedMouseY = mouseY + (int) this.getScrollY();

        for (int i = 0; i < items.size(); i++) {
            ScrollItem item = items.get(i);
            int itemY = currentY + (i * (itemHeight + gap));

            boolean isHovered = mouseX >= currentX && mouseX <= currentX + rowWidth &&
                    correctedMouseY >= itemY && correctedMouseY <= itemY + itemHeight &&
                    this.isMouseOver(mouseX, mouseY);

            int bgColor = isHovered ? 0xFF3B4F67 : 0xFF1E293B;
            context.fill(currentX, itemY, currentX + rowWidth, itemY + itemHeight, bgColor);

            int accentColor = isHovered ? 0xFF38BDF8 : 0xFF475569;
            context.fill(currentX, itemY, currentX + 2, itemY + itemHeight, accentColor);

            context.drawText(textRenderer, item.title, currentX + 8, itemY + 6, 0xFFF8FAFC, false);
            context.drawText(textRenderer, item.details, currentX + rowWidth - textRenderer.getWidth(item.details) - 8, itemY + 6, 0xFF94A3B8, false);

            String rawDesc = item.description;
            int maxDescWidth = rowWidth - 20;
            if (textRenderer.getWidth(rawDesc) > maxDescWidth) {
                rawDesc = textRenderer.trimToWidth(rawDesc, maxDescWidth - textRenderer.getWidth("...")) + "...";
            }
            context.drawText(textRenderer, rawDesc, currentX + 8, itemY + 22, 0xFF64748B, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.isMouseOver(mouseX, mouseY)) {
            int currentY = this.getY() + padding;
            int correctedMouseY = (int) (mouseY + (int) this.getScrollY());

            for (int i = 0; i < items.size(); i++) {
                int itemY = currentY + (i * (itemHeight + gap));
                if (correctedMouseY >= itemY && correctedMouseY <= itemY + itemHeight) {
                    if (onExploitSelected != null) {
                        onExploitSelected.accept(items.get(i).originalExploit);
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

    private record ScrollItem(String title, String details, String description, Exploit originalExploit) {}
}
