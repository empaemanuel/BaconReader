package graph;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private List<Production> productions = new ArrayList<>();
    private boolean visited;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVisited(boolean visited){
        this.visited = visited;
    }

    public void setProductions(List<Production> arr){
        productions = arr;
    }

    public List<Production> getProductions(){
        return productions;
    }

    public boolean hasBeenVisited() {
        return visited;
    }

    @Override
    public String toString() {
        return name;
    }

    private boolean compared;
    public void setCompared(boolean b) {
        compared = b;
    }

    public boolean hasBeenCompared(){
        return compared;
    }
}
