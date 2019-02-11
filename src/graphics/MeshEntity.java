package graphics;

import Textures.VoxelType;
import entities.Entity;
import toolbox.Vector3;
import org.lwjgl.util.vector.Vector3f;

public class MeshEntity extends Entity {

    public MeshEntity(VoxelType voxelType, Vector3f position) {
        super(voxelType, position);
    }

    public MeshEntity(VoxelType voxelType, Vector3 position) {
        super(voxelType, new Vector3f(position.getX(), position.getY(),position.getZ()));
    }
}
