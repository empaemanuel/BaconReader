package graph;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private int index;
    private List<Production> productions = new ArrayList<>();

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

    public void addProduction(Production p){
        productions.add(p);
    }

    public List<Production> getProductions(){
        return productions;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name);
    }
}
