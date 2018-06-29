package models;


public class Model {
    private int vaoID;
    private Mesh mesh;
    private int size; //size
    private int usage;

    public Model(int vaoID, Mesh mesh, int size, int usage) {
        this.vaoID = vaoID;
        this.mesh = mesh;
        this.size = size;
        this.usage = usage;
    }

    public Model(int vaoID, int size, int usage) {
        this.vaoID = vaoID;
        this.size = size;
        this.usage = usage;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Mesh getMesh() {
        return mesh;
    }
    }
