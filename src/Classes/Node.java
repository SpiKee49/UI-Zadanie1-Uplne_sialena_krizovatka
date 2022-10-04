package Classes;


public class Node {
    private State state;
    private int depth;
    private Node parent;
    private String lastStep = null;

    public Node( State state, int depth, Node parent,String lastStep){
        this.state = state;
        this.depth = depth;
        this.parent = parent;
        this.lastStep = lastStep;
    }

    public State getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public String getLastStep() {
        return lastStep;
    }
}
