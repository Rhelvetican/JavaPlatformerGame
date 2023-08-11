package org.twelvegames;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardListener {
    private static KeyboardListener instance;
    private boolean[] keyPressed = new boolean[350];

    private KeyboardListener() {

    }

    private static KeyboardListener getKeyboardListener() {
        if (KeyboardListener.instance == null) {
            KeyboardListener.instance = new KeyboardListener();
        }
        return KeyboardListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key < getKeyboardListener().keyPressed.length) {
                getKeyboardListener().keyPressed[key] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (key < getKeyboardListener().keyPressed.length) {
                getKeyboardListener().keyPressed[key] = false;
            }
        }
    }

    public static boolean getKeyPressed(int keycode) {
        if (keycode < getKeyboardListener().keyPressed.length) {
            return getKeyboardListener().keyPressed[keycode];
        } else return false;
    }


}
