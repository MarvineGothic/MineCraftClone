package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(100, 35, 20);
    private float pitch = 10;
    private float yaw = 0;


    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        if (Mouse.isGrabbed()) {
            if (player.isCollision()) {
                calculateZoom();
                calculatePitch();
                calculateAngleAroundPlayer();
                float horizontalDistance = calculateHorizontalDistance();
                float verticalDistance = calculateVerticalDistance();
                calculateCameraPosition(horizontalDistance, verticalDistance);
                this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
            } else {
                this.checkInputsNoCollision();
            }
        }
    }

    public void checkInputsNoCollision() {
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

        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    public Vector3f findLookAtVertexCoordinates() {
        float y = 0;
        float radius = (float) (this.getPosition().y / Math.tan(Math.toRadians(pitch)));
        float z = (float) (radius * Math.cos(Math.toRadians(yaw))) - position.z;
        float x = (float) (radius * Math.sin(Math.toRadians(yaw))) + position.x;
        return new Vector3f(x, y, -z);
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance;
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
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
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
}