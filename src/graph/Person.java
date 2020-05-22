package graph;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private int index;
    private List<Production> productions = new ArrayList<>();
    private boolean visited;

    public Person(String name) {
        this.name = name;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex(){
        return index;
    }

    public void setProductions(List<Production> arr){
        productions = arr;
    }

    public List<Production> getProductions(){
        return productions;
    }

    @Override
    public String toString() {
        return name;
    }

}
