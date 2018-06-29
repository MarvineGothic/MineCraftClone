package Textures;

import models.TexturedModel;

import static toolbox.Constants.CUBE;
import static toolbox.Constants.CUBE_6;

public enum VoxelType {

    EMPTY(-1, new TexturedModel(CUBE_6, new TextureObject(-1))),
    SUN(0, new TexturedModel(CUBE, new TextureObject("TreeBark").setFakeLighting(true))),
    PLAYER(1, new TexturedModel(CUBE_6, new TextureObject("grassTex"))),
    TREE_BARK(2, new TexturedModel(CUBE_6, new TextureObject("TreeBark"))),
    GRASS(3, new TexturedModel(CUBE_6, new TextureObject("grassTex"))),
    DIRT(4, new TexturedModel(CUBE, new TextureObject("dirtTex"))),
    GRASS_SIDE(5, new TexturedModel(CUBE, new TextureObject("grassSide"))),
    GRASS_TOP(6, new TexturedModel(CUBE, new TextureObject("grassTop")));


    private int id;
    private TexturedModel texturedModel;

    VoxelType(int id, TexturedModel texturedModel) {
        this.id = id;
        this.texturedModel = texturedModel;
    }

    public int getId() {
        return id;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    }
