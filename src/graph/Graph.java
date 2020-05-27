package graph;

import presentation.Presentation;

import java.util.*;

/**
 * Non-Directional multi edge graph where each node is a Person and each edge is relationship between two persons by
 * some production. A production makes up for a completed graph. Nodes are considered static as they
 * are loaded from files and the graph is adjusted for that specific set of files.
 * Nodes are stored in an array for O(1) inserts during creating and O(1) access by index during search.
 * The graph uses a breath first search to find the shortest path between two nodes.
 */
public class Graph implements Runnable{
    Presentation presentation;
    /**
     * A list of all nodes.
     */
    private List<Person> nodes;

    /**
     * Constructs a new graph and loads adjacency list.
     * @param nodes list of all nodes in the bacon graph.
     */
    public Graph(List<Person> nodes, Presentation presentation) {
        this.nodes = nodes;
        this.presentation = presentation;
        //new Thread(this::build).start();
        //populateCompletedGraphs();
    }

    public Graph(List<Person> nodes) {
        this.nodes = nodes;
        //new Thread(this::build).start();
        populateCompletedGraphs();
    }

    @Override
    public void run() {
        populateCompletedGraphs();
    }

    /**
     * Goes through every production and to form completed sub-graphs
     */
    private void populateCompletedGraphs(){
        System.out.println("Loading adjacency map...");
        for(Person person : nodes){
            for(Production prod : person.getProductions()){

                adjacencyMap.putIfAbsent(prod, new LinkedList<>());
                adjacencyMap.get(prod).addFirst(person);
            }
        }
        System.out.println("Adjacency map loaded!");
        if(presentation!=null)presentation.enableSearch();
    }

    /**
     * An adjacency representation of nodes grouped by edge name. The key is a String representation of a an edge with
     * a unique name. The key set represents all unique edges in the graph.
     * List<Integer> is a collection of nodes who all are associated by the key and together creates a completed graph.
     * The nodes are stored as integers representing the index of which they can be found at in the nodes collection.
     * The initial capacity is 1.25% higher than the total amount of productions to prevent rehashing during insert.
     */
    private HashMap<Production, LinkedList<Person>> adjacencyMap = new HashMap<>(4814365);

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
    public Path findShortestPath() throws IllegalStateException{
        if(target == null || source == null)
            throw new IllegalStateException("Both target and source must be assigned to before a shortest-path can be generated.");
        System.out.println("Starting breath first search");
        List<Edge> edges = breathFirstSearch();
        System.out.println("Breath first search done!");
        return extractPath(edges);
    }

    /**
     * Breath first search from source to target.
     * @Returns a stack of edges with target at top and source at bottom.
     */
    private List<Edge> breathFirstSearch() {
        LinkedList<Edge> edges = new LinkedList<>();
        LinkedList<Person> queue = new LinkedList<>();
        HashSet<Production> visitedProductions = new HashSet<>(adjacencyMap.size());


        if (source == target){
            edges.add(new Edge(source, target));
            return edges;
        };
        boolean[] visitedPersons = new boolean[nodes.size()];


        queue.add(source);
        visitedPersons[source.getIndex()] = true;
        Person currentPerson;
        boolean targetFound = false;

        while (queue.size() != 0) {
            //move to next node.
            currentPerson = queue.poll();

            //go through all completed graphs the person is associated with.
            //note: a production makes up a completed graph.
            for (Production prod : currentPerson.getProductions()) {

                //if a completed graph has already been visited, jump to next.
                if (visitedProductions.contains(prod)) continue;

                //add production to visited.
                visitedProductions.add(prod);

                //Go through all nodes in completed graph and search for node
                for (Person adjPerson : adjacencyMap.get(prod)) {
                    //if node not already added to queue, add and make an edge.
                    if (visitedPersons[adjPerson.getIndex()]) continue;
                    queue.add(adjPerson);
                    edges.push(new Edge(currentPerson, adjPerson));
                    visitedPersons[adjPerson.getIndex()] = true;

                    //if target is found: clear queue, add edge and stop.
                    if (adjPerson.equals(target)) {
                        targetFound = true;
                        queue.clear();
                        break;
                    }
                }
                //breaks the outer production loop
                if (targetFound) break;
            }
        }
        return edges;
    }

    /***
     * takes a stack of edges and returns an instance of Path.
     * A path has list of Edges going from source to target.
     * @param edges
     * @return
     */
    private Path extractPath(List<Edge> edges) {
        //===============================
        // backtrack to make result list
        LinkedList<Edge> path = new LinkedList<>();
        boolean first = true;
        for(Edge edge : edges){
            //first one is target and should always be added.
            if(first) { path.add(edge); first = false; }
            //if source was found break the loop.
            if (path.get(0).getFrom().equals(source)) break;
            //add middle edges.
            if (path.get(0).getFrom().equals(edge.getTo())) path.addFirst(edge);
        }
        return new Path(path);
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
