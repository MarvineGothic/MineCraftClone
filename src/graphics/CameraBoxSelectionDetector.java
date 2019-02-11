package graphics;

import MineKraft.MainGameLoop;
import entities.Camera;
import entities.Chunk;
import entities.Entity;
import entities.Voxel;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.Vector3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static graphics.shaders.StaticShader.VERTICES;
import static graphics.shaders.StaticShader.loadTransformationMatrix;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_ALIASED_LINE_WIDTH_RANGE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static renderEngine.Loader.storeDataInFloatBuffer;
import static renderEngine.Loader.storeDataInIntBuffer;
import static toolbox.Maths.toJOMLM4f;
import static toolbox.Maths.toJOMLV3f;

public class CameraBoxSelectionDetector {

    private static final Vector3f max;

    private static final Vector3f min;

    private static final Vector2f nearFar;

    private static Vector3f dir;

    static {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public synchronized static Voxel selectGameItem(Camera camera) {
        Entity selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        dir = toJOMLM4f(camera.getViewMatrix()).positiveZ(dir).negate();

        // TODO: 01.07.2018 processing whole chunk map with visible voxels here from MasterRenderer
        //  increased FPS from 12 to almost 60 on 5 * 16*16 world (10 * 16*16 FPS 15-20)
        for (int j = 0; j < MainGameLoop.getUsedPos().size(); j++) {
            Vector3 currentChunkPos = MainGameLoop.getUsedPos().get(j);
            Chunk currentChunk = MainGameLoop.getChunksMap().get(currentChunkPos);
            Voxel[] voxels = currentChunk.getVoxels();

            // List<Voxel> voxelList = new ArrayList<>(currentChunk.getVisibleVoxels());
            for (int i = 0; i < voxels.length; i++) {
                Voxel gameItem = voxels[i];
                if (gameItem != null && gameItem.isVisible()) {
                    gameItem.setSelected(false);
                    min.set(toJOMLV3f(gameItem.getPosition()));
                    max.set(toJOMLV3f(gameItem.getPosition()));
                    // min and max points positions depends on initial coordinates of vertex mesh. This vertex starts from -0.5f to 0.5f. Scale must be 0.5f
                    min.add(-gameItem.getScale() / 2, -gameItem.getScale() / 2, -gameItem.getScale() / 2);
                    max.add(gameItem.getScale() / 2, gameItem.getScale() / 2, gameItem.getScale() / 2);
                    if (Intersectionf.intersectRayAab(toJOMLV3f(camera.getPosition()), dir, min, max, nearFar) && nearFar.x < closestDistance) {
                        closestDistance = nearFar.x;
                        selectedGameItem = gameItem;
                    }
                }
            }
        }


        if (selectedGameItem != null) {
            selectedGameItem.setSelected(true);
        }
        return (Voxel) selectedGameItem;
    }

    public static void draw_bbox(Voxel voxel) {
        if (voxel == null) return;
        Matrix4f transfMatrix = voxel.getTransformationMatrix();
        if (transfMatrix == null) return;
        float vertices[] = {
                -0.51f, -0.51f, -0.51f, 5.0f, // 0
                0.51f, -0.51f, -0.51f, 5.0f, // 1
                0.51f, 0.51f, -0.51f, 5.0f, // 2
                -0.51f, 0.51f, -0.51f, 5.0f,  // 3

                -0.51f, -0.51f, 0.51f, 5.0f,
                0.51f, -0.51f, 0.51f, 5.0f,
                0.51f, 0.51f, 0.51f, 5.0f,
                -0.51f, 0.51f, 0.51f, 5.0f,
        };
        int indices[] = {
                0, 1, 2, 3,
                4, 5, 6, 7,
                0, 4, 1, 5, 2, 6, 3, 7
        };


        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        int vboID = glGenBuffers();
        FloatBuffer buffer = storeDataInFloatBuffer(vertices);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(VERTICES, 4, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


        //bindIndicesBuffer(indices, GL_STATIC_DRAW);
        int vboInd = glGenBuffers();
        IntBuffer Ind_buffer = storeDataInIntBuffer(indices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboInd);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Ind_buffer, GL_STATIC_DRAW);


        glBindVertexArray(0);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(VERTICES);

        loadTransformationMatrix(transfMatrix);
        glEnable(GL_LINE_SMOOTH);

        glDrawElements(GL_LINE_LOOP, 4, GL_UNSIGNED_INT, 0);
        glDrawElements(GL_LINE_LOOP, 4, GL_UNSIGNED_INT, 16);
        glDrawElements(GL_LINES, 8, GL_UNSIGNED_INT, 32);

        glDisableVertexAttribArray(VERTICES);
        glBindVertexArray(VERTICES);

    }

}
