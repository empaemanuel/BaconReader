import java.io.IOException;
import java.util.*;
public class BaconGraph {

    /**
     * List of Person-objects with bacon at index 0.
     */
    private Set<Person> persons = new HashSet<>();
    //private List<Person> personsB = new LinkedList<>();
    private Map<Production, Person> productions = new HashMap<>();
    private boolean baconIsAdded = false;
    private BaconReader br;
    BaconGraph(){};


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

    public int getNumberOfNodes(){
        return persons.size();
    }

    public int getNumberOfProductions(){
        return productions.size();
    }

    public void clear(){
        persons.clear();
    }

    public boolean hasBacon(){
        return baconIsAdded;
    }





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
                persons.add(currentPerson);
                if(isBacon) baconIsAdded = true;
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
                productions.put(production, newPerson);
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

}
