package graph;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Edge> path;
    private Person from;
    private Person to;

    public Path(List<Edge> path) {
        this.path = path;
        this.from = path.get(0).getFrom();
        this.to = path.get(path.size()-1).getTo();
    }

    public String getSource(){
        return from.getName();
    }

    public String getTarget(){
        return to.getName();
    }

    public int getBaconNumber(){
        return path.size();
    }

    public void printPath() {
        // prints the path from source to target
        System.out.println("============== BACON PRINTER ============");
        System.out.println("FROM: " + from.getName());
        System.out.println("TO: " + to.getName());
        System.out.println("Bacon number: " + getBaconNumber());
        for(Edge e : path){
            System.out.println(e.getFrom() + " --> " + e.getTo());
            List<Production> common = new ArrayList<>(e.getFrom().getProductions());
            common.retainAll(e.getTo().getProductions());
            for(Production p : common){
                System.out.println("\t* " + p);
            }
        }
    }

}
