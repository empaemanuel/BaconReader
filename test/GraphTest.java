import fileReading.Loader;
import graph.Graph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GraphTest {
    //full files
//    private static final int actorsSize = 2672685;
//    private static final int actressesSize = 1495997;
//    private static final int totalNumberOfProductions = 3610770;
//    private static final String actorsFilePath = "actors.list";
//    private static final String actressesFilePath = "actresses.list";
//

    //small files
    private static final int actorsSize = 39279;
    private static final int actressesSize = 76936;
    private static final int totalNumberOfProductions = 667559;
    private static final String actorsFilePath = "test/actors_short.list";
    private static final String actressesFilePath = "test/actresses_short.list";


    @BeforeAll
    public static void testBuild(){
        Loader.load(actorsFilePath, actressesFilePath,actorsSize+actressesSize);
    }

    private Graph g;
    @Test
    public void testSetSourceSuccess(){
        g = new Graph(Loader.getPersons());
        g.setSource("Bacon, Kevin (II)");
        assertEquals("Bacon, Kevin (II)", g.getSource());
    }

    @Test
    public void testSetTargetSuccess(){
        g = new Graph(Loader.getPersons());
        g.setTarget("Bacon, Kevin (II)");
        assertEquals("Bacon, Kevin (II)", g.getTarget());
    }

    @Test
    public void testSearch(){
        g = new Graph(Loader.getPersons());
        g.setSource("Bacon, Kevin (I)");
        g.setTarget("Hiromachi, Tokiko");
        g.findShortestPath();
    }



}
