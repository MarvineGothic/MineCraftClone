package graphics;

import Textures.Material;
import Textures.VoxelType;
import entities.Entity;
import entities.Chunk;
import entities.Voxel;
import models.Mesh;
import models.MeshModel;
import models.SimpleCubeMesh;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Textures.VoxelType.*;
import static entities.Chunk.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static toolbox.Constants.CHUNK_SIZE;
import static toolbox.Side.*;

public class GreedyMeshGenerator {

    private static VoxelType[] mask = new VoxelType[(int) Math.pow(CHUNK_SIZE, 3)];
    private static boolean[] done = new boolean[mask.length];

    private static Voxel voxel;
    private static VoxelType type;
    private static VoxelType textureType;


    private static int x, y, z, w = 0, h = 0, sum, i, j, k, q, face;
    private static boolean done_;


    private static Map<TexturedModel, List<Entity>> chunkMeshes;
    private static Map<VoxelType, List<List<Object>>> meshTypes;

    public static Map<TexturedModel, List<Entity>> processSegment(Chunk seg) {

        chunkMeshes = new HashMap<>();
        meshTypes = new HashMap<>();

        // fill up mask with voxel types
        for (i = 0; i < mask.length; i++) { // i = tileNumber
            voxel = seg.getVoxel(i);

            if (voxel == null || !voxel.isVisible() || voxel.getVoxelType() == EMPTY)
                mask[i] = EMPTY;
            else mask[i] = voxel.getVoxelType();
        }

        // *************************************************************************************

        for (face = 0; face < SIDES; face++) { // i=side    taking one face a time

            // mark voxels as done if they are EMPTY
            for (j = 0; j < mask.length; j++) {
                voxel = seg.getVoxel(j);
                done[j] = mask[j] == EMPTY || !voxel.isVisible(face);
            }


            for (sum = 0; sum < mask.length; sum++) {
                Vector3f coord = indexToCoord(sum);
                x = (int) coord.x;
                y = (int) coord.y;
                z = (int) coord.z;

                if (done[sum]) continue;
                type = mask[sum];

                done_ = false;

                checkSides();
                    addVertices(face, x-0.5f, y-0.5f, z-0.5f);

            }
        }

        for (Map.Entry<VoxelType, List<List<Object>>> entry : meshTypes.entrySet()) {
            VoxelType textureType = entry.getKey();
            List<List<Object>> coords = entry.getValue();
            createMeshEntity(textureType, coords.get(0), coords.get(1), coords.get(2), coords.get(3), seg);
        }
       // seg.setNeedUpdateVisibility(true);
        seg.setNeedUpdateMesh(false);
        return chunkMeshes;
    }

    private static void createMeshEntity(VoxelType textureType, List<Object> vertices, List<Object> indices, List<Object> textures, List<Object> normals, Chunk seg) {
        Material currentTexture = textureType.getTexturedModel().getMaterial();
        Mesh newMesh = new Mesh(vertices, indices, textures, normals);
        MeshModel model = Loader.loadToVAO(newMesh, GL_STATIC_DRAW);
        TexturedModel meshModel = new TexturedModel(model, currentTexture);
        MeshEntity meshEntity = new MeshEntity(textureType, seg.getChunkPos());

        chunkMeshes.computeIfAbsent(meshModel, k -> new ArrayList<>());
        chunkMeshes.get(meshModel).add(meshEntity);
    }

    private static void checkSides() {
        int shift1 = 0;
        int shift2 = 0;
        int a = 0;
        int b = 0;
        if (face == TOP || face == BOT) {
            shift1 = 1;
            a = x;
            b = z;
            shift2 = SHIFT_1;
        } else if (face == FRONT || face == BACK) {
            shift1 = 1;
            a = x;
            b = y;
            shift2 = SHIFT_2;
        } else if (face == RIGHT || face == LEFT) {
            shift1 = SHIFT_1;
            a = z;
            b = y;
            shift2 = SHIFT_2;
        }


        for (w = 1; w < CHUNK_SIZE - a && mask[sum + (w * shift1)] == type && !done[sum + (w * shift1)]; w++) {
        }

        for (h = 0; h < CHUNK_SIZE - b; h++) {
            q = sum + (h * shift2);
            for (k = 0; k < w; k++) {

                if (mask[q + k * shift1] != type || done[q + (k * shift1)]) {
                    done_ = true;
                    break;
                }
            }
            if (done_) {
                break;
            }
        }

        // update voxels for given height
        for (j = 0; j < h; j++) {
            q = sum + (j * shift2);
            for (k = 0; k < w; k++) {
                done[q + (k * shift1)] = true;
            }
        }
    }

