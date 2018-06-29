package entities;

import MineKraft.MainGameLoop;
import graphics.MeshEntityGenerator;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static Textures.VoxelType.DIRT;
import static Textures.VoxelType.GRASS;
import static toolbox.Constants.CHUNK_SIZE;
import static toolbox.KeyHandler.keySinglePress;
import static toolbox.Side.*;

public class Chunk {
    public static final int SHIFT_1 = CHUNK_SIZE;
    public static final int SHIFT_2 = CHUNK_SIZE * CHUNK_SIZE;
    private Voxel[] voxels = new Voxel[(int) Math.pow(CHUNK_SIZE, 3)];
    private List<Voxel> visibleVoxels;
    private Vector3f chunkPos;
    private boolean needUpdateMesh = true;
    private boolean needUpdateVisibility = true;
    private Map<TexturedModel, List<Entity>> chunkMeshes;

    public Chunk(Vector3f chunkPos) {
        this.chunkPos = chunkPos;
        visibleVoxels = Collections.synchronizedList(new ArrayList<>());
    }

    public static int coordToIndex(int x, int y, int z) {
        return x + z * SHIFT_1 + y * SHIFT_2;
    }

    public static Vector3f indexToCoord(int i) {
        return new Vector3f(i % SHIFT_1, (float) Math.floor(i / SHIFT_2), (float) Math.floor(i / SHIFT_1) % SHIFT_1);
    }

    private void updateVoxelVisibility(int x, int y, int z) {
        Voxel currentVoxel = getVoxel(x, y, z);
        //ChunkPosition currChunkPos = new ChunkPosition(chunkPos.x, chunkPos.y, chunkPos.z);

        Voxel surroundingVoxels[] = new Voxel[SIDES];

        surroundingVoxels[TOP] = getVoxel(x, y + 1, z); // top
        surroundingVoxels[BOT] = getVoxel(x, y - 1, z); // bot
        surroundingVoxels[FRONT] = getVoxel(x, y, z + 1); // front
        surroundingVoxels[BACK] = getVoxel(x, y, z - 1); // back
        surroundingVoxels[LEFT] = getVoxel(x - 1, y, z); // left
        surroundingVoxels[RIGHT] = getVoxel(x + 1, y, z); // right


        Chunk[] surroundingChunks = new Chunk[SIDES];
        Map<ChunkPosition, Chunk> chunksMap = MainGameLoop.getChunksMap();
        surroundingChunks[TOP] = chunksMap.get(new ChunkPosition(chunkPos.x, chunkPos.y + CHUNK_SIZE, chunkPos.z));
        surroundingChunks[BOT] = chunksMap.get(new ChunkPosition(chunkPos.x, chunkPos.y - CHUNK_SIZE, chunkPos.z));
        surroundingChunks[FRONT] = chunksMap.get(new ChunkPosition(chunkPos.x, chunkPos.y, chunkPos.z + CHUNK_SIZE));
        surroundingChunks[BACK] = chunksMap.get(new ChunkPosition(chunkPos.x, chunkPos.y, chunkPos.z - CHUNK_SIZE));
        surroundingChunks[LEFT] = chunksMap.get(new ChunkPosition(chunkPos.x - CHUNK_SIZE, chunkPos.y, chunkPos.z));
        surroundingChunks[RIGHT] = chunksMap.get(new ChunkPosition(chunkPos.x + CHUNK_SIZE, chunkPos.y, chunkPos.z));


        // TODO: 29.06.2018  gives improvement from 20 to 40 FPS rendering world 5 * 16*16
        if (y == CHUNK_SIZE - 1 && surroundingChunks[TOP] != null)
            surroundingVoxels[TOP] = surroundingChunks[TOP].getVoxel(x, 0, z);
        if (y == 0 && surroundingChunks[BOT] != null)
            surroundingVoxels[BOT] = surroundingChunks[BOT].getVoxel(x, CHUNK_SIZE - 1, z);
        if (z == CHUNK_SIZE - 1 && surroundingChunks[FRONT] != null)
            surroundingVoxels[FRONT] = surroundingChunks[FRONT].getVoxel(x, y, 0);
        if (z == 0 && surroundingChunks[BACK] != null)
            surroundingVoxels[BACK] = surroundingChunks[BACK].getVoxel(x, y, CHUNK_SIZE - 1);
        if (x == CHUNK_SIZE - 1 && surroundingChunks[RIGHT] != null)
            surroundingVoxels[RIGHT] = surroundingChunks[RIGHT].getVoxel(0, y, z);
        if (x == 0 && surroundingChunks[LEFT] != null)
            surroundingVoxels[LEFT] = surroundingChunks[LEFT].getVoxel(CHUNK_SIZE - 1, y, z);


        // sides visibility:
        int a = 1;
        for (int i = 0; i < SIDES; i++) {
            if (surroundingVoxels[i] == null && currentVoxel != null)
                currentVoxel.setVisibility(i, true);
            else if (surroundingVoxels[i] != null) {

                surroundingVoxels[i].setVisibility(i + a, false);
                if (currentVoxel == null) {
                    surroundingVoxels[i].setVisibility(i + a, true);
                } else
                    currentVoxel.setVisibility(i, false);
            }
            a = 0 - a;
        }

        // update current voxel and surrounding for visibility
        updateVoxel(currentVoxel);
        for (Voxel surroundingVoxel : surroundingVoxels)
            updateVoxel(surroundingVoxel);


        // if voxel was updated then recreate Mesh:
        this.setNeedUpdateMesh(true);
    }

