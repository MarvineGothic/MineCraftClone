package MineKraft;

import Models.RawModel;
import Models.SimpleCubeModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import Textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainGameLoop {
    private static final int WORLD_SIZE = 5 * 16;
    private static List<Chunk> chunks = Collections.synchronizedList(new ArrayList<>());
    private static Vector3f camPos = new Vector3f(0, 0, 0);
    private static List<Vector3f> usedPos = Collections.synchronizedList(new ArrayList<>());
    private static List<Thread> threads = new ArrayList<>();
    private static boolean close = false;


    public static void main(String[] args) {
        DisplayManager.createDisplay();
        //Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer();
        RawModel model = Loader.loadToVAO(SimpleCubeModel.getVertices(), SimpleCubeModel.getIndices(),
                SimpleCubeModel.setTextures(3, 3, 1, 0), SimpleCubeModel.getNormals());

        ModelTexture treeBark = new ModelTexture(Loader.loadTexture("TreeBark"));
        ModelTexture grassTex = new ModelTexture(Loader.loadTexture("grassTex"));
        TexturedModel modelTreeBark = new TexturedModel(model, treeBark);
        TexturedModel modelGrassTex = new TexturedModel(model, grassTex);

        Player player = new Player(modelGrassTex, new Vector3f(0, 1, 0), 0, 0, 0, 1);
        Camera camera = new Camera(player);

        Light sun = new Light(new Vector3f(10, 100, 0), new Vector3f(1, 1, 1));

        createThreads(modelTreeBark);

        while (!Display.isCloseRequested()) {
            player.move();
            camera.move();
            camPos = camera.getPosition();

            renderer.processEntity(player);
            renderer.processEntity(sun);

            for (int i = 0; i < chunks.size(); i++) {
                Vector3f origin = chunks.get(i).getOrigin();
                Vector3f lookAtVertex = camera.findLookAtVertexCoordinates();

                int distX = (int) (camPos.x - origin.x);
                int distZ = (int) (camPos.z - origin.z);

                if (distX < 0)
                    distX = -distX;

                if (distZ < 0)
                    distZ = -distZ;

                if ((distX <= WORLD_SIZE) && (distZ <= WORLD_SIZE))
                    for (int j = 0; j < chunks.get(i).getBlocks().size(); j++) {
                        Entity e = chunks.get(i).getBlocks().get(j);
                        if (e.equals(new Entity(lookAtVertex)))
                            e.setModel(modelGrassTex);
                        renderer.processEntity(e);
                    }
            }
            /*entity.increasedRotation(0.3f,0.5f,0.5f);
            renderer.processEntity(entity);*/
            renderer.render(sun, camera);
            DisplayManager.updateDisplay();
        }
        close = true;
        closeThreads();
        Loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static void addEntitiesToList(TexturedModel texturedModel, int x, int z) {
        Vector3f vector = new Vector3f(x * 16, 0 * 16, z * 16);
        if (!usedPos.contains(vector)) {
            List<Entity> blocks = new ArrayList<>();

            for (int i = 0; i < 16; i++)
                for (int j = 0; j < 16; j++) {
                    Vector3f v1 = new Vector3f((x * 16) + i, 0, (z * 16) + j);
                    blocks.add(new Entity(texturedModel, v1, 0, 0, 0, 1));
                }

            chunks.add(new Chunk(blocks, vector));
            usedPos.add(vector);
        }
    }

    private static void createThreads(TexturedModel texturedModel) {
        Thread thread1 = new Thread(() -> {
            while (!close) {
                for (int x = (int) (camPos.x - WORLD_SIZE) / 16; x < (int) (camPos.x + WORLD_SIZE) / 16; x++)
                    for (int z = (int) (camPos.z - WORLD_SIZE) / 16; z < (int) (camPos.z + WORLD_SIZE) / 16; z++)
                        addEntitiesToList(texturedModel, x, z);
            }
        });
        thread1.start();
        threads.add(thread1);
    }

    private static void closeThreads() {
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Couldn't safe stop thread");
                e.printStackTrace();
            }
    }

}
