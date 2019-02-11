package renderEngine;

import models.Mesh;
import models.MeshModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static graphics.shaders.StaticShader.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Loader {
    private static List<Integer> vaos = new ArrayList<>();
    private static List<Integer> vbos = new ArrayList<>();
    private static List<Integer> textures = new ArrayList<>();

    public static int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    public static int loadTexture(String fileName) {
        int textureID = 0;
        try (FileInputStream is = new FileInputStream("res/" + fileName + ".png")) {
            Texture texture = TextureLoader.getTexture("PNG", is);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -8f);

            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            textureID = texture.getTextureID();
            textures.add(textureID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textureID;
    }

    public static MeshModel loadToVAO(Mesh mesh, int usage) {
        int vaoID = createVAO();
        float[] vertices = mesh.getVertices();
        int[] indices = mesh.getIndices();
        float[] textures = mesh.getTextures();
        float[] normals = mesh.getNormals();

        bindDataBuffer(VERTICES, 3, vertices, usage);
        if (textures != null)
            bindDataBuffer(TEXTURES, 2, textures, usage);
        if (normals != null)
            bindDataBuffer(NORMALS, 3, normals, usage);
        bindIndicesBuffer(indices, usage);
        unbindVAO();
        return new MeshModel(vaoID, mesh, indices.length, usage);
    }

    public static void bindDataBuffer(int attributeNumber, int coordinateSize, float[] data, int usage) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, buffer, usage);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static void bindIndicesBuffer(int[] indices, int usage) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, usage);
    }

    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    public static void cleanUp() {
        for (int vao : vaos)
            GL30.glDeleteVertexArrays(vao);
        for (int vbo : vbos)
            glDeleteBuffers(vbo);
        for (int texture : textures)
            GL11.glDeleteTextures(texture);
    }

    public static List<Integer> getTextures() {
        return textures;
    }

    public static int loadToVAO(float[] positions, float[] textureCoords, int usage) {
        int vaoID = createVAO();
        bindDataBuffer(0, 2, positions, usage);
        bindDataBuffer(1, 2, textureCoords, usage);
        unbindVAO();
        return vaoID;
    }

    public static MeshModel loadToVAO(float[] positions, int dimensions, int usage) {
        int vaoID = createVAO();
        bindDataBuffer(0, dimensions, positions, usage);
        unbindVAO();
        return new MeshModel(vaoID, positions.length / dimensions, usage);
    }

    public static MeshModel loadToVAO(float[] positions, int[] indices, int usage) {
        int vaoID = createVAO();
        bindDataBuffer(0, 3, positions, usage);
        bindIndicesBuffer(indices, usage);
        unbindVAO();
        return new MeshModel(vaoID, indices.length, usage);
    }
}
