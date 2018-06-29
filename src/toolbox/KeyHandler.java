package toolbox;

import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public class KeyHandler {
    private static HashMap<Integer, Boolean> keyInputs = new HashMap<>();


    /**
     * Single key press
     *
     * @param key
     * @return
     */
    public static boolean keySinglePress(int key) {
        keyInputs.putIfAbsent(key, false);
        boolean pressed = keyInputs.get(key);
        if (!Keyboard.isKeyDown(key))
            pressed = false;
        if (pressed) return false;
        if (Keyboard.isKeyDown(key) && !pressed)
            pressed = true;
        keyInputs.put(key, pressed);
        return pressed;
    }
}
