package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.glViewport;


public class DisplayManager {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private static final int FPS_CAP = 60;

    private static long lastFrame;
    private static float delta;
    private static long lastFPS;
    private static int c_fps = 0;
    private static int fps = 0;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setResizable(true);
            Display.create(new PixelFormat(), attribs);
            //Display.setTitle("MineKraft");
            Display.setFullscreen(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        updateViewPort();

        getDelta();
        lastFPS = getCurrentTime();
    }

    public static void updateViewPort() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();


        updateViewPort();

        // grab Mouse:
        while (Keyboard.next())
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && Mouse.isGrabbed())
                    Mouse.setGrabbed(false);
            }

        while (Mouse.next())
            if (Mouse.getEventButtonState()) {
                if (Mouse.isButtonDown(0) && !Mouse.isGrabbed())
                    Mouse.setGrabbed(true);
            }

        delta = getDelta() / 1000f;
        updateFPS();
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static void closeDisplay() {
        Display.destroy();
        System.exit(0);
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    /**
     * Get the accurate system time
     *
     * @return The system time in milliseconds
     */
    public static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    public static void updateFPS() {
        if (getCurrentTime() - lastFPS > 1000) {
            fps = c_fps;
            c_fps = 0;
            lastFPS += 1000;
        }
        c_fps++;
    }

    /**
     * Calculate how many milliseconds have passed
     * since last frame.
     *
     * @return milliseconds passed since last frame
     */
    public static int getDelta() {
        long time = getCurrentTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    public static int getFPS() {
        return fps;
    }
}
