package Classes;

import java.util.*;

public class App {

    //hashmap to hold duplicit states
    private HashMap<String, Integer> hash = new HashMap<>();

    //stack to hold latest steps from root to current node

    private Node solution = null;

    //constructor
    public App() {

    }

//            cars.add(new Car("red", 2, 3, 4, false));
//        cars.add(new Car("orange", 2, 1, 2, false));
//        cars.add(new Car("yellow", 3, 1, 1, true));
//        cars.add(new Car("purple", 2, 4, 1, true));
//        cars.add(new Car("green", 3, 4, 4, true));
//        cars.add(new Car("blue", 3, 6, 1, false));
//        cars.add(new Car("gray", 2, 5, 2, false));
//        cars.add(new Car("dark-blue", 3, 4, 6, true));

    public void run(){

        int depth = 0;
        //car input array
        ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car("red", 2, 3, 2, false));
        cars.add(new Car("orange", 2, 1, 1, false));
        cars.add(new Car("yellow", 3, 2, 1, true));
        cars.add(new Car("purple", 2, 5, 1, true));
        cars.add(new Car("green", 3, 2, 4, true));
        cars.add(new Car("blue", 3, 6, 3, false));
        cars.add(new Car("gray", 2, 5, 5, false));
        cars.add(new Car("dark-blue", 3, 1, 6, true));

        //create new state from cars positions
        State entry = new State(cars);
        Stack<String> possibleMoves = new Stack<>();
        //create available moves Stack with format -> "color/DIRECTION"
        cars.forEach(item -> {
            if (item.isVertical()) {
                possibleMoves.push(item.getColor() + "/UP");
                possibleMoves.push(item.getColor() + "/DOWN");
                return;
            }
            possibleMoves.push(item.getColor() + "/LEFT");
            possibleMoves.push(item.getColor() + "/RIGHT");

        });

        //create root node
        Node root = new Node(null, entry, depth, possibleMoves);
        hash.put(root.getState().toString(), 1);



        boolean again = true;
        getNextState(root, 1, possibleMoves);




    }

    void printPath(Node solution) {
        if (solution == null){
            return;
        }
        solution.getState().printMap();
        System.out.println("-------------");
        printPath(solution.parent);

    }


    void getNextState(Node knot, int depth, Stack<String> possibleMoves) {

        State currentState = knot.getState();
        ArrayList<Car> cars = currentState.getPosition();

        //we are in root and we traversed through entire tree and found no match we reset the tree and continue to search for the solution
        if (knot.parent == null && knot.availableMoves.isEmpty()){
            knot.setAvailableMoves(possibleMoves);
            depth+=1;
        }
        System.out.println(depth);
        //check if we went through and found no match then we return to the parent Node
        if (knot.availableMoves.isEmpty()) {
            Node newNode = knot.parent;
            knot.parent = null;
            getNextState(newNode, depth, possibleMoves);
            return;
        }


        // nextMove format after split -> ["color","DIRECTION"]
        String[] nextMove = knot.availableMoves.pop().split("/");

        //if the knot is in wanted depth
        if (depth == knot.getDepth()) {
            //look for car at exit and check if it is horizontal, then we found solution
            Car match = knot.getState().findCarByColor(knot.getState().getMap()[2][5]);
            if (match != null && !match.isVertical()) {
                solution = knot;
                printPath(knot);
                return;
            }
            //if the match is NULL or is vertical then we have to look for other sibling
            Node newNode = knot.parent;
            knot.parent = null;
            getNextState(newNode, depth, possibleMoves);
            return;
        }

        //find car from current move obtained from Stack availableMoves
        Car car = cars.stream().filter(item -> Objects.equals(item.getColor(), nextMove[0])).findFirst().orElse(null);

        if (car == null) return;


        int carIndex = cars.indexOf(car);

        //make new state with changed position on chosen car
        State newState = moveCar(nextMove[1], currentState, car, carIndex);
        //TODO if we cant make a move newSate comes back as null -> then we have to run func again on same node but pick differend car
        if (newState == null){
            getNextState(knot, depth, possibleMoves);
            return;
        }
        //check if state was already found
        if (hash.putIfAbsent(newState.toString(), 1) == null) {
            Node newKnot = new Node(knot, newState, knot.getDepth()+ 1, possibleMoves);
            getNextState(newKnot, depth, possibleMoves);

        }else {                         //if it was we gotta look for another sibling
            Node newNode = knot.parent;
            knot.parent = null;
            getNextState(newNode, depth, possibleMoves);

        }

    }


    State moveCar(String inDirection, State currentState, Car car, int carIndex) {
        //get map with cars to tell which squares are occupated
        String[][] currentMap = currentState.getMap();


        switch (inDirection) {
            case "LEFT": {
                if (car.isVertical() || car.getColumn() == 1) return null;  //check if the car isnt at the edge position

                //if it isnt at the edge, we have to check if there isnt another car blocking desired square
                if (currentMap[car.getRow() - 1][car.getColumn() - 2] != null) return null;

                State newState = new State(currentState.getPosition()); //copy current state

                //replace current car with new one moved by one square in desired direction: in this case LEFT
                newState.getPosition().set(carIndex, new Car(car.getColor(), car.getLength(), car.getRow(), car.getColumn() - 1, car.isVertical()));
                newState.generateMap();

                //System.out.print(enableDirectionPrint ? car.getColor() + "\n Went LEFT\n" : "");
                return newState;
            }
            case "RIGHT": {
                if (car.isVertical() || car.getLength() == 2 && car.getColumn() == 5 || car.getLength() == 3 && car.getColumn() == 4)
                    return null;

                if (currentMap[car.getRow() - 1][car.getColumn() + car.getLength() - 1] != null) return null;

                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(), car.getLength(), car.getRow(), car.getColumn() + 1, car.isVertical()));
                newState.generateMap();
                //System.out.print(enableDirectionPrint ? car.getColor() + "\n Went RIGHT\n" : "");
                return newState;
            }
            case "UP": {
                if (car.isVertical() && car.getRow() == 1) return null;

                if (currentMap[car.getRow() - 2][car.getColumn() - 1] != null) return null;
                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(), car.getLength(), car.getRow() - 1, car.getColumn(), car.isVertical()));
                newState.generateMap();
                //System.out.print(enableDirectionPrint ? car.getColor() + "\n Went UP\n" : "");
                return newState;
            }
            case "DOWN": {
                if (!car.isVertical() || car.getLength() == 2 && car.getRow() == 5 || car.isVertical() && car.getLength() == 3 && car.getRow() == 4)
                    return null;

                if (currentMap[car.getRow() + car.getLength() - 1][car.getColumn() - 1] != null) return null;
                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(), car.getLength(), car.getRow() + 1, car.getColumn(), car.isVertical()));
                newState.generateMap();
                //System.out.print(enableDirectionPrint ? car.getColor() + "\n Went DOWN\n" : "");
                return newState;
            }
            default:
                return null;
        }

    }

}


