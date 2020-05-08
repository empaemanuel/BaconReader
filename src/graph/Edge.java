package graph;

public class Edge {
    private Person from;
    private Person to;

    public Edge(Person from, Person to) {
        this.from = from;
        this.to = to;
    }

    public Person getFrom() {
        return from;
    }

    public Person getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
