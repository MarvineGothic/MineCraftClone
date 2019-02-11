package renderEngine;

import Textures.Material;
import entities.Entity;
import entities.Voxel;
import models.MeshModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

import static graphics.shaders.StaticShader.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static toolbox.Side.SIDES;


public class EntityRenderer {

    static void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel texturedModel : entities.keySet()) {
            bindTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);

            for (Entity entity : batch) {
                loadSelectedEntity(entity.isSelected());
                updateTransformationMatrix(entity);

                if (entity instanceof Voxel)
                    for (int i = 0; i < SIDES; i++) {
                        if (((Voxel) entity).isVisible(i))
                            GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, i * 24);
                    }
                else
                    GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getMeshModel().getSize(), GL11.GL_UNSIGNED_INT, 0);

            }
            unbindTexturedModel();
        }
    }

    // TODO: 01.07.2018 make it
    static void renderInstanced(Map<TexturedModel, List<Entity>> entities) {

        for (TexturedModel texturedModel : entities.keySet()) {
            bindTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);

            for (Entity entity : batch) {
                loadSelectedEntity(entity.isSelected());
                updateTransformationMatrix(entity);
            }
            glDrawElementsInstanced(GL11.GL_TRIANGLES, texturedModel.getMeshModel().getSize(), GL_UNSIGNED_INT, 0, batch.size());
            unbindTexturedModel();
        }
    }

    private static void bindTexturedModel(TexturedModel model) {
        MeshModel rawMeshModel = model.getMeshModel();
        glBindVertexArray(rawMeshModel.getVaoID());
        glEnableVertexAttribArray(VERTICES);
        glEnableVertexAttribArray(TEXTURES);
        glEnableVertexAttribArray(NORMALS);
        Material material = model.getMaterial();
        //shader.loadNumberOfRows(material.getNumberOfRows());
        if (material.isTransparent())
            MasterRenderer.disableCulling();
        loadFakeLightingVariable(material.isFakeLighting());
        loadShineVariables(material.getShineDamper(), material.getReflectance());
        // bind material
        // TODO: 28.06.2018 remove bottleneck by using texture arrays (texture atlas)
        material.bind();
    }

    private static void updateTransformationMatrix(Entity entity) {
        entity.updateTransformationMatrix();
        loadTransformationMatrix(entity.getTransformationMatrix());
    }

    private static void unbindTexturedModel() {
        // MasterRenderer.enableCulling();
        glDisableVertexAttribArray(VERTICES);
        glDisableVertexAttribArray(TEXTURES);
        glDisableVertexAttribArray(NORMALS);
        glBindVertexArray(0);
    }
}