    private static void addVertices(int side, float x, float y, float z) {
        List<Object> vertices = new ArrayList<>();
        List<Object> textures = new ArrayList<>();
        List<Object> normals = new ArrayList<>();
        List<Object> indices = new ArrayList<>();

        if (type.equals(GRASS)) {
            if (face == LEFT || face == RIGHT || face == FRONT || face == BACK)
                textureType = GRASS_SIDE;
            else if (face == TOP)
                textureType = GRASS_TOP;
            else if (face == BOT) textureType = DIRT;
        } else if (type.equals(DIRT)) textureType = DIRT;

        // depending on what type we create we get data from map:
        List<List<Object>> listOfCoordinates = new ArrayList<>();
        if (meshTypes.containsKey(textureType)) {
            List<List<Object>> coord = meshTypes.get(textureType);
            vertices = coord.get(0);
            indices = coord.get(1);
            textures = coord.get(2);
            normals = coord.get(3);
        }


        Mesh mesh = type.getTexturedModel().getMeshModel().getMesh();
        float[][] meshTextures = Mesh.splitSides(SimpleCubeMesh.get_textures(), 8);
        float[][] meshNorm = Mesh.splitSides(mesh.getNormals(), 12);
        float[] vert = new float[12];
        float[] tex = new float[12];
        float[] norm = new float[12];
        if (side == TOP) {
            vert = new float[]{
                    x + w, y + 1, z + h,
                    x + w, y + 1, z,
                    x, y + 1, z,
                    x, y + 1, z + h};
            tex = meshTextures[TOP];
            tex[0] *= w;
            tex[1] *= h;
            tex[2] *= w;
            tex[7] *= h;
            norm = meshNorm[TOP];
        } else if (side == BOT) {
            vert = new float[]{
                    x + w, y, z + h,
                    x, y, z + h,
                    x, y, z,
                    x + w, y, z,};
            tex = meshTextures[BOT];
            tex[0] *= w;
            tex[5] *= h;
            tex[6] *= w;
            tex[7] *= h;
            norm = meshNorm[BOT];
        } else if (side == FRONT) {
            vert = new float[]{
                    x + w, y + h, z + 1,
                    x, y + h, z + 1,
                    x, y, z + 1,
                    x + w, y, z + 1};
            tex = meshTextures[FRONT];
            tex[0] *= w;
            tex[5] *= h;
            tex[6] *= w;
            tex[7] *= h;

            norm = meshNorm[FRONT];
        } else if (side == BACK) {
            vert = new float[]{
                    x + w, y + h, z,
                    x + w, y, z,
                    x, y, z,
                    x, y + h, z};
            tex = meshTextures[BACK];
            tex[3] *= h;
            tex[4] *= w;
            tex[5] *= h;
            tex[6] *= w;

            norm = meshNorm[BACK];
        } else if (side == LEFT) {
            vert = new float[]{
                    x, y + h, z + w,
                    x, y + h, z,
                    x, y, z,
                    x, y, z + w};
            tex = meshTextures[LEFT];
            tex[0] *= w;
            tex[5] *= h;
            tex[6] *= w;
            tex[7] *= h;
            norm = meshNorm[LEFT];
        } else if (side == RIGHT) {
            vert = new float[]{
                    x + 1, y + h, z + w,
                    x + 1, y, z + w,
                    x + 1, y, z,
                    x + 1, y + h, z};
            tex = meshTextures[RIGHT];
            tex[3] *= h;
            tex[4] *= w;
            tex[5] *= h;
            tex[6] *= w;
            norm = meshNorm[RIGHT];
        }
        // convert to arrayList
        for (float aVert : vert) vertices.add(aVert);
        for (float aTex : tex) textures.add(aTex);
        for (float aNorm : norm) normals.add(aNorm);

        // add indices:
        indices.add(vertices.size() / 3 - 4);
        indices.add(vertices.size() / 3 - 3);
        indices.add(vertices.size() / 3 - 2);

        indices.add(vertices.size() / 3 - 2);
        indices.add(vertices.size() / 3 - 1);
        indices.add(vertices.size() / 3 - 4);


        // put in map:
        listOfCoordinates.add(0, vertices);
        listOfCoordinates.add(1, indices);
        listOfCoordinates.add(2, textures);
        listOfCoordinates.add(3, normals);

        meshTypes.computeIfAbsent(textureType, k -> new ArrayList<>());
        meshTypes.get(textureType).addAll(listOfCoordinates);
    }
}

