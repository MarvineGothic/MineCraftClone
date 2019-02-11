package models;


import java.util.Arrays;

public class SimpleCubeMesh extends Mesh {
    private static float[] vertices = { // 72

            // Top 48 - 60
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,

            // Bottom 0 - 12
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,

            // Front 36 - 48
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,

            // Back 60 - 72
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,

            // Left 12 - 24
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,

            // Right 24 - 36
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,

    };
    private static float[] textures = {
            // top
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            // bottom
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            // Front
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            // Back
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            // Left
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            // Right
            0, 0,
            0, 1,
            1, 1,
            1, 0,


    };
    private static float[] map_textures = {
            // top
            0.6566667f, 0.99f,
            0.6566667f, 0.6766667f,
            0.34333333f, 0.6766667f,
            0.34333333f, 0.99f,
            // bottom
            0.32333335f, 0.34333333f,
            0.01f, 0.34333333f,
            0.01f, 0.6566667f,
            0.32333335f, 0.6566667f,
            // Front
            0.32333335f, 0.6766667f,
            0.01f, 0.6766667f,
            0.01f, 0.99f,
            0.32333335f, 0.99f,
            // Back
            0.99f, 0.99f,
            0.99f, 0.6766667f,
            0.6766667f, 0.6766667f,
            0.6766667f, 0.99f,
            // Left
            0.6566667f, 0.34333333f,
            0.34333333f, 0.34333333f,
            0.34333333f, 0.6566667f,
            0.6566667f, 0.6566667f,
            // Right
            0.6766667f, 0.34333333f,
            0.6766667f, 0.6566667f,
            0.99f, 0.6566667f,
            0.99f, 0.34333333f,
    };
    private static int[] indices = {
            // Top
            0, 1, 2,
            2, 3, 0,
            // Bottom
            4, 5, 6,
            6, 7, 4,
            // Front
            8, 9, 10,
            10, 11, 8,
            // Back
            12, 13, 14,
            12, 14, 15,
            // Left
            16, 17, 18,
            18, 19, 16,
            // Right
            20, 21, 22,
            22, 23, 20

    };
    private static float[] normals = {
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

            // Left face
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,

            // Right face
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,

    };
    private float boundingRadius = 0.5f;

    public SimpleCubeMesh() {
        super(vertices, indices, textures, normals);
    }

    public SimpleCubeMesh(float[] textures) {
        super(vertices, indices, textures, normals);
    }


    public static float[] setTextures(int rows, int cols, int rowsOffset, int colsOffset) {
        float[] result = new float[48];
        float[] init = {
                1, 1,
                1, 0,
                0, 0,
                0, 1,
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

    public static float[] getMap_textures() {
        return map_textures;
    }

    public static float[] get_textures() {
        return textures;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(SimpleCubeMesh.setTextures(3, 3, 1, 0)));

    }
}
