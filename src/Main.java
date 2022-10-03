import Classes.Car;
import Classes.Node;
import Classes.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Main {


    public static void main(String[] args) throws CloneNotSupportedException {
        HashMap<State, State> hash = new HashMap<>();
        ArrayList<Node> currentNodes = new ArrayList<>();
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
        hash.putIfAbsent(root.getState(),root.getState());
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
                System.out.println(currentNodes.get(i).getState().getPosition());
//                System.out.println(currentNodes.get(i) +" "+ currentNodes.get(i).getLastStep());
//                currentNodes.get(i).getState().printMap();
                getNextState(currentNodes.get(i), finalDepth + 1);
            }
            depth = depth +1;
        }while (solution == null);

        if (solution != null){
            printPath(solution);
        }


    }
    void printPath(Node solution){
        System.out.println("im here");
        if (solution.getParent() == null) return;
        System.out.println(solution.getDepth());
        printPath(solution.getParent());

    }


     void getNextState(Node knot, int depth){
        State currentState = knot.getState();
        ArrayList<Car> cars = currentState.getPosition();

        cars.forEach((item)->{
            int itemIndex = cars.indexOf(item);
//            System.out.println("-------------------");
//            System.out.println(item.getColor().toUpperCase(Locale.ROOT));
//            currentState.printMap();
            if (item.isVertical()){
                State upState = null;
                State downState = null;
                try {
                    upState = moveCar("UP", currentState, item, itemIndex);
                    downState = moveCar("DOWN", currentState, item, itemIndex);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                createNewNodeFromState(upState, knot, depth, item,"UP");
                createNewNodeFromState(downState, knot, depth, item, "DOWN");
            }else {
                State leftState = null;
                State rightState = null;
                try {
                    leftState = moveCar("LEFT", currentState, item, itemIndex);
                    rightState = moveCar("RIGHT", currentState, item, itemIndex);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                createNewNodeFromState(leftState, knot, depth, item, "LEFT");
                createNewNodeFromState(rightState, knot, depth, item, "RIGHT");
            }
        });
    }

     void createNewNodeFromState(State state, Node parent, int depth, Car car, String direction){
        if (state == null) return;
//        System.out.println("STATE ID-> " + state);
//        state.printMap();
        if (hash.putIfAbsent(state, state) == null){
            currentNodes.add(new Node(state,depth,parent, car.getColor()+" "+ direction));
        }

//        System.out.println("Node created with car " + car.getColor() + " moved");
    }

    static State moveCar(String inDirection, State currentState, Car car, int carIndex) throws CloneNotSupportedException {
        boolean enableDirectionPrint = false;
        String[][] currentMap = currentState.getMap();



        switch (inDirection){
            case "LEFT":{
                if (car.isVertical() || car.getColumn() == 1) return null;

                if (currentMap[car.getRow()-1][car.getColumn()-2] != null) return null;

                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow(),car.getColumn()-1, car.isVertical()));
                newState.generateMap();

                System.out.print(enableDirectionPrint ? car.getColor() + "\n Went LEFT\n" : "");
                return newState;
            }
            case "RIGHT":{
                if (car.isVertical() || car.getLength()==2 && car.getColumn() == 5 || car.getLength()==3 && car.getColumn() == 4 ) return null;

                if (currentMap[car.getRow()-1][car.getColumn() + car.getLength() -1] != null) return null;

                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow(),car.getColumn()+1, car.isVertical()));
                newState.generateMap();
                System.out.print(enableDirectionPrint ? car.getColor() + "\n Went RIGHT\n" : "");
                return newState;
            }
            case "UP":{
                if (!car.isVertical() || car.getRow() == 1) return null;

                if (currentMap[car.getRow()-2][car.getColumn()-1] != null) return null;
                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow()-1,car.getColumn(), car.isVertical()));
                newState.generateMap();
                System.out.print(enableDirectionPrint ? car.getColor() + "\n Went UP\n" : "");
                return newState;
            }
            case "DOWN":{
                if (!car.isVertical() ||  car.getLength()==2 && car.getRow() == 5 || car.isVertical()&& car.getLength()==3 && car.getRow() == 4 ) return null;

                if (currentMap[car.getRow() +car.getLength() -1][car.getColumn()-1] != null) return null;
                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow()+1,car.getColumn(), car.isVertical()));
                newState.generateMap();
                System.out.print(enableDirectionPrint ? car.getColor() + "\n Went DOWN\n" : "");
                return newState;
            }
            default:
                return null;
        }

    }

}
