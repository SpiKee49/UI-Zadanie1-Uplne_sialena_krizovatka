package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State implements Cloneable {
    private ArrayList<Car> state;
    private String[][] map;
    public State(ArrayList<Car> cars) {
        this.state = new ArrayList<>(cars.size());
        this.state.addAll(cars);
        generateMap();
    }
    public void generateMap(){
        this.map  = new String[6][6];
        this.state.forEach(item->{
            for (int i = 0; i< item.getLength(); i++){
                int x = item.isVertical() ? item.getRow() -1 +i : item.getRow() -1;
                int y = item.isVertical() ? item.getColumn() -1: item.getColumn() -1 + i;
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


    public void printMap(){
        for (String[] row: this.map) {
            for (String item: row) {
                System.out.print(item == null? "  ": (item.substring(0,1).toUpperCase()+ ' '));
            }
            System.out.print('\n');
        }
    }

    public Car findCarByColor(String color){
        return this.state.stream().filter(item-> Objects.equals(item.getColor(),color)).findFirst().orElse(null);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
