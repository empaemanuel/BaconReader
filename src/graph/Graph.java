package graph;

import java.util.*;

/**
 * Graph of persons connected by productions. Each person holds a list of productions that it
 * is associated with. A production makes up for a complete sub-graph of persons.
 * The graph uses a breath first search to find the shortest path between two nodes.
 */
public class Graph {

    /**
     * A list of all nodes.
     */
    private List<Person> nodes;

    /**
     * Constructs a new graph and loads adjacency list.
     * @param nodes list of all nodes in the bacon graph.
     */
    public Graph(List<Person> nodes) {
        this.nodes = nodes;
        //new Thread(this::build).start();
        populateCompletedGraphs();
    }

    /**
     * Goes through every production and to form completed sub-graphs
     */
    private void populateCompletedGraphs(){
        for(Person person : nodes){
            for(Production prod : person.getProductions()){
                adjacencyMap.putIfAbsent(prod, new LinkedList<>());
                adjacencyMap.get(prod).add(person);
            }
        }
    }

    /**
     * An adjacency representation of nodes grouped by edge name. The key is a String representation of a an edge with
     * a unique name. The key set represents all unique edges in the graph.
     * List<Integer> is a collection of nodes who all are associated by the key and together creates a completed graph.
     * The nodes are stored as integers representing the index of which they can be found at in the nodes collection.
     */
    private HashMap<Production, List<Person>> adjacencyMap = new HashMap<>(700011);

    private Person source; //Bacon
    private Person target;

    /**
     * Sets the source of the search to Person with same name as argument.
     * @throws IllegalArgumentException if node can't be found.
     */
    public void setSource(String name) throws IllegalArgumentException{
        source = findNode(name);
    }

    /**
     * Get method for source node.
     * @throws IllegalStateException if called on null.
     * @return String representation of the node.
     */
    public String getSource(){
        if(source == null) throw new IllegalStateException("Source has to be set before it can be accessed");
        return source.getName();
    }

    /**
     * Sets the name of the target of the search.
     * @throws IllegalArgumentException if node can't be found.
     */
    public void setTarget(String name) throws IllegalArgumentException{
        target = findNode(name);
    }

    /**
     * Get method for target node.
     * @throws IllegalStateException if called on null.
     * @return String representation of the node.
     */
    public String getTarget() throws IllegalStateException{
        if(target == null) throw new IllegalStateException("Target has to be set before it can be accessed");
        return target.getName();
    }


    /**
     * driver method for finding the shortest path from source to target.
     * @throws IllegalStateException if source or target is missing.
     * @return sorted list of edges from source to target.
     */
    public List<Person> findShortestPath() throws IllegalStateException{
        if(target == null || source == null)
            throw new IllegalStateException("Both target and source must be assigned to before a shortest-path can be generated.");
        breathFirstSearch();
        return null;
    }

    private LinkedList<Edge> path;
    /**
     * Breath first search from source to target.
     *
     */
    private void breathFirstSearch(){
        if(source == target) return;

        LinkedList<Edge> edges = new LinkedList<>();
        LinkedList<Person> queue = new LinkedList<>();
        HashSet<Production> visited = new HashSet<>(adjacencyMap.size());


        queue.add(source);
        Person currentPerson;
        boolean targetFound = false;

        while(queue.size() != 0){
            currentPerson = queue.poll();
            currentPerson.visit();
            List<Production> productions = currentPerson.getProductions();
            for(Production prod : productions){
                if(visited.contains(prod)) continue;
                visited.add(prod);
                for(Person adjPerson : adjacencyMap.get(prod)){
                    if(adjPerson.hasBeenVisited()) continue;
                    edges.addFirst(new Edge(currentPerson, adjPerson));
                    queue.add(adjPerson);
                    if(adjPerson.equals(target)){
                        //Target found
                        queue.clear();
                        targetFound = true;
                        break;
                    }
                }
                if(targetFound) break;
            }
        }

        //========================
        //  generate result list
        path = new LinkedList<>();
        boolean first = true;
        for(Edge edge : edges){
            //System.out.println(edge);
            if(first) { path.add(edge); first = false; }
            if(path.get(0).getFrom().equals(edge.getTo())) path.addFirst(edge);
            if(path.get(0).getFrom().equals(source)) break;
        }

        for(Edge e : path){
            System.out.println("===" + e.getFrom() + " : " + e.getTo());
            List<Production> common = new ArrayList<>(e.getFrom().getProductions());
            common.retainAll(e.getTo().getProductions());
            for(Production p : common){
                System.out.println(p);
            }
        }

    }

    /**
     * Searches for the a node in linear time.
     * @param name is the String representation of the node.
     * @return Person with name matching argument.
     * @throws IllegalArgumentException if there was no match.
     */
    private Person findNode(String name){
        for(Person p : nodes){
            if(p.getName().equals(name))
                return p;
        }
        throw new IllegalArgumentException(name + " not found.");
    }

    /**
     * Getter that returns String representation of node at index.
     * @throws IllegalArgumentException if called on index out of bounds.
     * @param index of the node in the data structure.
     * @return Person representing node at index.
     */
    private Person getNode(int index){
        try{
            return nodes.get(index);
        } catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Not a valid index: " + index);
        }
    }

}
