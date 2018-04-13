package Models;

public class AtlasCubeModel {
    static float[] vertices = {

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

            // Back
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,

            // Top
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,

            // Bottom
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,

    };

    static float[] textures = {

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

            // Front
            1.01f / 3f, 1.01f / 3f,
            1.01f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.01f / 3f,

            // Top
            1.01f/3f, 2.01f/3f,
            1.01f/3f, 0.99f,
            1.99f/3f, 0.99f,
            1.99f/3f, 2.01f/3f,

            // Bottom
            0.01f, 1.01f / 3f,
            0.01f, 1.99f / 3f,
            0.99f / 3f, 1.99f / 3f,
            0.99f / 3f, 1.01f / 3f
    };

    static int[] indices = {
            /*0, 1, 3,
            3, 1, 2,
            4, 5, 7,
            7, 5, 6,
            8, 9, 11,
            11, 9, 10,
            12, 13, 15,
            15, 13, 14,
            16, 17, 19,
            19, 17, 18,
            20, 21, 23,
            23, 21, 22*/
            // Top
            0, 1, 2,
            0, 2, 3,

            // Left
            5, 4, 6,
            6, 4, 7,

            // Right
            8, 9, 10,
            8, 10, 11,

            // Front
            13, 12, 14,
            15, 14, 12,

            // Back
            16, 17, 18,
            16, 18, 19,

            // Bottom
            21, 20, 22,
            22, 20, 23
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
}
