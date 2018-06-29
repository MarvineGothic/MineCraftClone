package graphics;

import Textures.VoxelType;
import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

public class MeshEntity extends Entity {

    public MeshEntity(VoxelType voxelType, Vector3f position) {
        super(voxelType, position);
    }
}
