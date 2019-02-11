package graphics;

import entities.Camera;
import entities.Chunk;
import entities.Voxel;
import org.joml.FrustumIntersection;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import static toolbox.Constants.CHUNK_SIZE;


public class FrustumCullingFilter {

    private static final org.joml.Matrix4f prjViewMatrix;

    private static FrustumIntersection frustumInt;

    static {
        prjViewMatrix = new org.joml.Matrix4f();
        frustumInt = new FrustumIntersection();
    }

    public static void update(Camera camera) {
        // Calculate projection view matrix
        prjViewMatrix.set(Maths.toJOMLM4f(camera.getProjectionMatrix()));
        prjViewMatrix.mul(Maths.toJOMLM4f(camera.getViewMatrix()));
        // Update frustum intersection class
        frustumInt.set(prjViewMatrix);
    }

    public static void filter(Chunk chunk) {
        Vector3f chunkPos = chunk.getChunkPos().toVector3f();
        Vector3f.add(chunkPos, new Vector3f((CHUNK_SIZE - 1) / 2f, (CHUNK_SIZE - 1) / 2f, (CHUNK_SIZE - 1) / 2f), chunkPos);
        if (insideFrustum(chunkPos, 10f)) {
            chunk.setInsideFrustum(true);
            //filter(chunk.getVoxels(), 0.75f);
        } else chunk.setInsideFrustum(false);
    }

    public static void filter(Voxel[] voxels, float meshBoundingRadius) {
        float boundingRadius;
        Vector3f pos;
        for (Voxel voxel : voxels) {
            if ( !voxel.isDisableFrustumCulling()) {
                boundingRadius = voxel.getScale() * meshBoundingRadius;
                pos = voxel.getPosition();
                voxel.setInsideFrustum(insideFrustum(pos.x + 0.5f, pos.y + 0.5f, pos.z + 0.5f, boundingRadius));
            }
        }
    }

    public static boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
        return frustumInt.testSphere(x0, y0, z0, boundingRadius);
    }

    private static boolean insideFrustum(Vector3f v, float boundingRadius) {
        return insideFrustum(v.x, v.y, v.z, boundingRadius);
    }
}

/*
public class FrustumCullingFilter {

    private static final int NUM_PLANES = 6;

    private static final Matrix4f prjViewMatrix;

    private static final Vector4f[] frustumPlanes;

    static {
        prjViewMatrix = new Matrix4f();
        frustumPlanes = new Vector4f[NUM_PLANES];
        for (int i = 0; i < NUM_PLANES; i++) {
            frustumPlanes[i] = new Vector4f();
        }
    }

    public static void update(Camera camera) {
        // Calculate projection view matrix
        Matrix4f.load(camera.getProjectionMatrix(), prjViewMatrix);
        Matrix4f.mul(prjViewMatrix, camera.getViewMatrix(), prjViewMatrix);
        org.joml.Matrix4f jomlMatrix = Maths.toJOMLM4f(prjViewMatrix);
        // Get frustum planes
        for (int i = 0; i < NUM_PLANES; i++) {
            jomlMatrix.frustumPlane(i, frustumPlanes[i]);
        }
    }

    public static void filter(Chunk chunk) {
        Vector3f chunkPos = chunk.getChunkPos().toVector3f();
        if (insideFrustum(chunkPos, 16f)) {
            chunk.setInsideFrustum(true);
            filter(new ArrayList<>(chunk.getVisibleVoxels()), 2f);
        } else chunk.setInsideFrustum(false);
    }

    private static void filter(List<Voxel> voxels, float meshBoundingRadius) {
        float boundingRadius;
        Vector3f pos;
        for (Voxel voxel : voxels) {
            boundingRadius = voxel.getScale() * meshBoundingRadius;
            pos = voxel.getPosition();
            voxel.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
        }
    }

    private static boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
        for (int i = 0; i < NUM_PLANES; i++) {
            Vector4f plane = frustumPlanes[i];
            if (plane.x * x0 + plane.y * y0 + plane.z * z0 + plane.w <= -boundingRadius) {
                return false;
            }
        }
        return true;
    }

    private static boolean insideFrustum(Vector3f v, float boundingRadius) {
        return insideFrustum(v.x, v.y, v.z, boundingRadius);
    }
}*/
