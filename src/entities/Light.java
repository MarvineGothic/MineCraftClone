package entities;

import Textures.VoxelType;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;


public class Light extends Entity {
    private Vector4f lightEntityPosition;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    public Light(VoxelType voxelType, Vector4f lightEntityPosition, Vector3f color) {
        super(voxelType, new Vector3f(lightEntityPosition.x, lightEntityPosition.y, lightEntityPosition.z));
        this.lightEntityPosition = lightEntityPosition;
        this.color = color;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Light setAttenuation(Vector3f attenuation) {
        this.attenuation = attenuation;
        return this;
    }

    public Vector4f getLightPosition() {
        return lightEntityPosition;
    }

       public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
