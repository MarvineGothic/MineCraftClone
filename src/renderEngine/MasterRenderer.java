package renderEngine;


import entities.*;
import graphics.shaders.StaticShader;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static graphics.CameraBoxSelectionDetector.draw_bbox;
import static graphics.CameraBoxSelectionDetector.selectGameItem;

public class MasterRenderer {

    private static final float RED = 0.5f;
    private static final float GREEN = 0.7f;
    private static final float BLUE = 0.9f;


    private static StaticShader shader;
    private Camera camera;
    private Map<TexturedModel, List<Entity>> entities;
    private Map<TexturedModel, List<Entity>> meshes;
    private Voxel selectedItem;

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
        this.meshes = Collections.synchronizedMap(new HashMap<>());
        this.entities = Collections.synchronizedMap(new HashMap<>());
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
        if (camera.isNeedUpdate())
            selectedItem = selectGameItem(camera);
        EntityRenderer.renderInstanced(meshes);

        //EntityRenderer.render(entities);
        draw_bbox(selectedItem);

        shader.stop();
        clearEntities();
    }

    public void clearEntities() {
        meshes.clear();
        entities.clear();
    }

    public void processChunk(Chunk chunk) {
        // TODO: 01.07.2018 better update
            meshes.putAll(chunk.getChunkMeshes());

    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getTexturedModel();
        entities.computeIfAbsent(entityModel, k -> new ArrayList<>());
        entities.get(entityModel).add(entity);
    }

    public Voxel getSelectedItem() {
        return selectedItem;
    }

    public Map<TexturedModel, List<Entity>> getMeshes() {
        return meshes;
    }

    public Map<TexturedModel, List<Entity>> getEntities() {
        return entities;
    }
}
