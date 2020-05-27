package fileReading;

import graph.Person;
import graph.Production;
import presentation.Presentation;

import java.io.IOException;
import java.util.*;

/**
 * Extracts all actors, actresses and respective movies from the dataset using the fileReading.BaconReader class.
 */
public class Loader implements Runnable{
    /**
     * List representation of persons.
     */
    private List<Person> persons;

    /**
     * Returns list of persons, if running multiple threads, use getCopyOfPersons()
     * @return list of persons.
     */
    public List<Person> getPersons(){
        return persons;
    }

    /**
     * Should be used if running multiple threads.
     * @return new cloned list of persons
     */
    public List<Person> getCopyOfPersons(){
        return new ArrayList<>(persons);
    }

    public Loader(String actorsFilePath, String actressesFilePath, int numberOfActors, int numberOfActresses){
        this.actorsFilePath = actorsFilePath;
        this.actressesFilePath = actressesFilePath;
        this.numberOfActors = numberOfActors;
        this.numberOfActresses = numberOfActresses;
    }

    private String actorsFilePath, actressesFilePath;
    private int numberOfActors, numberOfActresses;

    @Override
    public void run() {
        load();
    }

    /**
     * The link is set up for updating the progress bar at the presentation class.
     */
    private Presentation presentation;
    public void setPresentation(Presentation presentation){
        this.presentation = presentation;
    }

    /**
     * Starts extraction and loading of the dataset in two separate threads and returns execution to caller.
     */
    public void load(){
        try {
            persons = new ArrayList<>(numberOfActors + numberOfActresses);
            FileLoader actorsLoader = new FileLoader(actorsFilePath, numberOfActors);
            FileLoader actressesLoader = new FileLoader(actressesFilePath, numberOfActresses);
            Thread t1 = new Thread(actressesLoader, "actresses");
            Thread t2 = new Thread(actorsLoader, "actors");
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
        System.out.println("Files successfully loaded");
    }

    /**
     * Adds person to the person list.
     * @param p
     */
    private synchronized void add(Person p){
        p.setIndex(persons.size());
        persons.add(p);
    }

    /***
     * This map is used for preventing duplicate instances of the same object.
     * There are 3610773 non-duplicate productions. The initial capacity is
     * first prime number higher than 1.25% of total amount for a healthy load factor without having
     * to rehash the table.
     */
    private HashMap<Production, Production> productions = new HashMap<>(4814365);
    private int productionsCount = 0;
    private synchronized Production getProduction(Production p){
        Production result = productions.putIfAbsent(p, p);
        if(result == null) {
            productionsCount++;
            return p;
        }
        return result;
    }

    public int getNumberOfProductions(){
        return productionsCount;
    }


    /**
     * Inner class to load one single file.
     */
    private class FileLoader implements Runnable{
        /**
         * Bacon reader is a scanner and tokenizer used to extract actors/actresses and movies/productions from the file.
         */
        private final BaconReader br;

        private int totalCount;

        private int count = 0;
        /**
         * Constructs a loader instance of a single imdb actors file.
         * @param filePath directory location of the file.
         * @throws IOException if file not found
         */
        private FileLoader(String filePath, int totalCount) throws IOException {
            br = new BaconReader(filePath);
            this.totalCount=totalCount;
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
                if(presentation != null) presentation.updateProgressLabel(Thread.currentThread().getName(), false);
                br.move();
                while (br.getCurrent() != null) {
                    Person person = new Person(br.getCurrent().text);
                    count++;
                    add(person);
                    br.move();
                    extractProductions(person);
                    if(presentation != null) presentation.updateProgressBar(count,totalCount, Thread.currentThread().getName());
                    //person.setProductions(productions);
                }
                if(presentation != null) presentation.updateProgressLabel(Thread.currentThread().getName(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * Goes through all productions of the current person and returns a list of those productions when
         * bacon reader is on new person or at the end of the file.
         * @return List of String representations of movie titles.
         * @throws IOException required by the bacon reader.
         */
        private List<Production> extractProductions(Person person) throws IOException{
            BaconReader.Part p;

            //List<Production> tmp = new ArrayList<>();
            do {
                if (br.getCurrent().type == BaconReader.PartType.NAME) {
                    System.out.println(br.getCurrent());
                    throw new IllegalArgumentException("Found NAME where it wasn't expected");
                }
                Production production = extractProduction();
                //person.addProduction(production);
              //  tmp.add(production);
                Production prod = getProduction(production);
                person.addProduction(prod);
                p = br.getCurrent();
            } while (p != null && p.type == BaconReader.PartType.TITLE);
            //return tmp;
            return null;
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
}