    private void updateVoxel(Voxel voxel) {
        if (voxel != null) {
            voxel.updateVisibility();

            // update voxel textures GRASS and DIRT
            if (voxel.getVoxelType().equals(DIRT) && (voxel).isAtTop()) {
                voxel.setModel(GRASS);
                voxel.setModel(GRASS);
            }
            if (voxel.getVoxelType().equals(GRASS) && !(voxel).isAtTop()) {
                voxel.setModel(DIRT);
                voxel.setModel(DIRT);
            }

            // update visible Voxels list:
            if (!voxel.isVisible()) visibleVoxels.remove(voxel);
            else if (voxel.isVisible() && !visibleVoxels.contains(voxel))
                visibleVoxels.add(voxel);
        }
    }

    public Voxel getVoxel(int i) {
        return voxels[i];
    }

    public Voxel getVoxel(Vector3f v) {
        return getVoxel((int) v.x, (int) v.y, (int) v.z);
    }

    public Voxel getVoxel(int x, int y, int z) {
        if (indexOutOfBounds(x) || indexOutOfBounds(y) || indexOutOfBounds(z)) return null;
        return voxels[coordToIndex(x, y, z)];
    }

    public void setVoxel(int i, Voxel voxel) {
        voxels[i] = voxel;
    }

    public void setVoxel(int x, int y, int z, Voxel voxel) {
        if (indexOutOfBounds(x) || indexOutOfBounds(y) || indexOutOfBounds(z)) return;
        // when delete voxel, remove from visible list:
        if (voxel == null)
            visibleVoxels.remove(voxels[coordToIndex(x, y, z)]);
        voxels[coordToIndex(x, y, z)] = voxel;
        updateVoxelVisibility(x, y, z);
    }

    public void setVoxel(Vector3f p, Voxel voxel) {
        setVoxel((int) p.x, (int) p.y, (int) p.z, voxel);
    }

    public void removeVisibleVoxels() {
        for (Voxel voxel : visibleVoxels)
            if (voxel.isSelected() && keySinglePress(Keyboard.KEY_R)) {
                //if (voxel.isSelected() && Keyboard.isKeyDown(Keyboard.KEY_R)) {
                this.setVoxel(voxel.getPositionInChunk(), null);    // also updates visibility
                return;
            }
    }

    private boolean indexOutOfBounds(int i) {
        return (i < 0 || i > CHUNK_SIZE - 1);
    }

    public Vector3f getChunkPos() {
        return chunkPos;
    }

    public List<Voxel> getVisibleVoxels() {
        return visibleVoxels;
    }

    public boolean isNeedUpdateMesh() {
        return needUpdateMesh;
    }

    public void setNeedUpdateMesh(boolean needUpdateMesh) {
        this.needUpdateMesh = needUpdateMesh;
    }

    public Map<TexturedModel, List<Entity>> getChunkMeshes() {
        this.setChunkMeshes();
        return chunkMeshes;
    }

    public void setChunkMeshes() {
        // TODO: 29.06.2018 gives improvement from 5 to 20 FPS rendering world 5 * 16*16
        if (this.isNeedUpdateMesh())
            this.chunkMeshes = MeshEntityGenerator.processSegment(this);
    }

    public boolean isNeedUpdateVisibility() {
        return needUpdateVisibility;
    }

    public void setNeedUpdateVisibility(boolean needUpdateVisibility) {
        this.needUpdateVisibility = needUpdateVisibility;
    }

}
