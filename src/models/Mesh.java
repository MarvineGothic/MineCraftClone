package models;


import java.util.List;

public class Mesh {
    private float[] vertices;
    private int[] indices;
    private float[] textures;
    private float[] normals;

    public Mesh(float[] vertices, int[] indices, float[] textures, float[] normals) {
        this.vertices = vertices;
        this.indices = indices;
        if (textures != null)
            this.textures = textures;
        if (normals != null)
            this.normals = normals;
    }

    public Mesh(List<Object> vertices, List<Object> indices, List<Object> textures, List<Object> normals) {
        this(listToArrayf(vertices), listToArrayi(indices), listToArrayf(textures), listToArrayf(normals));
    }

    public static float[][] splitSides(float[] data, int offset) {
        float[][] s_vertices = new float[6][offset];
        int a = 0;
        for (int j = 0; j < 6; j++) {
            for (int i = a; i < a + data.length / 6; i++)
                s_vertices[j][i - a] = data[i];
            a = a + offset;
        }
        return s_vertices;
    }

    public static int[][] splitSides(int[] data, int offset) {
        int[][] s_vertices = new int[6][offset];
        int a = 0;
        for (int j = 0; j < 6; j++) {
            for (int i = a; i < a + data.length / 6; i++)
                s_vertices[j][i - a] = data[i];
            a = a + offset;
        }
        return s_vertices;
    }

    private static float[] listToArrayf(List<Object> elements) {
        float[] result = new float[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            result[i] = (float) elements.get(i);
        }
        return result;
    }

    private static int[] listToArrayi(List<Object> elements) {
        int[] result = new int[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            result[i] = (int) elements.get(i);
        }
        return result;
    }

    private static void listToArray(List<Object> elements, Object[] dest) {
        for (int i = 0; i < elements.size(); i++) {
            dest[i] = elements.get(i);
        }
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public float[] getTextures() {
        return textures;
    }

    public float[] getNormals() {
        return normals;
    }
}
