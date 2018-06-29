package models;

import Textures.TextureObject;

public class TexturedModel {
    private Model model;
    private TextureObject textureObject;

    public TexturedModel(Model model, TextureObject textureObject) {
        this.model = model;
        this.textureObject = textureObject;
    }

    public Model getModel() {
        return model;
    }

    public TextureObject getTextureObject() {
        return textureObject;
    }

    public void setTextureObject(TextureObject textureObject) {
        this.textureObject = textureObject;
    }
}
