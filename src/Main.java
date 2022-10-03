import Classes.Car;
import Classes.Node;
import Classes.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Main {
    private static final HashMap<State, State> hash = new HashMap<>();
    private static final ArrayList<Node> currentNodes = new ArrayList<>();

    public static void main(String[] args) {
        int depth = 0;
        ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car("red",2, 3, 2, false));
        cars.add(new Car("orange",2, 1, 1, false));
        cars.add(new Car("yellow",3, 2, 1, true));
        cars.add(new Car("purple",2, 5, 1, true));
        cars.add(new Car("green",3, 2, 4, true));
        cars.add(new Car("blue",3, 6, 3, false));
        cars.add(new Car("gray",2, 5, 5, false));
        cars.add(new Car("dark-blue",3, 1, 6, true));
        State entry = new State(cars);
        Node root = new Node(entry,depth,null, null);
        currentNodes.add(root);
        Node solution = null ;

        do{
            int finalDepth = depth;
            currentNodes.removeIf(item -> item.getDepth() != finalDepth);
            int curNumOfNodes = currentNodes.size();
            if (currentNodes.isEmpty()){
                System.out.println("Problem does not have a solution. Reached depth: " + finalDepth);
                break;
            }
            for (Node item: currentNodes) {
                Car findCar = item.getState().findCarByColor(item.getState().getMap()[2][5]);
                if (findCar == null) return;;
                if (!findCar.isVertical()){
                    solution = item;
                    break;
                }
            }

            for (int i = 0; i < curNumOfNodes ; i++){
                System.out.println(currentNodes.get(i).getParent());
                System.out.println(currentNodes.get(i) +" "+ currentNodes.get(i).getLastStep());
                currentNodes.get(i).getState().printMap();
                getNextState(currentNodes.get(i), finalDepth + 1);
            }
            depth = depth +1;
        }while (solution == null);

        if (solution != null){
            printPath(solution);
        }


    }
    static void printPath(Node solution){
        System.out.println("im here");
        if (solution.getParent() == null) return;
        System.out.println(solution.getDepth());
        printPath(solution.getParent());

    }


    static void getNextState(Node knot, int depth){
        State currentState = knot.getState();
        ArrayList<Car> cars = currentState.getPosition();

        cars.forEach((item)->{
            int itemIndex = cars.indexOf(item);
//            System.out.println(currentState);
//            currentState.printMap();
            if (item.isVertical()){
                State upState = moveCar("UP", currentState, item, itemIndex);
                State downState = moveCar("DOWN", currentState, item, itemIndex);

                createNewNodeFromState(upState, knot, depth, item,"UP");
                createNewNodeFromState(downState, knot, depth, item, "DOWN");
            }else {
                State leftState = moveCar("LEFT", currentState, item, itemIndex);
                State rightState = moveCar("RIGHT", currentState, item, itemIndex);

                createNewNodeFromState(leftState, knot, depth, item, "LEFT");
                createNewNodeFromState(rightState, knot, depth, item, "RIGHT");
            }
        });
    }

    static void createNewNodeFromState(State state, Node parent, int depth, Car car, String direction){
        if (state == null) return;
//        state.printMap();
        if (hash.putIfAbsent(state, state) == null){
            currentNodes.add(new Node(state,depth,parent, car.getColor()+" "+ direction));
        }

//        System.out.println("Node created with car " + car.getColor() + " moved");
    }

    static State moveCar(String inDirection, State currentState, Car car, int carIndex){

        String[][] currentMap = currentState.getMap();

        State newState = new State(currentState.getPosition());

        switch (inDirection){
            case "LEFT":{
                if (car.isVertical() || car.getColumn() == 1) return null;

                if (currentMap[car.getRow()-1][car.getColumn()-2] != null) return null;

                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow(),car.getColumn()-1, car.isVertical()));
                newState.generateMap();
                boolean enableDirectionPrint = false;
                enableDirectionPrint ?System.out.println(car.getColor() + " Went LEFT") : null;
                return newState;
            }
            case "RIGHT":{
                if (car.isVertical() || car.getLength()==2 && car.getColumn() == 5 || car.getLength()==3 && car.getColumn() == 4 ) return null;

                if (currentMap[car.getRow()-1][car.getColumn() + car.getLength() -1] != null) return null;

                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow(),car.getColumn()+1, car.isVertical()));
                newState.generateMap();
                System.out.println(car.getColor() +" Went RIGHT");
                return newState;
            }
            case "UP":{
                if (!car.isVertical() || car.getRow() == 1) return null;

                if (currentMap[car.getRow()-2][car.getColumn()-1] != null) return null;

                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow()-1,car.getColumn(), car.isVertical()));
                newState.generateMap();
                System.out.println(car.getColor() + " Went UP");
                return newState;
            }
            case "DOWN":{
                if (!car.isVertical() ||  car.getLength()==2 && car.getRow() == 5 || car.isVertical()&& car.getLength()==3 && car.getRow() == 4 ) return null;

                if (currentMap[car.getRow() +car.getLength() -1][car.getColumn()-1] != null) return null;

                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow()+1,car.getColumn(), car.isVertical()));
                newState.generateMap();
                System.out.println(car.getColor() + " Went DOWN");
                return newState;
            }
            default:
                return null;
        }

    }

}
