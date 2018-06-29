package renderEngine;


import entities.*;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import graphics.shaders.StaticShader;

import java.util.*;

import static toolbox.CameraBoxSelectionDetector.selectGameItem;

public class MasterRenderer {

    private static final float RED = 0.5f;
    private static final float GREEN = 0.7f;
    private static final float BLUE = 0.9f;


    private static StaticShader shader;
    private Camera camera;
    private Map<TexturedModel, List<Entity>> meshes;
    private List<Entity> visibleVoxels;

    public MasterRenderer(Camera camera) {
        this.camera = camera;
        init();
    }

    private static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    private void init() {
        enableCulling();
        shader = new StaticShader(camera.getProjectionMatrix());
        this.meshes = new HashMap<>();
        this.visibleVoxels = new ArrayList<>();
    }

    /**
     * Prepare before rendering
     */
    private void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 1);
    }

    public void render(Light sun) {
        prepare();
        shader.start();
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        selectGameItem(visibleVoxels, camera);
        EntityRenderer.render(meshes);
        shader.stop();
        clearEntities();
    }

    public void clearEntities() {
        visibleVoxels.clear();
        meshes.clear();
    }

    public void processChunk(Chunk chunk) {
        meshes.putAll(chunk.getChunkMeshes());
        visibleVoxels.addAll(new ArrayList<>(chunk.getVisibleVoxels()));
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getTexturedModel();
        meshes.computeIfAbsent(entityModel, k -> new ArrayList<>());
        meshes.get(entityModel).add(entity);
        if (!(entity instanceof Player) && !(entity instanceof Light))
            visibleVoxels.add(entity);
    }
}
