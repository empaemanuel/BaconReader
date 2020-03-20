import java.util.*;

public class Person {
    private String name;
    private Set<Production> productions = new HashSet<>();
    private boolean visited = false;
    private boolean hasBeenCompared = false;

    Person(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addProduction(Production production){
        productions.add(production);
    }

    public Set<Production> getProductions(){
        return productions;
    }

    public int getNumberOfProductions(){
        return productions.size();
    }

    public void setVisitedFlag(boolean flag){
        visited = flag;
    }

    public boolean hasBeenVisited(){
        return visited;
    }

    public void hasBeenCompared(boolean flag){
        hasBeenCompared = flag;
    }

    public boolean hasBeenCompared(){return hasBeenCompared;}

    @Override
    public String toString() {
        return name + ": " + Objects.hash(getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(getName(), person.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

}
