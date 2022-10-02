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
        Node root = new Node(entry,depth,null);
        currentNodes.add(root);
        Node solution = null ;

        while (true){
            int finalDepth = depth;
            currentNodes.removeIf(item -> item.getDepth() != finalDepth);
            if (currentNodes.isEmpty()){
                System.out.println("Problem does not have any solution");
                break;
            }
            for (Node item: currentNodes) {
                if (item.getState().findCarByColor(item.getState().getMap()[2][5]).isVertical()){
                    solution = item;
                    break;
                }
            }

            currentNodes.forEach(item-> getNextState(item,finalDepth));
            depth++;
        }

        if (solution != null){
            printPath(solution);
        }


    }
    static void printPath(Node solution){
        if (solution.getParent() == null) return;
        System.out.println(solution.getDepth());
        printPath(solution.getParent());

    }


    static void getNextState(Node knot, int depth){
        ArrayList<Car> cars = knot.getState().getPosition();
        State currentState = knot.getState();

        cars.forEach((item)->{
            int itemIndex = cars.indexOf(item);
            State leftState = moveCar("LEFT", currentState, item, itemIndex);
            State rightState = moveCar("RIGHT", currentState, item, itemIndex);
            State upState = moveCar("UP", currentState, item, itemIndex);
            State downState = moveCar("DOWN", currentState, item, itemIndex);
            if (hash.putIfAbsent(leftState, leftState) == null){
                currentNodes.add(new Node(leftState,depth,knot));
            }
            if (hash.putIfAbsent(rightState, rightState) == null){
                currentNodes.add(new Node(rightState,depth,knot));
            }
            if (hash.putIfAbsent(upState, upState) == null){
                currentNodes.add(new Node(upState,depth,knot));
            }
            if (hash.putIfAbsent(downState, downState) == null){
                currentNodes.add(new Node(downState,depth,knot));
            }
        });
    }

    static State moveCar(String inDirection, State currentState, Car car, int carIndex){
        // if the car is situated in first 'colum', then there isn't a way for it to move left
        if ((car.isVertical() && car.getRow() == 1 && Objects.equals(inDirection, "UP"))||
                (car.isVertical()&& car.getLength()==2 && car.getRow() == 5 && Objects.equals(inDirection, "DOWN"))||
                (car.isVertical()&& car.getLength()==3 && car.getRow() == 4 && Objects.equals(inDirection, "DOWN"))||
                (!car.isVertical() && car.getColumn() == 1 && Objects.equals(inDirection, "LEFT"))||
                (!car.isVertical()&& car.getLength()==2 && car.getColumn() == 5 && Objects.equals(inDirection, "RIGHT"))||
                (!car.isVertical()&& car.getLength()==3 && car.getColumn() == 4 && Objects.equals(inDirection, "RIGHT"))
        ) return null;

        String[][] currentMap = currentState.getMap();



        State newState = new State(currentState.getPosition());

        switch (inDirection){
            case "LEFT":{
                if (car.isVertical()) return null;
                if (currentMap[car.getRow()-1][car.getColumn()-2] != null) return null;
                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow(),car.getColumn()-1, car.isVertical()));
            }
            case "RIGHT":{
                if (car.isVertical()) return null;
                System.out.println(car.getColor() + " " + car.getRow() + " " + car.getColumn());
                if (currentMap[car.getRow()-1][car.getColumn() + car.getLength() -1] != null) return null;
                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow(),car.getColumn()+1, car.isVertical()));
            }
            case "UP":{
                if (!car.isVertical()) return null;
                if (currentMap[car.getRow()][car.getColumn()-1] != null) return null;
                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow()-1,car.getColumn(), car.isVertical()));
            }
            case "DOWN":{
                if (!car.isVertical()) return null;
                if (currentMap[car.getRow() +car.getLength() -1][car.getColumn()-1] != null) return null;
                newState.getPosition().set(carIndex, new Car(car.getColor(),car.getLength(),car.getRow()+1,car.getColumn(), car.isVertical()));
            }
        }



        return newState;
    }

}
