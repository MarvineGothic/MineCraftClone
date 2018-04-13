package Models;

public class CubeModel {
    static float[] vertices = {
            // Top
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,

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

            // Bottom
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,

    };

    static float[] textures = {
            0, 0,
            0, 1,
            1, 1,
            1, 0,

            0, 0,
            1, 0,
            1, 1,
            0, 1,

            1, 1,
            0, 1,
            0, 0,
            1, 0,

            1, 1,
            1, 0,
            0, 0,
            0, 1,

            0, 0,
            0, 1,
            1, 1,
            1, 0,

            1, 1,
            1, 0,
            0, 0,
            0, 1
    };

    static int[] indices = {
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
