package Classes;


import java.util.Stack;

public class Node {
    private State state;
    private int depth;
    public Node parent;
    public Stack<String> availableMoves;
    @SuppressWarnings (value="unchecked")
    public Node(Node parent, State state, int depth,Stack<String> availableMoves){
        this.parent = parent;
        this.availableMoves = (Stack<String>) availableMoves.clone();
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
