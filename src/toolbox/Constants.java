package toolbox;

import models.MeshModel;
import models.SimpleCubeMesh;
import renderEngine.Loader;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Constants {
    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;

    public static final int CHUNK_SIZE = 16;
    public static final int WORLD_SIZE = 10 * CHUNK_SIZE;

    public static final SimpleCubeMesh cube = new SimpleCubeMesh(SimpleCubeMesh.getMap_textures());
    public static final MeshModel CUBE_6 = Loader.loadToVAO(cube, GL_STATIC_DRAW);
    public static final MeshModel CUBE = Loader.loadToVAO(new SimpleCubeMesh(), GL_STATIC_DRAW);

}
