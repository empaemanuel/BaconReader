package fileReading;

import graph.Person;
import graph.Production;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts all actors, actresses and respective movies from the dataset using the fileReading.BaconReader class.
 * Depends on class Structure to store and implement the extracted data.
 */
public class Loader implements Runnable {
    /**
     * is set to true when it has completed reading files and has data ready to be collected.
     */
    public static boolean hasLoadedData;

    public static List<Person> getPersons(){
        return persons;
    }

    /**
     * List representation of persons.
     */
    private static List<Person> persons;

    /**
     * Starts extraction and loading of the dataset in two separate threads and returns execution to caller.
     */
    public static void load(String filePath1, String filePath2, int numberOfPersons){
        try {
            hasLoadedData = false;
            persons = new ArrayList<>(numberOfPersons);
            Loader file1Loader = new Loader(filePath1);
            Loader file2Loader = new Loader(filePath2);
            Thread t1 = new Thread(file2Loader, "filePath1");
            Thread t2 = new Thread(file1Loader, "filePath2");
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        hasLoadedData = true;
    }

    /**
     * Bacon reader is a scanner and tokenizer used to extract actors/actresses and movies/productions from the file.
     */
    private final BaconReader br;

    /**
     * Constructs a loader instance of a single imdb actors file.
     * @param filePath directory location of the file.
     * @throws IOException if file not found
     */
    private Loader(String filePath) throws IOException {
        br = new BaconReader(filePath);
    }


    @Override
    public void run() {
        load();
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * driver method for running through the file and extracting all data.
     * calls add() after each person including it's productions has been extracted for storing the data.
     */
    private void load() {
        try {
            br.move();
            while (br.getCurrent() != null) {
                Person person = new Person(br.getCurrent().text);
                add(person);
                br.move();
                List<Production> productions = extractProductions();
                person.setProductions(productions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void add(Person p){
        persons.add(p);
    }

    /**
     * Goes through all productions of the current person and returns a list of those productions when
     * bacon reader is on new person or at the end of the file.
     * @return List of String representations of movie titles.
     * @throws IOException required by the bacon reader.
     */
    private List<Production> extractProductions() throws IOException{
        BaconReader.Part p;

        List<Production> tmp = new ArrayList<>(20);
        do {
            if (br.getCurrent().type == BaconReader.PartType.NAME) {
                System.out.println(br.getCurrent());
                throw new IllegalArgumentException("Found NAME where it wasn't expected");
            }
            Production production = extractProduction();
            tmp.add(production);
            p = br.getCurrent();
        } while (p != null && p.type == BaconReader.PartType.TITLE);
        return tmp;
    }

    /**
     * Extracts a single production and returns when baconreader is on a new title, person or end of file.
     * @return String representation of production.
     * @throws IOException required by bacon reader.
     */
    private Production extractProduction() throws IOException {
        String title = "", year = "", id = "";
        BaconReader.Part p = br.getCurrent();

        while (p != null && p.type != BaconReader.PartType.NAME) {
            if (p.type == BaconReader.PartType.TITLE) {
                if(!title.isEmpty()) break;
                title = p.text;
            } else if (p.type == BaconReader.PartType.YEAR) {
                year = p.text;
            } else if (p.type == BaconReader.PartType.ID) {
                id = p.text;
            }
            br.move();
            p = br.getCurrent();
        }
        return new Production(title , year , id);
    }

}
