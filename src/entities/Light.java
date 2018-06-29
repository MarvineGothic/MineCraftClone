package entities;

import Textures.VoxelType;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;


public class Light extends Entity {
    private Vector4f position4f;
    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    public Light(VoxelType voxelType, Vector4f position4f, Vector3f color) {
        super(voxelType, new Vector3f(position4f.x, position4f.y, position4f.z));
        this.position4f = position4f;
        this.position = new Vector3f(position4f.x, position4f.y, position4f.z);
        this.color = color;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    public Vector4f getPosition4f() {
        return position4f;
    }

    public Entity setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
