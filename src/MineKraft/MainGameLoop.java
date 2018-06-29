package MineKraft;

import Textures.VoxelType;
import entities.*;
import graphics.fontMeshCreator.FontType;
import graphics.fontMeshCreator.GUIText;
import graphics.fontRendering.TextMaster;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

import java.io.File;
import java.util.*;

import static Textures.VoxelType.*;
import static entities.Chunk.coordToIndex;
import static renderEngine.DisplayManager.getFPS;
import static renderEngine.Loader.loadTexture;
import static toolbox.Constants.CHUNK_SIZE;
import static toolbox.Constants.WORLD_SIZE;

public class MainGameLoop {

    private static MasterRenderer masterRenderer;
    private static Player player;
    private static Camera camera;
    private static Light sun;
    private static List<Chunk> chunks = Collections.synchronizedList(new ArrayList<>());
    private static Map<ChunkPosition, Chunk> chunksMap = Collections.synchronizedMap(new HashMap<>());
    private static List<ChunkPosition> usedPos = Collections.synchronizedList(new ArrayList<>());
    private static List<Thread> threads = new ArrayList<>();
    private static Vector3f camPos = new Vector3f();
    private static boolean close = false;
    private static FontType font;
    private static Voxel sE = null;
    private static GUIText[] text;

    private static Voxel selectedVoxel;


    public static void main(String[] args) {
        init();

        while (!Display.isCloseRequested()) {
            update();
            createHUD();
            render();
            clean();
        }
        close();
    }

    private static void init() {
        DisplayManager.createDisplay();
        TextMaster.init();
        font = new FontType(loadTexture("font"), new File("res/font.fnt"));

        player = new Player(PLAYER, new Vector3f(0, 20, 0), 0, 0, 0, 1);
        camera = new Camera(player);
        sun = (Light) new Light(SUN, new Vector4f(100, 100, 100, 0), new Vector3f(1, 1, 1)).setScale(10);

        masterRenderer = new MasterRenderer(camera);
        createThreads(DIRT);
    }

    private static void update() {
        Display.setTitle("fps:" + getFPS());
        player.move();
        camera.move();
        camPos = camera.getPosition();

        masterRenderer.clearEntities();
        masterRenderer.processEntity(player);
        masterRenderer.processEntity(sun);


        for (int i = 0; i < usedPos.size(); i++) {
            //for (Map.Entry<Vector3f, Chunk> entry : chunksMap.entrySet()) {
            ChunkPosition currentChunkPos = usedPos.get(i);
            Chunk currentChunk = chunksMap.get(currentChunkPos);

            currentChunk.removeVisibleVoxels();

            int distX = (int) (camPos.x - currentChunkPos.getX());
            int distZ = (int) (camPos.z - currentChunkPos.getZ());

            if (distX < 0)
                distX = -distX;

            if (distZ < 0)
                distZ = -distZ;

            if ((distX <= WORLD_SIZE) && (distZ <= WORLD_SIZE))

            masterRenderer.processChunk(currentChunk);

            for (int j = 0; j < currentChunk.getVisibleVoxels().size(); j++) {
                Entity e = currentChunk.getVisibleVoxels().get(j);
                // remove selected
                if (e.isSelected()) {
                    sE = (Voxel) e;
                }
                // masterRenderer.processEntity(e);
            }
        }
    }

    private static void render() {
        masterRenderer.render(sun);
        TextMaster.render();
    }

    private static void createHUD() {
// ***************************** HUD **************************************
        Vector3f camPos = camera.getPosition();
        Vector3f voxelPos = null;
        int voxelIndexInChunk = 0;
        boolean atTop = false;
        VoxelType type = null;
        Chunk currentSegm = null;
        Vector3f segmPos = null;

        if (sE != null) {
            currentSegm = sE.getParentChunk();
            segmPos = currentSegm != null ? currentSegm.getChunkPos() : null;
            if (sE.getPosition() != null && segmPos != null) {
                voxelPos = sE.getPositionInChunk();
                voxelIndexInChunk = coordToIndex((int) voxelPos.x, (int) voxelPos.y, (int) voxelPos.z);
            }
            atTop = sE.isAtTop();
            type = sE.getVoxelType();
        }


        String HUD = String.format("FPS: %s;", getFPS())
                .concat(String.format("Camera position: x=%s y=%s z=%s;", camPos.x, camPos.y, camPos.z))
                .concat(String.format("Chunk: %s;", segmPos))
                .concat(String.format("     LookAt Voxel: %s;", voxelPos))
                .concat(String.format("     Pos in chunkArray: %s;", voxelIndexInChunk))
                .concat(String.format("     At Top: %s;", atTop))
                .concat(String.format("     Type: %s", type));

        String[] hud = HUD.split(";");
        text = new GUIText[hud.length];

        for (int i = 0; i < text.length; i++)
            text[i] = new GUIText(hud[i], 0.5f, font, new Vector2f(0f, i * 0.02f), 1f, false).setColour(1, 0, 0);
    }

    private static void clean() {
        DisplayManager.updateDisplay();
        for (GUIText aText : text) aText.remove();
    }

    private static void close() {
        close = true;
        closeThreads();
        TextMaster.cleanUp();
        Loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static void addEntitiesToList(VoxelType voxelType, int wX, int wY, int wZ) {
        VoxelType currentVoxelType;
        float chunkX = wX * CHUNK_SIZE;
        float chunkY = wY * CHUNK_SIZE;
        float chunkZ = wZ * CHUNK_SIZE;

        ChunkPosition chunkPosition = new ChunkPosition(chunkX, chunkY, chunkZ);
        if (!chunksMap.containsKey(chunkPosition)) {

            Chunk chunk = new Chunk(new Vector3f(chunkPosition.getX(), chunkPosition.getY(), chunkPosition.getZ()));

            for (int x = 0; x < CHUNK_SIZE; x++)
                for (int y = 0; y < CHUNK_SIZE; y++)
                    for (int z = 0; z < CHUNK_SIZE; z++) {
                        Vector3f voxelPositions = new Vector3f(chunkX + x, chunkY + y, chunkZ + z);
                        currentVoxelType = voxelType;
                        chunk.setVoxel(x, y, z, new Voxel(currentVoxelType, voxelPositions, chunk));
                    }
            chunk.setNeedUpdateVisibility(false);
            usedPos.add(chunkPosition);
            chunksMap.put(chunkPosition, chunk);
        }
    }

    private static void createThreads(VoxelType voxelType) {
        Thread thread1 = new Thread(() -> {
            while (!close) {
                for (int x = (int) (camPos.x - WORLD_SIZE) / CHUNK_SIZE; x < (int) (camPos.x + WORLD_SIZE) / CHUNK_SIZE; x++)
                    for (int z = (int) (camPos.z - WORLD_SIZE) / CHUNK_SIZE; z < (int) (camPos.z + WORLD_SIZE) / CHUNK_SIZE; z++)
                        addEntitiesToList(voxelType, x,0, z);
                /*addEntitiesToList(voxelType, 0, 0, 0);
                addEntitiesToList(voxelType, 0, 1, 0);
                addEntitiesToList(voxelType, 0, -1, 0);
                addEntitiesToList(voxelType, -1, 0, 0);
                addEntitiesToList(voxelType, 1, 0, 0);
                addEntitiesToList(voxelType, 0, 0, 1);
                addEntitiesToList(voxelType, 0, 0, -1);*/
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

    public static Camera getCamera() {
        return camera;
    }

    public static FontType getFont() {
        return font;
    }

    public static Map<ChunkPosition, Chunk> getChunksMap() {
        return chunksMap;
    }
}
