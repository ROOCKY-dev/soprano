package adris.altoclef.ui.gui.mc;

import adris.altoclef.ui.gui.kit.Keys;
import baritone.compat.input.InputWithModifiers;

// 1.21.1 sends the same GLFW codes used by the kit and guiKey setting.
public final class McKeys {

    private McKeys() {
    }

    // 1.21.1 and the kit both use GLFW key codes and zero-based mouse buttons
    public static int toKit(int key) { return key <= 0 ? Keys.NONE : key; }
    public static int toGame(int key) { return key; }
    public static int buttonToKit(int button) { return button; }

    // the events carry the modifiers that were held for that very input, so no asking the keyboard
    public static int mods(InputWithModifiers e) {
        int m = 0;
        if (e.hasShiftDown()) {
            m |= Keys.MOD_SHIFT;
        }
        // the shortcut modifier: ctrl, or cmd on a mac, same as 1.21.4's Screen.hasControlDown
        if (e.hasControlDownWithQuirk()) {
            m |= Keys.MOD_CTRL;
        }
        if (e.hasAltDown()) {
            m |= Keys.MOD_ALT;
        }
        return m;
    }
}
