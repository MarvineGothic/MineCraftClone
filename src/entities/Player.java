package entities;

import Models.TexturedModel;
import RenderEngine.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;


public class Player extends Entity {
    private static final float RUN_SPEED = 50f;
    private static final float TURN_SPEED = 160f;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;

    private static final float TERRAIN_HEIGHT = 0;
    private float terrainHeight = 0;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;
    private boolean isInAir = false;
    private boolean isJump = true;

    private boolean collision = true;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public Player(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(position, rotX, rotY, rotZ, scale);
    }

    public void move() {
        if (Mouse.isGrabbed()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_C))
                collision = !collision;

            if (collision) {
                checkInputsCollision();
                super.increasedRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
                float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
                float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
                float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
                super.increasedPosition(dx, 0, dz);
                //terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
                upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
                super.increasedPosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
                if (super.getPosition().y < terrainHeight+2) {
                    upwardsSpeed = 0;
                    super.getPosition().y = terrainHeight+2;
                    isInAir = false;
                }
            }
        }
    }

    private void jump() {
        if (isJump) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputsCollision() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W) && !isInAir) {
            this.currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !isInAir) {
            this.currentSpeed = -RUN_SPEED;
        } else if (!isInAir)
            this.currentSpeed = 0;

        if (!isInAir) {
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                this.currentTurnSpeed = -TURN_SPEED;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                this.currentTurnSpeed = TURN_SPEED;
            } else this.currentTurnSpeed = 0;

            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                jump();
                isJump = false;
            } else isJump = true;

        } else this.currentTurnSpeed = 0;
    }

    public boolean isCollision() {
        return collision;
    }
}
