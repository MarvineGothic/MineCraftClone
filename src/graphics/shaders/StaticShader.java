package graphics.shaders;


import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;


public class StaticShader extends ShaderProgram {
    public static final int VERTICES = 0;
    public static final int TEXTURES = 1;
    public static final int NORMALS = 2;

    private static final String VERTEX_FILE = "src/graphics/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/graphics/shaders/fragmentShader.glsl";

    private static int location_modelMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_attenuation;
    private static int location_shineDamper;
    private static int location_reflectance;
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
        location_modelMatrix = super.getUniformLocation("modelMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("pointLight.position");
        location_lightColor = super.getUniformLocation("pointLight.color");
        location_attenuation = super.getUniformLocation("pointLight.attenuation");
        location_outSelected = super.getUniformLocation("outSelected");
         location_shineDamper = super.getUniformLocation("material.shineDamper");
         location_reflectance = super.getUniformLocation("material.reflectance");
        location_useFakeLighting = super.getUniformLocation("material.useFakeLighting");
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
        loadFloat(location_numberOfRows, numberOfRows);
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

    public static void loadShineVariables(float damper, float reflectivity) {
        loadFloat(location_shineDamper, damper);
        loadFloat(location_reflectance, reflectivity);
    }

    public static void loadTransformationMatrix(Matrix4f matrix4f) {
        loadMatrix(location_modelMatrix, matrix4f);
    }

    public void loadLight(Light light) {
        super.loadVector4f(location_lightPosition, light.getLightPosition());
        super.loadVector(location_lightColor, light.getColor());
        super.loadVector(location_attenuation, light.getAttenuation());
    }

    public void loadViewMatrix(Camera camera) {
        loadMatrix(location_viewMatrix, camera.getViewMatrix());
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }


}
