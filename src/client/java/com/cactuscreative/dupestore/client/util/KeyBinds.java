package com.cactuscreative.dupestore.client.util;

import net.minecraft.client.option.KeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.util.InputUtil;

// This is a helper class for keybinds, currently just for opening the GUI but in the future it will be used for macros
public class KeyBinds {
    public static KeyBinding openGUIKeybind;

    public static void register() {
        openGUIKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open GUI",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "Dupe Store"
        ));
    }
}
