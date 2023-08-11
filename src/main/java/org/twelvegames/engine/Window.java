package org.twelvegames.engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.twelvegames.utils.Time;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class Window {
    private int width, height;
    private String title;
    private long glWindow;
    private static Window window;
    private static int currentSceneIndex = -1;
    private static Scene currentScene;

    public float r, g, b, a;
    private boolean fade2Black;
    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "Unnamed 0";
        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false: "Unknown Scene Index used.";
        }
    }

    public static Window getWindow() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("System initialized.");
        System.out.println("LWJGL Version " + Version.getVersion());

        //Main execute block
        init();
        loop();

        //Post-close
        glfwFreeCallbacks(glWindow);
        glfwDestroyWindow(glWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).set();
    }

    public void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glWindow == NULL) throw new IllegalStateException("Window not created.");

        glfwSetKeyCallback(glWindow, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        glfwSetCursorPosCallback(glWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glWindow, MouseListener::mouseActionCallback);
        glfwSetScrollCallback(glWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glWindow, KeyboardListener::keyCallback);

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(glWindow, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    glWindow,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
        glfwMakeContextCurrent(glWindow);

        glfwSwapInterval(1);

        glfwShowWindow(glWindow);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dTime = -1.0f;

        GL.createCapabilities();

        //Main loop
        while (!glfwWindowShouldClose(glWindow)) {
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(glWindow);
            glfwPollEvents();

            endTime = Time.getTime();
            dTime = endTime - beginTime;
            beginTime = endTime;

            if (dTime >= 0) {
                currentScene.update(dTime);
            }
        }
    }
}
