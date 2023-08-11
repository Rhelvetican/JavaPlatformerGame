package org.twelvegames.engine;

import java.awt.event.KeyEvent;

public class LevelScene extends Scene {
    private boolean changingScene = false;
    private float time2ChangeScene = 2.0f;
    public LevelScene() {
        System.out.println("Level Scene.");
    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyboardListener.getKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        };

        if (changingScene && (time2ChangeScene > 0)) {

            time2ChangeScene =- dt;
            Window.getWindow().r -= dt * 5.0f;
            Window.getWindow().g -= dt * 5.0f;
            Window.getWindow().b -= dt * 5.0f;

        } else if (changingScene) {

            Window.changeScene(0);

        }
    }
}
