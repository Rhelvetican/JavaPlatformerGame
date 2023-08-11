package org.twelvegames;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

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
    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "Unnamed 0";
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
        GL.createCapabilities();
        glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
        while (!glfwWindowShouldClose(glWindow)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(glWindow);
            glfwPollEvents();
        }
    }
}
