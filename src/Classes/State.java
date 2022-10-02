package Classes;

import java.util.ArrayList;
import java.util.Objects;

public class State {
    private ArrayList<Car> state;
    private String[][] map = new String[6][6];
    public State(ArrayList<Car> cars){
        this.state = cars;
        generateMap();
    }
    private void generateMap(){
        this.state.forEach(item->{
            for (int i = 0; i< item.getLength(); i++){
                int x = item.isVertical() ? item.getRow() -1 +i : item.getRow() -1;
                int y = item.isVertical() ? item.getColumn() -1: item.getColumn() -1 + i;
//                System.out.println(item.getColor() + " " + x + " " +y);
                this.map[x][y] = item.getColor();
            }
        });
    }

    public ArrayList<Car> getPosition() {
        return state;
    }

    public String[][] getMap() {
        return map;
    }

    private void printMap(){
        for (String[] row: this.map) {
            for (String item: row) {
                System.out.print(item == null? "0 ": (item+ ' '));
            }
            System.out.print('\n');
        }
    }

    public Car findCarByColor(String color){
        return this.state.stream().filter(item-> Objects.equals(item.getColor(),color)).findFirst().orElse(null);
    }
}
