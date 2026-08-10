package com.cactuscreative.dupedocs.client.gui;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import com.cactuscreative.dupedocs.client.util.Exploit;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderTickCounter;

public class PinnedHudOverlay {
    public static Exploit pinnedExploit = null;
    public static int savedX = 150;
    public static int savedY = 50;

    public static void register() {
        HudRenderCallback.EVENT.register((DrawContext context, RenderTickCounter rtc) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (pinnedExploit == null || client.world == null || client.player == null) {
                return;
            }

            if (client.options.hudHidden) {
                return;
            }
            RenderSystem.disableScissor();

            int x = savedX;
            int y = savedY;
            int width = 180;
            int height = 240;
            int x2 = x + width;
            int y2 = y + height;

            context.fill(x - 1, y - 1, x2 + 1, y2 + 1, 0xCC475569);
            context.fill(x, y, x2, y2, 0xAA0F172A);
            context.fill(x, y, x2, y + 14, 0xCC1E293B);

            context.drawText(client.textRenderer, "Pinned: Exploit #" + pinnedExploit.id(), x + 6, y + 3, 0xFF94A3B8, false);

            int contentY = y + 20;
            context.drawText(client.textRenderer, pinnedExploit.title(), x + 8, contentY, 0xFFF8FAFC, false);
            contentY += 14;

            for (net.minecraft.text.OrderedText descLine : client.textRenderer.wrapLines(net.minecraft.text.Text.literal(pinnedExploit.description()), width - 16)) {
                context.drawText(client.textRenderer, descLine, x + 8, contentY, 0xFF94A3B8, false);
                contentY += 10;
            }
            contentY += 6;

            context.drawText(client.textRenderer, "Steps:", x + 8, contentY, 0xFFF8FAFC, false);
            contentY += 12;

            if (pinnedExploit.reproSteps() != null) {
                for (String step : pinnedExploit.reproSteps()) {
                    String bullet = "• " + step;
                    for (net.minecraft.text.OrderedText wrapped : client.textRenderer.wrapLines(net.minecraft.text.Text.literal(bullet), width - 20)) {
                        if (contentY > y2 - 10) break;
                        context.drawText(client.textRenderer, wrapped, x + 12, contentY, 0xFF64748B, false);
                        contentY += 10;
                    }
                }
            }
        });
    }
}
