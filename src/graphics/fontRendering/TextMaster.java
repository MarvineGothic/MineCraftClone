package graphics.fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphics.fontMeshCreator.FontType;
import graphics.fontMeshCreator.GUIText;
import graphics.fontMeshCreator.TextMeshData;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static renderEngine.Loader.*;

public class TextMaster {

    private static Map<FontType, List<GUIText>> texts = new HashMap<>();
    private static FontRenderer renderer;

    public static void init(){
        renderer = new FontRenderer();
    }

    public static void render(){
        renderer.render(texts);
    }

    public static void loadText(GUIText text){
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = loadToVAO(data.getVertexPositions(), data.getTextureCoords(), GL_STATIC_DRAW);
        text.setMeshInfo(vao, data.getVertexCount());
        List<GUIText> textBatch = texts.computeIfAbsent(font, k -> new ArrayList<>());
        textBatch.add(text);
    }

    public static void removeText(GUIText text){
        List<GUIText> textBatch = texts.get(text.getFont());
        textBatch.remove(text);
        if(textBatch.isEmpty()){
            texts.remove(text.getFont());
        }
    }

    public static void cleanUp(){
        renderer.cleanUp();
    }

}