package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import static toolbox.Constants.*;

public class Camera {

    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    // private Matrix4f invertedProjectionMatrix; // for ray casting
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 0;
    private float yaw = 0;
    private boolean needUpdate = true;


    private Player player;

    public Camera(Player player) {
        this.player = player;
        projectionMatrix = Maths.createProjectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);
        //Matrix4f.invert(projectionMatrix, invertedProjectionMatrix);
    }

    public void update() {
        updateViewMatrix();
        if (Mouse.isGrabbed()) {
            if (player.isCollision()) {
                /*calculateZoom();
                calculatePitch();
                calculateAngleAroundPlayer();
                float horizontalDistance = calculateHorizontalDistance();
                float verticalDistance = calculateVerticalDistance();
                calculateCameraPosition(horizontalDistance, verticalDistance);
                this.yaw = 180 - (player.getRotY() + angleAroundPlayer);*/
            } else if (!player.isCollision()) {
                this.checkInputsNoCollision2();
            }
        } else this.needUpdate = false;
    }

    private void updateViewMatrix() {
        viewMatrix = Maths.createViewMatrix(this);
    }

    /*public void checkInputsNoCollision() {
        float moveAt;
        float speed = 0.3f;
        float sideSpeed;
        float turnSpeed = 0.1f;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) moveAt = -speed;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S)) moveAt = speed;
        else moveAt = 0;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) sideSpeed = -speed;
        else if (Keyboard.isKeyDown(Keyboard.KEY_A)) sideSpeed = speed;
        else sideSpeed = 0;

        pitch += -Mouse.getDY() * turnSpeed;
        yaw += Mouse.getDX() * turnSpeed;

        float dx = (float) -(moveAt * Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) + (sideSpeed * Math.cos(Math.toRadians(yaw))));
        float dy = (float) (moveAt * Math.sin(Math.toRadians(pitch)));
        float dz = (float) (moveAt * Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) - sideSpeed * Math.sin(Math.toRadians(yaw)));

        increasePosition(dx, dy, dz);
    }*/

    public void checkInputsNoCollision2() {
        float oldPitch = pitch;
        float oldYaw = yaw;
        float oldX = position.x;
        float oldY = position.y;
        float oldZ = position.z;

        float moveAt;
        float speed = 0.3f;
        float sideSpeed;
        float turnSpeed = 0.1f;
        float vertical;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) moveAt = -speed;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S)) moveAt = speed;
        else moveAt = 0;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) sideSpeed = -speed;
        else if (Keyboard.isKeyDown(Keyboard.KEY_A)) sideSpeed = speed;
        else sideSpeed = 0;
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) vertical = -speed;
        else if (Keyboard.isKeyDown(Keyboard.KEY_E)) vertical = speed;
        else vertical = 0;

        pitch += -Mouse.getDY() * turnSpeed;
        yaw += Mouse.getDX() * turnSpeed;

        float dx = (float) -(moveAt * Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) + (sideSpeed * Math.cos(Math.toRadians(yaw))));
        float dy = vertical;
        float dz = (float) (moveAt * Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) - sideSpeed * Math.sin(Math.toRadians(yaw)));


        increasePosition(dx, dy, dz);
        // check if camera changed position (optimization):
        needUpdate = oldX != position.x || oldY != position.y || oldZ != position.z || oldPitch != pitch || oldYaw != yaw;
    }

    public void increasePosition(float dx, float dy, float dz) {
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    /**
     * Experimental (not effective) method to locate "look at" voxels
     *
     * @param height
     * @param maxDistance
     * @return
     */
    public Vector3f findLookAtVoxelCoordinates(float height, float maxDistance) {
        float y = height;
        float distance = (float) Math.abs((position.y - y) / Math.sin(Math.toRadians(pitch)));
        if (distance > maxDistance) return null;
        float radius = (float) (this.getPosition().y / Math.tan(Math.toRadians(pitch)));
        float z = (float) (radius * Math.cos(Math.toRadians(yaw))) - position.z;
        float x = (float) (radius * Math.sin(Math.toRadians(yaw))) + position.x;
        return new Vector3f(x, y, -z);
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
        if (distanceFromPlayer <= 0) distanceFromPlayer = 0;
    }

    private void calculatePitch() {
        //if (Mouse.isButtonDown(1)) {
        float pitchChange = Mouse.getDY() * 0.1f;
        pitch -= pitchChange;
        //}
    }

    private void calculateAngleAroundPlayer() {
        //if (Mouse.isButtonDown(0)) {
        float angleChange = Mouse.getDX() * 0.3f;
        angleAroundPlayer -= angleChange;
        //}
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }


    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }


    /*public Matrix4f getInvertedProjectionMatrix() {
        return invertedProjectionMatrix;
    }

    public void setInvertedProjectionMatrix(Matrix4f invertedProjectionMatrix) {
        this.invertedProjectionMatrix = invertedProjectionMatrix;
    }
*/

    /**
     * Tells the program to update if camera changed position (optimization)
     *
     * @return
     */
    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
}