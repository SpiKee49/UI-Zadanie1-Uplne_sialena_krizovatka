// -------------------------------------
// This code is property of René Bukovina and cannot be distributed or used outside of this project without authors approval.
// -------------------------------------

package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State implements Cloneable {
    private ArrayList<Car> state;
    private Integer[][] map;
    public String lastMove;

    // constructor
    public State(ArrayList<Car> cars) {
        this.state = new ArrayList<>(cars.size());
        this.state.addAll(cars);
    }
    //function to generate 2D array
    public void generateMap(){
        this.map  = new Integer[6][6];
        this.state.forEach(item->{
            for (int i = 0; i< item.getLength(); i++){
                int x = item.isVertical() ? item.getRow() -1 +i : item.getRow() -1;
                int y = item.isVertical() ? item.getColumn() -1: item.getColumn() -1 + i;
                this.map[x][y] = this.state.indexOf(item);
            }
        });
    }

    public ArrayList<Car> getPosition() {
        return state;
    }

    public Integer[][] getMap() {
        return map;
    }


    public void printMap(){
        for (Integer[] row: this.map) {
            for (Integer item: row) {
                System.out.print(item == null? "_ ": item+" ");
            }
            System.out.print('\n');
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
