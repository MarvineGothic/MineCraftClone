package entities;

import Models.RawModel;
import Models.SimpleCubeModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class Light extends Entity {
    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1,0,0);

    public Light(Vector3f position, Vector3f color) {
        super(position);
        super.setScale(10);
        setTexturedModel();
        this.position = position;
        this.color = color;
    }
    public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        super(position);
        super.setScale(10);
        setTexturedModel();
        this.position = position;
        this.color = color;
        this.attenuation =attenuation;
    }

    public void setTexturedModel() {
        RawModel sunModel = Loader.loadToVAO(SimpleCubeModel.getVertices(), SimpleCubeModel.getIndices(),SimpleCubeModel.getTextures(), SimpleCubeModel.getNormals());
        ModelTexture treeBark = new ModelTexture(Loader.loadTexture("TreeBark"));
        super.setModel(new TexturedModel(sunModel, treeBark));
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
