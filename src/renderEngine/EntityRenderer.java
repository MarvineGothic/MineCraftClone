package renderEngine;

import Textures.TextureObject;
import entities.Entity;
import models.Model;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static graphics.shaders.StaticShader.*;


public class EntityRenderer {

    static void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            bindTexturedModel(model);
            List<Entity> batch = entities.get(model);

            for (Entity entity : batch) {
                loadSelectedEntity(entity.isSelected());
                updateTransformationMatrix(entity);

                /*if (entity instanceof Voxel)
                    for (int i = 0; i < SIDES; i++) {
                        if (((Voxel) entity).isVisible(i))
                            GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, i * 24);
                    }
                else*/
                //if (!(entity instanceof MeshEntity))
                    GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getSize(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private static void bindTexturedModel(TexturedModel model) {
        Model rawModel = model.getModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(VERTICES);
        glEnableVertexAttribArray(TEXTURES);
        glEnableVertexAttribArray(NORMALS);
        TextureObject textureObject = model.getTextureObject();
        //shader.loadNumberOfRows(textureObject.getNumberOfRows());
        if (textureObject.isTransparent())
            MasterRenderer.disableCulling();
        loadFakeLightingVariable(textureObject.isFakeLighting());
        //  shader.loadShineVariables(textureObject.getShineDamper(), textureObject.getReflectivity());
        // bind textureObject
        // TODO: 28.06.2018 remove bottleneck by using texture arrays (texture atlas)
        textureObject.bind();
    }

    private static void updateTransformationMatrix(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        loadTransformationMatrix(transformationMatrix);
    }

    private static void unbindTexturedModel() {
        // MasterRenderer.enableCulling();
        glDisableVertexAttribArray(VERTICES);
        glDisableVertexAttribArray(TEXTURES);
        glDisableVertexAttribArray(NORMALS);
        glBindVertexArray(0);
    }
}