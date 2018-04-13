package Models;

import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

public class SimpleCubeModel {
    static float[] vertices = {

            // Bottom
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            // Left
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            // Right
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            // Front
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            // Top
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            // Back
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
    };

    static float[] textures = {

            // Bottom
            0.01f, 1.01f / 3f,
            0.01f, 1.99f / 3f,
            0.99f / 3f, 1.99f / 3f,
            0.99f / 3f, 1.01f / 3f,
            // Left
            1.01f / 3f, 1.01f / 3f,
            1.01f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.01f / 3f,
            // Right
            1.01f / 3f, 1.01f / 3f,
            1.01f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.01f / 3f,
            // Front
            1.01f / 3f, 1.01f / 3f,
            1.01f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.01f / 3f,
            // Top
            1.01f / 3f, 2.01f / 3f,
            1.01f / 3f, 0.99f,
            1.99f / 3f, 0.99f,
            1.99f / 3f, 2.01f / 3f,
            // Back
            1.01f / 3f, 1.01f / 3f,
            1.01f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.01f / 3f
    };

    static int[] indices = {

            // Bottom inverted
            3, 2, 1,
            3, 1, 0,
            // Left  inverted
            4, 7, 5,
            5, 7, 6,
            // Right
            8, 9, 10,
            8, 10, 11,
            // Front  inverted
            12, 15, 13,
            13, 15, 14,
            // Top
            16, 17, 18,
            16, 18, 19,
            // Back
            20, 21, 22,
            20, 22, 23
    };

    static float[] normals = {
            // Front face
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,

            // Back face
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,

            // Top face
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            // Bottom face
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,

            // Right face
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,

            // Left face
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f
    };

    public static float[] getVertices() {
        return vertices;
    }

    public static float[] getTextures() {
        return textures;
    }

    public static int[] getIndices() {
        return indices;
    }

    public static float[] getNormals() {
        return normals;
    }

    public static float[] setTextures(int rows, int cols, int rowsOffset, int colsOffset) {
        float[] result = new float[48];
        float[] init = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };
        int offset = rowsOffset * cols;

        for (int i = offset; i < rows * cols; i++) {
            float[] side = new float[8];
            int colIndex = i % cols;
            int rowIndex = i / cols;
            int index;
            float textureOffset;
            for (int j = 0; j < side.length; j++) {
                index = j % 2 == 0 ? colIndex : rowIndex;
                textureOffset = init[j] == 0 ? 0.01f : -0.01f;
                side[j] = ((init[j] + index) / 3f) + textureOffset;
            }
            /*for (int k = 0; k < side.length; k++) {
                result[k + (i - offset) * 8] = side[k];
            }*/
            System.arraycopy(side, 0, result, (i - offset) * 8, side.length);
        }
        return result;
    }

    public static void main(String[] args) {
        /*float[] res = setTextures(3, 3, 1, 0);
        //System.out.println(3 % 3);
        for (int i = 0; i < res.length; i += 8) {
            for (int j = i; j < i + 8; j++)
                System.out.print(res[j] + " ");
            System.out.println();

        }*/
        Vector3f v1 = new Vector3f(0,0,0);
        Vector3f v2 = new Vector3f(-0.4f,0,-0.4f);
        Entity e1 = new Entity(v1);
        Entity e2 = new Entity(v2);
        System.out.println(e1.equals(e2));

    }
}
