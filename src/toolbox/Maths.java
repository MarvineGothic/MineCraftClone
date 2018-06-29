package toolbox;

import entities.Camera;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class Maths {

    public static Matrix4f createProjectionMatrix(float fov, float near, float far) {
        Matrix4f projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        perspective(projectionMatrix, fov, aspectRatio, near, far);
        return projectionMatrix;
    }

    public static void perspective(Matrix4f out, float fov, float aspectRatio, float near, float far) {
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = far - near;

        out.m00 = x_scale;
        out.m11 = y_scale;
        out.m22 = -((far + near) / frustum_length);
        out.m23 = -1;
        out.m32 = -((2 * near * far) / frustum_length);
        out.m33 = 0;
    }

    public static void orthographic(Matrix4f out, float left, float right, float bottom, float top, float near, float far) {
        float lr = 1f / (left - right), bt = 1f / (bottom - top), nf = 1f / (near - far);

        out.m00 = -2 * lr;
        out.m11 = -2 * bt;
        out.m22 = 2 * nf;
        out.m23 = 0;

        out.m30 = (left + right) * lr;
        out.m31 = (top + bottom) * bt;
        out.m32 = (far + near) * nf;
        out.m33 = 1;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    public static Vector3f normalize(Vector3f dest) {
        float length = (float) Math.sqrt(dest.x * dest.x + dest.y * dest.y + dest.z * dest.z);
        float invLength = 1.0f / length;
        dest.x = dest.x * invLength;
        dest.y = dest.y * invLength;
        dest.z = dest.z * invLength;
        return dest;
    }

    public static org.joml.Vector3f toJOMLV3f(Vector3f vector3f) {
        return new org.joml.Vector3f(vector3f.x, vector3f.y, vector3f.z);
    }

    public static org.joml.Matrix4f toJOMLM4f(Matrix4f matrix4f) {
        return new org.joml.Matrix4f(matrix4f.m00, matrix4f.m01, matrix4f.m02, matrix4f.m03, matrix4f.m10, matrix4f.m11, matrix4f.m12, matrix4f.m13, matrix4f.m20, matrix4f.m21, matrix4f.m22, matrix4f.m23, matrix4f.m30, matrix4f.m31, matrix4f.m32, matrix4f.m33);
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static double log2(float x) {
        return (Math.log(x) / Math.log(2));
    }
}
