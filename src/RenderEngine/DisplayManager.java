package RenderEngine;

import MineKraft.MainGameLoop;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class DisplayManager {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private static final int FPS_CAP = 120;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setResizable(true);
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("MineKraft");
            Display.setFullscreen(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && Mouse.isGrabbed())
                    Mouse.setGrabbed(false);
            }
        }
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                if (Mouse.isButtonDown(0) && !Mouse.isGrabbed())
                    Mouse.setGrabbed(true);
            }
        }
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    public static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}
