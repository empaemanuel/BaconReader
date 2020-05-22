import fileReading.Loader;
import graph.Person;
import graph.Production;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoaderTest {
    //small files
//    private static final int actorsSize = 39279;
//    private static final int actressesSize = 76936;
//    private static final int totalNumberOfProductions = 667559;
//    private static final String actorsFilePath = "test/actors_short.list";
//    private static final String actressesFilePath = "test/actresses_short.list";

    //test files
    private static final int actorsSize = 5;
    private static final int actressesSize = 3;
    private static final int totalNumberOfProductions = 24;
    private static final String actorsFilePath = "test/actors_test.list";
    private static final String actressesFilePath = "test/actresses_test.list";


    @Test
    public void testNumberOfPersons(){
        Loader loader = new Loader(actorsFilePath, actressesFilePath, actorsSize, actressesSize);
        loader.load();
        assertEquals(actorsSize+actressesSize, loader.getPersons().size());
        assertEquals(totalNumberOfProductions, loader.getNumberOfProductions());
    }

    @Test
    public void testActorInfo(){
        Loader loader = new Loader(actorsFilePath, actressesFilePath, actorsSize, actressesSize);
        loader.load();
        assertTrue(loader.getPersons().contains(new Person("Bacon, Kevin (II)")));
        int i = loader.getPersons().indexOf(new Person("Bacon, Kevin (II)"));
        assertTrue(loader.getPersons().get(i).getProductions().contains(new Production("Behind the Scene", "2011", "")));
    }

    @Test
    public void testActressInfo(){
        Loader loader = new Loader(actorsFilePath, actressesFilePath, actorsSize, actressesSize);
        loader.load();
        assertTrue(loader.getPersons().contains(new Person("Aabel, Hauk (II)")));
        int i = loader.getPersons().indexOf(new Person("Aabel, Hauk (II)"));
        assertTrue(loader.getPersons().get(i).getProductions().contains(new Production("Galilei", "1977", "")));
    }

//    @Test
//    public void loadFullFiles(){
//        Loader loader = new Loader("actors.list", "actresses.list", 2672685, 1495997);
//        loader.load();
//        assertEquals(3610773, loader.getNumberOfProductions());
//        assertEquals(4168682, loader.getPersons().size());
//        assertTrue(loader.getPersons().contains(new Person("Bacon, Kevin (II)")));
//    }

}
