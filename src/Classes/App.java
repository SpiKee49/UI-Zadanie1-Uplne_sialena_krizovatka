package Classes;

import java.util.*;

public class App {

    //Node that will be use if we found solution
    private Node solution = null;

    //constructor
    public App() {

    }




    public void run() {
        String[] problem1 = new String[]{"red 2 3 4 false", "orange 2 1 2 false","yellow 3 1 1 true","purple 2 4 1 true","green 3 4 4 true","blue 3 6 1 false","gray 2 5 2 false","dark-blue 3 4 6 true"};
       //TODO 2 doesnt work
        String[] problem2 = new String[]{"red 2 3 1 false", "orange 2 5 6 true","yellow 3 1 3 true","purple 3 4 1 true","green 3 4 2 false","blue 2 5 2 false","gray 2 1 1 false","dark-blue 2 3 6 true"};
        String[] problem3 = new String[]{"red 2 3 2 false", "orange 2 5 6 true","yellow 3 1 4 true","purple 3 1 1 true","green 3 4 2 false","blue 2 5 2 false","gray 2 2 1 false","dark-blue 2 3 6 true"};
        String[] problem4 = new String[]{"red 2 3 2 false", "orange 2 3 5 true","yellow 3 3 4 true","purple 3 2 1 true","green 3 6 1 false","blue 2 5 1 false","gray 2 1 1 false","dark-blue 2 3 6 true"};





        //car input array
        ArrayList<Car> cars = problemToArrayList(problem1);



//        cars.add(new Car("red", 2, 3, 2, false));
//        cars.add(new Car("orange", 2, 1, 1, false));
//        cars.add(new Car("yellow", 3, 2, 1, true));
//        cars.add(new Car("purple", 2, 5, 1, true));
//        cars.add(new Car("green", 3, 2, 4, true));
//        cars.add(new Car("blue", 3, 6, 3, false));
//        cars.add(new Car("gray", 2, 5, 5, false));
//        cars.add(new Car("dark-blue", 3, 1, 6, true));

        int depth = 0;
        //create new state from cars positions
        State entry = new State(cars);
        entry.generateMap();


        ArrayList<String[]> possibleMoves = new ArrayList<>();

        //create available moves Arraylist with format -> "color/DIRECTION"
        cars.forEach(item -> {
            if (item.isVertical()) {
                possibleMoves.add(new String[]{String.valueOf(cars.indexOf(item)), "UP"});
                possibleMoves.add(new String[]{String.valueOf(cars.indexOf(item)), "DOWN"});
                return;
            }
            possibleMoves.add(new String[]{String.valueOf(cars.indexOf(item)), "LEFT"});
            possibleMoves.add(new String[]{String.valueOf(cars.indexOf(item)), "RIGHT"});

        });

        //create root node
        Node root = new Node(null, entry, depth);
        boolean again = true;

        long start = System.currentTimeMillis();
        while (again) {
            depth++;
            System.out.println(depth);
            again = false;
            getNextState(root, depth, possibleMoves);
            if (solution == null) {
                again = true;
            }
        }
        printPath(solution);
        long end = System.currentTimeMillis();
        System.out.println("Took time: " + ((end-start)/1000.0) +"s");

    }

    void printPath(Node solution) {
        if (solution == null) {
            return;
        }
        System.out.println("-------------");
        solution.getState().printMap();
        System.out.println("-------------");

    }


    void getNextState(Node knot, int finalDepth, ArrayList<String[]> possibleMoves) {
//      1. skontroluj hlbku v akej sme, ak je < ako pozadovana chod na 3. || return
        //if the knot is in wanted depth
        if (knot.getDepth() == 13) {
            System.out.println("---------");
            knot.getState().printMap();
        }
        if (finalDepth == knot.getDepth()) {

//        2. check ci sme nanasli solution ak ano return

            //look for car at exit and check if it is horizontal, then we found solution


            if ( knot.getState().getMap()[2][5] == null) return;

            Car match = knot.getState().getPosition().get(knot.getState().getMap()[2][5]);
            if (match != null && !match.isVertical()) {
                solution = knot;
                knot.parent = null;
            }
            return;
        }

        State currentState = knot.getState();
        ArrayList<Car> cars = currentState.getPosition();

//        3. possibleMoves.forEach(move->getNextState)
        possibleMoves.forEach(move -> {


            //find car from current move obtained from Stack availableMoves
            Car car = cars.get(Integer.parseInt(move[0]));

            if (car == null) return;

            //make new state with changed position on chosen car
            State newState = moveCar(move[1], currentState, car, Integer.parseInt(move[0]));
            if (newState == null) {
                return;
            }
            newState.generateMap();
            // state wasnt null so the move was valid, therefore we can create new node
            Node newKnot = new Node(knot, newState, knot.getDepth() + 1);
            getNextState(newKnot, finalDepth, possibleMoves);


        });


    }


    State moveCar(String inDirection, State currentState, Car car, int carIndex) {
        //get map with cars to tell which squares are occupated

        Integer[][] currentMap = currentState.getMap();


        switch (inDirection) {
            case "LEFT": {
                if (car.isVertical() || car.getColumn() == 1) return null;  //check if the car isnt at the edge position

                //if it isnt at the edge, we have to check if there isnt another car blocking desired square
                if (currentMap[car.getRow() - 1][car.getColumn() - 2] != null) return null;

                State newState = new State(currentState.getPosition()); //copy current state

                //replace current car with new one moved by one square in desired direction: in this case LEFT
                newState.getPosition().set(carIndex, new Car(car.getColor(), car.getLength(), car.getRow(), car.getColumn() - 1, car.isVertical()));

                return newState;
            }
            case "RIGHT": {
                if (car.isVertical() || car.getLength() == 2 && car.getColumn() == 5 || car.getLength() == 3 && car.getColumn() == 4)
                    return null;

                if (currentMap[car.getRow() - 1][car.getColumn() + car.getLength() - 1] != null) return null;

                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(), car.getLength(), car.getRow(), car.getColumn() + 1, car.isVertical()));


                return newState;
            }
            case "UP": {
                if (car.isVertical() && car.getRow() == 1) return null;

                if (currentMap[car.getRow() - 2][car.getColumn() - 1] != null) return null;
                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(), car.getLength(), car.getRow() - 1, car.getColumn(), car.isVertical()));
                return newState;
            }
            case "DOWN": {
                if (!car.isVertical() || car.getLength() == 2 && car.getRow() == 5 || car.isVertical() && car.getLength() == 3 && car.getRow() == 4)
                    return null;

                if (currentMap[car.getRow() + car.getLength() - 1][car.getColumn() - 1] != null) return null;
                State newState = new State(currentState.getPosition());
                newState.getPosition().set(carIndex, new Car(car.getColor(), car.getLength(), car.getRow() + 1, car.getColumn(), car.isVertical()));

                return newState;
            }
            default:
                return null;
        }

    }

    ArrayList<Car> problemToArrayList(String[] problem ){
        //problem format "color length x y boolean"
        ArrayList<Car> objects = new ArrayList<>();

        for (String car:problem){
            String[] entry = car.split(" ");
            objects.add(new Car(entry[0],Integer.parseInt(entry[1]),Integer.parseInt(entry[2]),Integer.parseInt(entry[3]),Boolean.parseBoolean(entry[4])));
        }
        return objects;
    }


}


