package Textures;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import renderEngine.Loader;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class Material {
    private int width;
    private int height;
    private ByteBuffer buffer;
    private int textureID;
    private float shineDamper = 10f;
    private float reflectance = 0f;

    private boolean transparent = false;
    private boolean fakeLighting = false;

    private int numberOfRows = 1;

    public Material(int textureID) {
        this.textureID = textureID;
    }

    public Material(String fileName) {
        this.textureID = Loader.loadTexture(fileName);
    }

    /**
     * Material from file to ByteBuffer
     *
     * @param fileName
     * @return
     */
    private ByteBuffer decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try (FileInputStream in = new FileInputStream("res/" + fileName + ".png")) {
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            this.width = width;
            this.height = height;
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return buffer;
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }

    public int getID() {
        return textureID;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public Material setTransparency(boolean hasTransparency) {
        this.transparent = hasTransparency;
        return this;
    }

    public boolean isFakeLighting() {
        return fakeLighting;
    }

    public Material setFakeLighting(boolean useFakeLighting) {
        this.fakeLighting = useFakeLighting;
        return this;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public Material setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
        return this;
    }

    public float getReflectance() {
        return reflectance;
    }

    public Material setReflectance(float reflectance) {
        this.reflectance = reflectance;
        return this;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
