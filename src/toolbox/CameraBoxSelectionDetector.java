package toolbox;

import entities.Camera;
import entities.Entity;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static toolbox.Maths.toJOMLM4f;
import static toolbox.Maths.toJOMLV3f;

public class CameraBoxSelectionDetector {

    private static final Vector3f max;

    private static final Vector3f min;

    private static final Vector2f nearFar;

    private static Vector3f dir;

    static {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public static void selectGameItem(Collection<Entity> gameItems, Camera camera) {
        Entity selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        dir = toJOMLM4f(camera.getViewMatrix()).positiveZ(dir).negate();
        for (Entity gameItem : gameItems) {
            gameItem.setSelected(false);
            min.set(toJOMLV3f(gameItem.getPosition()));
            max.set(toJOMLV3f(gameItem.getPosition()));
            min.add(-gameItem.getScale(), -gameItem.getScale(), -gameItem.getScale());
            max.add(gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
            if (Intersectionf.intersectRayAab(toJOMLV3f(camera.getPosition()), dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameItem = gameItem;
            }
        }

        if (selectedGameItem != null) {
            selectedGameItem.setSelected(true);
        }
    }
    public static void selectGameItem(Entity gameItem, Camera camera) {
        Entity selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        dir = toJOMLM4f(camera.getViewMatrix()).positiveZ(dir).negate();

            gameItem.setSelected(false);
            min.set(toJOMLV3f(gameItem.getPosition()));
            max.set(toJOMLV3f(gameItem.getPosition()));
            min.add(-gameItem.getScale(), -gameItem.getScale(), -gameItem.getScale());
            max.add(gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
            if (Intersectionf.intersectRayAab(toJOMLV3f(camera.getPosition()), dir, min, max, nearFar) && nearFar.x < closestDistance) {

                selectedGameItem = gameItem;
            }

        if (selectedGameItem != null) {
            selectedGameItem.setSelected(true);
        }
    }
}
