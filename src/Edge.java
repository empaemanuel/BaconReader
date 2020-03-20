public class Edge {
    private Person from, to;
    private Production connection;

    public Edge(Person from, Person to, Production connection) {
        this.from = from;
        this.to = to;
        this.connection = connection;
    }

    public Person from(){
        return from;
    }

    public Person to(){
        return to;
    }

    public Production connection(){
        return connection;
    }
}
