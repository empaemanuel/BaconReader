import fileReading.Loader;
import graph.Graph;
import graph.Path;
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
    private static Loader loader;

    @BeforeAll
    public static void testBuild(){
        loader = new Loader(actorsFilePath, actressesFilePath,actorsSize, actressesSize);
        loader.load();
    }

    private Graph g;
    @Test
    public void testSetSource(){
        g = new Graph(loader.getPersons());
        g.setSource("Bacon, Kevin (II)");
        assertEquals("Bacon, Kevin (II)", g.getSource());
    }

    @Test
    public void testSetTarget(){
        g = new Graph(loader.getPersons());
        g.setTarget("Bacon, Kevin (II)");
        assertEquals("Bacon, Kevin (II)", g.getTarget());
    }

    @Test
    public void testConsistency(){
        String source = "Bacon, Kevin (I)";
        String target = "Hirotsu, Yukiko";
        g = new Graph(loader.getCopyOfPersons());
        g.setSource(source);
        g.setTarget(target);
        Path p = g.findShortestPath();
        assertEquals(3, p.getBaconNumber());
        assertEquals(source, p.getSource());
        assertEquals(target, p.getTarget());
    }

    @Test
    public void testSearchShortReflexive(){
        String source = "Hirotsu, Yukiko";
        String target = "Bacon, Kevin (I)";
        g = new Graph(loader.getCopyOfPersons());
        g.setSource(source);
        g.setTarget(target);
        Path p = g.findShortestPath();
        assertEquals(3, p.getBaconNumber());
        assertEquals(source, p.getSource());
        assertEquals(target, p.getTarget());
    }


    @Test
    public void testSearchLong(){
        String source = "Aalto, Tero (I)";
        String target = "Johansson, Marika (I)";
        g = new Graph(loader.getCopyOfPersons());
        g.setSource(source);
        g.setTarget(target);
        Path p = g.findShortestPath();
        assertEquals(source, p.getSource());
        assertEquals(target, p.getTarget());
        assertEquals(1, p.getBaconNumber());

        target = "Johansson, Frida (III)";
        g.setTarget(target);
        p = g.findShortestPath();
        assertEquals(source, p.getSource());
        assertEquals(target, p.getTarget());
        assertEquals(2, p.getBaconNumber());
        source = "Johansson, Marika (I)";
        g.setSource(source);
        p = g.findShortestPath();
        assertEquals(1, p.getBaconNumber());
        assertEquals(source, p.getSource());
        assertEquals(target, p.getTarget());
    }


    @Test
    public void testSearchLongB(){
        String source = "Aalto, Tero (I)";
        String target = "Johansson, Frida (III)";
        g = new Graph(loader.getCopyOfPersons());
        g.setSource(source);
        g.setTarget(target);
        Path p = g.findShortestPath();
        assertEquals(source, p.getSource());
        assertEquals(target, p.getTarget());
        assertEquals(2, p.getBaconNumber());
    }




}
