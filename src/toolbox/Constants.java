package toolbox;

import models.Model;
import models.SimpleCubeObject;
import renderEngine.Loader;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Constants {
    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;

    public static final int CHUNK_SIZE = 16;
    public static final int WORLD_SIZE = 5 * CHUNK_SIZE;

    public static final SimpleCubeObject cube = new SimpleCubeObject(SimpleCubeObject.getMap_textures());
    public static final Model CUBE_6 = Loader.loadToVAO(cube, GL_STATIC_DRAW);
    public static final Model CUBE = Loader.loadToVAO(new SimpleCubeObject(), GL_STATIC_DRAW);

}
