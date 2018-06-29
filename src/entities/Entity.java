package entities;

import Textures.VoxelType;
import models.Model;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;


public class Entity {
    private TexturedModel texturedModel;
    private VoxelType voxelType;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale = 1;

    private boolean isSelected = false;

    public Entity(VoxelType voxelType, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this(voxelType, position);
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public Entity(VoxelType voxelType, Vector3f position) {
        setModel(voxelType).setPosition(position);
    }


    public Entity setModel(VoxelType voxelType) {
        this.voxelType = voxelType;
        this.texturedModel = voxelType.getTexturedModel();
        return this;
    }


    @Override
    public boolean equals(Object o) {
        Entity entity = (Entity) o;
        if (this == o) return true;
        if (o == null || entity.getPosition() == null || getClass() != o.getClass()) return false;

        return this.position.x + 0.5 > entity.position.x && this.position.x - 0.5 < entity.position.x &&
                this.position.z + 0.5 > entity.position.z && this.position.z - 0.5 < entity.position.z &&
                this.position.y + 0.5 > entity.position.y && this.position.y - 0.5 < entity.position.y;
    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Model getModel() {
        return getTexturedModel().getModel();
    }


    public Vector3f getPosition() {
        return position;
    }

    public Entity setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public VoxelType getVoxelType() {
        return voxelType;
    }

    public float getRotX() {
        return rotX;
    }


    public float getRotY() {
        return rotY;
    }


    public float getRotZ() {
        return rotZ;
    }


    public float getScale() {
        return scale;
    }

    public Entity setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
