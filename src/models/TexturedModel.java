package models;

import Textures.Material;

public class TexturedModel {
    private MeshModel meshModel;
    private Material material;

    public TexturedModel(MeshModel meshModel, Material material) {
        this.meshModel = meshModel;
        this.material = material;
    }

    public MeshModel getMeshModel() {
        return meshModel;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
