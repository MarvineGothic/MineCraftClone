package entities;

import Textures.VoxelType;
import models.MeshModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import java.util.Objects;


public class Entity {
    private TexturedModel texturedModel;
    private VoxelType voxelType;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale = 1;
    private Matrix4f transformationMatrix;

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
        updateTransformationMatrix();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Float.compare(entity.position.x, position.x) == 0 &&
                Float.compare(entity.position.y, position.y) == 0 &&
                Float.compare(entity.position.z, position.z) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(position.x, position.y, position.z);
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

    public MeshModel getModel() {
        return getTexturedModel().getMeshModel();
    }

    public Entity setModel(VoxelType voxelType) {
        this.voxelType = voxelType;
        this.texturedModel = voxelType.getTexturedModel();
        return this;
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

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    public void updateTransformationMatrix() {
        this.transformationMatrix = Maths.createTransformationMatrix(this.position, this.rotX, this.rotY, this.rotZ, this.scale);
    }
}
