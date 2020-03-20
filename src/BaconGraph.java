import java.io.IOException;
import java.util.*;
public class BaconGraph {

    private Map<String, Person> persons = new HashMap<>();
    private Map<Production, LinkedList<Person>> personsInProduction = new HashMap<>();

    private Person bacon;

    BaconGraph(){};



    //===================================================
    //Private help methods for handling data structures
    //====================================================

    private Person getPerson(String name){
        return persons.get(name);
    }

    public int getNumberOfPersons(){
        return persons.size();
    }

    public int getNumberOfPersons(String title, String year, String id){
        Production tmp = new Production(title, year, id);
        return personsInProduction.get(tmp).size();
    }

    public int getNumberOfProductions(){
        return personsInProduction.size();
    }

    public int getNumberOfProductions(String name) {
        return getPerson(name).getNumberOfProductions();
    }

    public void clear(){
        persons.clear();
    }

    //=====================================================
    //Breadth-first search methods
    //=====================================================
    public int findBaconNumber(String name){
        if(!hasBacon()) throw new IllegalCallerException("Bacon undefined");
        Person searchFor = getPerson(name);
        if(searchFor == null) return -1;
        return breathFirstSearch(bacon, searchFor);
    }

    private int breathFirstSearch(Person source, Person target){
        if(source.equals(target)) return 0;

        //LinkedList<Person> visited = new LinkedList<>();
        LinkedList<Edge> edges = new LinkedList<>();
        LinkedList<Person> queue = new LinkedList<>();


        queue.add(source);
        Person currentPerson;

        boolean targetFound = false;
        while(queue.size() != 0){
            currentPerson = queue.poll();
            //visited.add(currentPerson);
            currentPerson.setVisitedFlag(true);
            for(Production production : currentPerson.getProductions()){
                for(Person connectedPerson: personsInProduction.get(production)){
                    if(connectedPerson.hasBeenVisited()) continue;
                    if(!connectedPerson.hasBeenCompared()) {
                        edges.addFirst(new Edge(currentPerson, connectedPerson, production));
                        queue.add(connectedPerson);
                        if(connectedPerson.equals(target)){
                            //==================================
                            //Target found
                            queue.clear();
                            targetFound = true;
                            break;

                            //================================
                        }
                    }
                }
                if(targetFound) break;
            }

        }

        //========================
        //  generate result list
        LinkedList<Edge> result = new LinkedList<>();
        boolean first = true;
        for(Edge edge : edges){
            if(first) { result.addFirst(edge); first = false; }
            if(result.get(0).from().equals(edge.to())) result.addFirst(edge);
            if(result.get(0).from().equals(source)) return result.size();
        }
        return -1;
    }

    private Set<Production> intersectProductions(Person source, Person target){
        Set<Production> unionSet = new HashSet<>(source.getProductions());
        unionSet.retainAll(target.getProductions());
        return unionSet;
    }


    //================================================
    //Drivers for reading dataset
    //================================================
    private boolean baconIsAdded = false;
    private BaconReader br;


    public void readData(String filePath, int amount, String bacon){
        if(baconIsAdded) throw new IllegalArgumentException("Only one bacon allowed");

        try {
            init(filePath, amount + persons.size(), bacon);
        } catch (IOException e){
            System.out.println("no file found");
        }
        if(!baconIsAdded) throw new IllegalArgumentException(bacon + " not found");
    }

    public void readData(String filePath, int amount){
        try{
            init(filePath, amount+persons.size(), null);
        } catch (IOException e){
            System.out.println("no file found");
        }
    }

    public void readData(String filePath, String bacon){
        readData(filePath, Integer.MAX_VALUE-persons.size(), bacon);
    }

    public void readData(String filePath) {
        readData(filePath, Integer.MAX_VALUE-persons.size());
    }

    //========================================
    //Extracting info from dataset
    //======================================
    /**
     * Init Graph reads actors/actresses from dataset and stores them as Person-objects  persons list.
     * @param filePath filePath to imdb Actors/Actresses dataset.
     * @param numberOfPersons amount of persons to extract.
     * @param bacon string name of bacon.
     */
    private void init(String filePath, int numberOfPersons, String bacon) throws IOException{
        Person currentPerson;
        br = new BaconReader(filePath);
        boolean isBacon;
        while(persons.size()<numberOfPersons){
            br.move();
            br.getCurrent().toString();
            isBacon = !baconIsAdded && br.getCurrent().text.equals(bacon);
            if(persons.size() < numberOfPersons - 1 || persons.size() < numberOfPersons && baconIsAdded || isBacon || bacon == null){
                currentPerson = extractPerson();
                persons.put(currentPerson.getName(), currentPerson);
                if(isBacon) {
                    this.bacon = currentPerson;
                    baconIsAdded = true;
                }
            }
            if(br.getNext() == null) break; //eof
        }
        br.close();
    }

    private Person extractPerson() throws IOException {
        if (br.getCurrent().type != BaconReader.PartType.NAME) {
            throw new IllegalArgumentException("Expected NAME, found " + br.getCurrent().type);
        }
        if (br.getNext() == null) {
            throw new NullPointerException("Found NAME followed by null at: " + br.getCurrent());
        }

        Person newPerson = new Person(br.getCurrent().text);

        while (br.getNext().type != BaconReader.PartType.NAME) {
            br.move();
            if (br.getCurrent().type == BaconReader.PartType.TITLE) {
                Production production = extractProduction();
                personsInProduction.putIfAbsent(production, new LinkedList<>());
                personsInProduction.get(production).addFirst(newPerson);
                newPerson.addProduction(production);
            }
            if (br.getNext() == null) break;
        }
        return newPerson;
    }

    private Production extractProduction() throws IOException{
        if(br.getCurrent().type != BaconReader.PartType.TITLE){
            throw new IllegalArgumentException("Expected TITLE, found " + br.getCurrent().type);
        }
        String title, year = "", id = "";
        title = br.getCurrent().text;

        while(br.getNext().type != BaconReader.PartType.TITLE && br.getNext().type != BaconReader.PartType.NAME) {
            br.move();
            if (br.getCurrent().type == BaconReader.PartType.YEAR) {
                year = br.getCurrent().text;
            } else if (br.getCurrent().type == BaconReader.PartType.ID) {
                id = br.getCurrent().text;
            }
            if(br.getNext() == null) break;
        }
        return new Production(title,year,id);
    }

    public boolean hasBacon(){
        return baconIsAdded;
    }

}
