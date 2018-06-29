package graphics.shaders;


import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;


public class StaticShader extends ShaderProgram {
    public static final int VERTICES = 0;
    public static final int TEXTURES = 1;
    public static final int NORMALS = 2;

    private static final String VERTEX_FILE = "src/graphics/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/graphics/shaders/fragmentShader.glsl";

    private static int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private static int location_useFakeLighting;
    private static int location_outSelected;
    private int location_skyColor;
    private int location_numberOfRows;
    private int location_offset;

    public StaticShader(Matrix4f projection) {
        super(VERTEX_FILE, FRAGMENT_FILE);
        start();
        loadProjectionMatrix(projection);
        stop();
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_outSelected = super.getUniformLocation("outSelected");
        // location_shineDamper = super.getUniformLocation("shineDamper");
        // location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        /* location_skyColor = super.getUniformLocation("skyColor");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset = super.getUniformLocation("offset");*/
    }

    //   skyColor
    @Override
    protected void bindAttributes() {
        super.bindAttribute(VERTICES, "position");
        super.bindAttribute(TEXTURES, "textures");
        super.bindAttribute(NORMALS, "normal");
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    public void loadOffset(float x, float y) {
        super.load2DVector(location_offset, new Vector2f(x, y));
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }

    public static void loadFakeLightingVariable(boolean useFake) {
        loadBoolean(location_useFakeLighting, useFake);
    }

    public static void loadSelectedEntity(boolean selected) {
        loadBoolean(location_outSelected, selected);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public static void loadTransformationMatrix(Matrix4f matrix4f) {
        loadMatrix(location_transformationMatrix, matrix4f);
    }

    public void loadLight(Light light) {
        super.loadVector4f(location_lightPosition, light.getPosition4f());
        super.loadVector(location_lightColor, light.getColor());
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        camera.setViewMatrix(viewMatrix);
        loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }


}
