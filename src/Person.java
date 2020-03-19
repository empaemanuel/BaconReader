import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Person {
    private String name;
    //private List<Production> productions = new LinkedList<>();

    Person(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addProduction(Production production){
        //productions.add(0,production);
    }

    @Override
    public String toString() {
        return name;
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
