import entities.Voxel;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Voxel> visibleVoxels=new ArrayList<>();
        visibleVoxels.add(new Voxel(null, new Vector3f(0,0,1), null));
        System.out.println(visibleVoxels.contains(new Voxel(null, new Vector3f(0,0,1), null)));
    }
}
