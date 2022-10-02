package Classes;

import java.util.ArrayList;

public class Node {
    private State state;
    private int depth;
    private Node parent;

    public Node( State state, int depth, Node parent){
        this.state = state;
        this.depth = depth;
        this.parent = parent;
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
}
