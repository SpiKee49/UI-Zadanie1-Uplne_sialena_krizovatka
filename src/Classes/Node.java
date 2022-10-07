package Classes;


import java.util.ArrayList;
import java.util.Stack;

public class Node {
    private State state;
    private int depth;
    public Node parent;
    public Node(Node parent, State state, int depth){
        this.parent = parent;
        this.state = state;
        this.depth = depth;
    }

    public State getState() {
        return state;
    }

    public int getDepth() {
        return depth;
    }

}
