package Classes;

import java.util.*;

public class App {

    //hashmap to hold duplicit states

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
        ArrayList<String> possibleMoves = new ArrayList<>();
        //create available moves Stack with format -> "color/DIRECTION"
        cars.forEach(item -> {
            if (item.isVertical()) {
                possibleMoves.add(item.getColor() + "/UP");
                possibleMoves.add(item.getColor() + "/DOWN");
                return;
            }
            possibleMoves.add(item.getColor() + "/LEFT");
            possibleMoves.add(item.getColor() + "/RIGHT");

        });

        //create root node
        Node root = new Node(null, entry, depth);

        boolean again = true;

        while(again){
            depth++;
            again = false;
        getNextState(root, depth, possibleMoves);
        if (solution == null){

            again = true;
        }
        }
        printPath(solution);

    }

    void printPath(Node solution) {
        if (solution == null){
            return;
        }
        solution.getState().printMap();
        System.out.println("-------------");

    }


    void getNextState(Node knot, int finalDepth, ArrayList<String> possibleMoves) {
//      1. skontroluj hlbku v akej sme, ak je < ako pozadovana chod na 3. || return
        System.out.println(knot.getDepth());
        //if the knot is in wanted depth
        if (finalDepth == knot.getDepth()) {

//        2. check ci sme nanasli solution ak ano return
            //look for car at exit and check if it is horizontal, then we found solution
            Car match = knot.getState().findCarByColor(knot.getState().getMap()[2][5]);
            if (match != null && !match.isVertical()) {
                solution = knot;
                knot.parent = null;
            }
            return;
        }

        State currentState = knot.getState();
        ArrayList<Car> cars = currentState.getPosition();

//        3. possibleMoves.forEach(move->getNextState)
        possibleMoves.forEach(move->{

            // nextMove format after split -> ["color","DIRECTION"]
            String[] nextMove = move.split("/");

            //find car from current move obtained from Stack availableMoves
            Car car = cars.stream().filter(item -> Objects.equals(item.getColor(), nextMove[0])).findFirst().orElse(null);

            if (car == null) return;
            int carIndex = cars.indexOf(car);

            //make new state with changed position on chosen car
            State newState = moveCar(nextMove[1], currentState, car, carIndex);

            if (newState == null) {
                return;
            }

            // state wasnt null so the move was valid, therefore we can create new node
            Node newKnot = new Node(knot, newState, knot.getDepth()+ 1);
            getNextState(newKnot,finalDepth,possibleMoves);


        });





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


