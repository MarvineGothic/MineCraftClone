package Textures;

import models.TexturedModel;

import static toolbox.Constants.CUBE;
import static toolbox.Constants.CUBE_6;

public enum VoxelType {

    EMPTY(-1, new TexturedModel(CUBE_6, new Material(-1))),
    SUN(0, new TexturedModel(CUBE, new Material("TreeBark").setFakeLighting(true))),
    PLAYER(1, new TexturedModel(CUBE_6, new Material("grassTex").setReflectance(1f))),
    TREE_BARK(2, new TexturedModel(CUBE_6, new Material("TreeBark"))),
    GRASS(3, new TexturedModel(CUBE_6, new Material("grassTex"))),
    DIRT(4, new TexturedModel(CUBE, new Material("dirtTex"))),
    GRASS_SIDE(5, new TexturedModel(CUBE, new Material("grassSide"))),
    GRASS_TOP(6, new TexturedModel(CUBE, new Material("grassTop")));


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
