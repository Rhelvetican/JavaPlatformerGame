package org.twelvegames.engine;

import java.awt.event.KeyEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;

public class LevelScene extends Scene {
    private boolean changingScene = false;
    private float time2ChangeScene = 2.0f;
    public LevelScene() {
        System.out.println("Level Scene.");
        Window.getWindow().r = 1;
        Window.getWindow().g = 1;
        Window.getWindow().b = 1;
    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyboardListener.getKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && (time2ChangeScene > 0)) {

            time2ChangeScene =- dt;
            Window.getWindow().r += dt * 5.0f;
            Window.getWindow().g += dt * 5.0f;
            Window.getWindow().b += dt * 5.0f;

        } else if (changingScene && (KeyboardListener.getKeyPressed(GLFW_KEY_0))) {

            Window.changeScene(0);

        }
    }
}
