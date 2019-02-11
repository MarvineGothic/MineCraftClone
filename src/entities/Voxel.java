package entities;

import Textures.VoxelType;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Side;
import toolbox.Vector3;

import static toolbox.Side.SIDES;

public class Voxel extends Entity {

    private boolean insideFrustum;
    private boolean disableFrustumCulling = false;
    private boolean visible;
    private boolean[] sidesVisibility;
    private Vector3f positionInChunk;
    private Chunk parentChunk;
    private boolean isChanged = false;

    public Voxel(VoxelType voxelType, Vector3f position, Chunk parent) {
        super(voxelType, position);
        this.parentChunk = parent;
        visible = true;
        Vector3 parentPos = parent.getChunkPos();
        positionInChunk = Vector3f.sub(position, new Vector3f(parentPos.getX(), parentPos.getY(), parentPos.getZ()), null);
        this.sidesVisibility = new boolean[SIDES];
        for (int i = 0; i < sidesVisibility.length; i++)
            sidesVisibility[i] = true;
    }

    public void updateVisibility() {
        visible = sidesVisibility[0] || sidesVisibility[1] || sidesVisibility[2] || sidesVisibility[3] || sidesVisibility[4] || sidesVisibility[5];
    }

    public void setVisibility(int side, boolean visible) {
        if (sidesVisibility[side] != visible) this.getParentChunk().setNeedUpdateMesh(true);
        sidesVisibility[side] = visible;
    }

    public void setSidesVisibility(boolean[] sidesVisibility) {
        this.sidesVisibility = sidesVisibility;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        //if (this.visible != visible) this.getParentChunk().setNeedUpdateMesh(true);
        this.visible = visible;
    }

    public boolean isVisible(int side) {
        return sidesVisibility[side];
    }

    public boolean isAtTop() {
        return sidesVisibility[Side.TOP];
    }

    public Vector3f getPositionInChunk() {
        return positionInChunk;
    }

    public Chunk getParentChunk() {
        return parentChunk;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public boolean isInsideFrustum() {
        return insideFrustum;
    }

    public void setInsideFrustum(boolean insideFrustum) {
        /*if (this.insideFrustum != insideFrustum) this.getParentChunk().setNeedUpdateMesh(true);*/
        this.insideFrustum = insideFrustum;
        //setVisible(insideFrustum);
    }

    public boolean isDisableFrustumCulling() {
        return disableFrustumCulling;
    }

    public void setDisableFrustumCulling(boolean disableFrustumCulling) {
        this.disableFrustumCulling = disableFrustumCulling;
    }
}
