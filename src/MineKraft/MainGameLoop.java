package MineKraft;

import Textures.VoxelType;
import entities.*;
import graphics.FrustumCullingFilter;
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
import toolbox.Timer;
import toolbox.Vector3;

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
    // Entities
    private static Player player;
    private static Camera camera;
    private static Light sun;
    private static Voxel selectedVoxel = null;

    // Collections
    private static Map<Vector3, Chunk> chunksMap = Collections.synchronizedMap(new HashMap<>());
    private static List<Vector3> usedPos = Collections.synchronizedList(new ArrayList<>());
    private static List<Thread> threads = Collections.synchronizedList(new ArrayList<>());
    private static Vector3f camPos = new Vector3f();

    // HUD
    private static FontType font;
    private static GUIText[] text;

    // Performance meter
    private static Timer timer = new Timer();
    private static Timer timer2 = new Timer();
    private static float max = 0;
    private static float update = 0;
    private static float hud = 0;
    private static float render = 0;
    private static float clean = 0;
    private static float updStart = 0;
    private static float frustum = 0;
    private static float process = 0;

    private static boolean close = false;

    public static void main(String[] args) {
        init();

        while (!Display.isCloseRequested()) {
            timer.init();
            update();
            update += timer.getElapsedTime();
            createHUD();
            hud += timer.getElapsedTime();
            render();
            render += timer.getElapsedTime();
            clean();
            clean += timer.getElapsedTime();
        }
        if (render > clean && render > hud && render > update) System.out.println("render");
        if (update > clean && update > hud && update > render) System.out.println("update");
        if (hud > clean && hud > render && hud > update) System.out.println("hud");
        if (clean > render && clean > hud && clean > update) System.out.println("clean");
        close();
    }

    private static void init() {
        DisplayManager.createDisplay();
        TextMaster.init();
        font = new FontType(loadTexture("font"), new File("res/font.fnt"));

        player = new Player(PLAYER, new Vector3f(0, 20, 0), 0, 0, 0, 1);
        camera = new Camera(player);
        sun = (Light) new Light(SUN, new Vector4f(1000, 1000, 1000, 1), new Vector3f(1, 1, 1)).setScale(10);

        masterRenderer = new MasterRenderer(camera);

        createThreads(DIRT);
    }

    private static void update() {
        timer2.init(); // performance measurement
        Display.setTitle("fps:" + getFPS());

        player.update();
        camera.update();

        camPos = camera.getPosition();

        FrustumCullingFilter.update(camera);

        masterRenderer.clearEntities();
        masterRenderer.processEntity(player);
        masterRenderer.processEntity(sun);

        updStart += timer2.getElapsedTime();

        for (int i = 0; i < usedPos.size(); i++) {
            timer2.init();
            Vector3 currentChunkPos = usedPos.get(i);
            Chunk currentChunk = chunksMap.get(currentChunkPos);

            // frustum culling bottleneck. Needs improvement
            if (camera.isNeedUpdate())
                FrustumCullingFilter.filter(currentChunk);


            if (currentChunk.isInsideFrustum()) {
                int distX = (int) (camPos.x - currentChunkPos.getX());
                int distZ = (int) (camPos.z - currentChunkPos.getZ());

                if (distX < 0)
                    distX = -distX;

                if (distZ < 0)
                    distZ = -distZ;

                frustum += timer2.getElapsedTime();
                if ((distX <= WORLD_SIZE) && (distZ <= WORLD_SIZE))
                    masterRenderer.processChunk(currentChunk);

                /*Set<Voxel> chunkVisibleVoxels = currentChunk.getVisibleVoxels();
                for (int j = 0; j < chunkVisibleVoxels.size(); j++)
                    masterRenderer.processEntity(new ArrayList<>(chunkVisibleVoxels).get(j));*/
                process += timer2.getElapsedTime();
                //System.out.println(masterRenderer.getEntities().size());

            }
        }

    }

    private static void render() {
        selectedVoxel = masterRenderer.getSelectedItem();
        // TODO: 01.07.2018 increased FPS from 12 to 60 on world 10 * 16*16 (can run 20 * 16*16 FPS 12-30)
        // remove selected voxel from chunk:
        Chunk selectedChunk;
        if (selectedVoxel != null) {
            selectedChunk = chunksMap.get(selectedVoxel.getParentChunk().getChunkPos());
            selectedChunk.removeSelectedVoxel(selectedVoxel);
        }

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
        Vector3 segmPos = null;

        if (selectedVoxel != null) {
            currentSegm = selectedVoxel.getParentChunk();
            segmPos = currentSegm != null ? currentSegm.getChunkPos() : null;
            if (selectedVoxel.getPosition() != null && segmPos != null) {
                voxelPos = selectedVoxel.getPositionInChunk();
                voxelIndexInChunk = coordToIndex((int) voxelPos.x, (int) voxelPos.y, (int) voxelPos.z);
            }
            atTop = selectedVoxel.isAtTop();
            type = selectedVoxel.getVoxelType();
        }


        String HUD = String.format("FPS: %s;", getFPS())
                .concat(String.format("Camera position: x=%s y=%s z=%s;", camPos.x, camPos.y, camPos.z))
                .concat(String.format("Chunk: %s;", segmPos))
                .concat(String.format("     LookAt Voxel: %s;", voxelPos))
                .concat(String.format("     Pos in chunkArray: %s;", voxelIndexInChunk))
                .concat(String.format("     At Top: %s;", atTop))
                .concat(String.format("     Type: %s;", type)
                        .concat(String.format("Performance:;    Update: %s;", update))
                        .concat(String.format("     Hud: %s;", hud))
                        .concat(String.format("     render: %s;", render))
                        .concat(String.format("     Clean: %s;", clean))
                        .concat(String.format("     updStart: %s;", updStart))
                        .concat(String.format("     frustum: %s;", frustum))
                        .concat(String.format("     process: %s;", process))
                        .concat(String.format("HUD: %s;", hud))
                        .concat(String.format("Render: %s;", render))
                        .concat(String.format("Clean: %s;", clean))
                );

        String[] hud = HUD.split(";");
        text = new GUIText[hud.length];

        for (int i = 0; i < text.length; i++)
            text[i] = new GUIText(hud[i], 0.5f, font, new Vector2f(0f, i * 0.02f), 1f, false).setColour(1, 0, 0);
    }

    private static void clean() {
        DisplayManager.updateDisplay();
        // clean HUD:
        GUIText.clear(text);
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

        Vector3 vector3 = new Vector3(chunkX, chunkY, chunkZ);
        if (!chunksMap.containsKey(vector3)) {

            Chunk chunk = new Chunk(new Vector3(vector3.getX(), vector3.getY(), vector3.getZ()));

            for (int x = 0; x < CHUNK_SIZE; x++)
                for (int y = 0; y < CHUNK_SIZE; y++)
                    for (int z = 0; z < CHUNK_SIZE; z++) {
                        Vector3f voxelPositions = new Vector3f(chunkX + x, chunkY + y, chunkZ + z);
                        currentVoxelType = voxelType;
                        chunk.setVoxel(x, y, z, new Voxel(currentVoxelType, voxelPositions, chunk));
                    }

            usedPos.add(vector3);
            chunksMap.put(vector3, chunk);
        }
    }

    private static void createThreads(VoxelType voxelType) {
        Thread thread1 = new Thread(() -> {
            while (!close) {
                for (int x = (int) (camPos.x - WORLD_SIZE) / CHUNK_SIZE; x < (int) (camPos.x + WORLD_SIZE) / CHUNK_SIZE; x++)
                    for (int z = (int) (camPos.z - WORLD_SIZE) / CHUNK_SIZE; z < (int) (camPos.z + WORLD_SIZE) / CHUNK_SIZE; z++)
                        addEntitiesToList(voxelType, x, 0, z);
                //addEntitiesToList(voxelType, 0, 0, 0);
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

    public static Map<Vector3, Chunk> getChunksMap() {
        return chunksMap;
    }

    public static MasterRenderer getMasterRenderer() {
        return masterRenderer;
    }

    public static List<Vector3> getUsedPos() {
        return usedPos;
    }
}
