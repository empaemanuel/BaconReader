


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class BaconGraphTest {
    private static boolean setUpIsDone = false;
    private BaconGraph graph;
    private String fileA = "test/TestActorsA.list"; //10 persons
    private String fileB = "test/TestActorsB.list"; //21 persons
    private String fileC = "test/TestActorsC.list";
    private String fileActors = "database/actors.list";
    private String fileActresses = "database/actresses.list";
    private String fileABacon = "Bacon, Kevin (I)";
    private String fileBBacon = "Kowdrin, Antonio";

    @BeforeEach
    public void setup(){
        //if(setUpIsDone) return;
        graph = new BaconGraph();
        //setUpIsDone = true;
    }

    @Test
    public void testReadDataExceptions() {
        assertThrows(IllegalArgumentException.class, () -> {
            graph.readData(fileB, fileABacon);
        });

        graph.readData(fileB, fileBBacon);
        assertThrows(IllegalArgumentException.class, () -> {
            graph.readData(fileA, fileABacon);
        });
    }

    @Test
    public void testReadDataPartOfFile() {
        graph.readData(fileB, 3);
        graph.readData(fileA, 2);
        assertEquals(5, graph.getNumberOfPersons());
    }

    @Test
    public void testReadDataWithBaconInRange(){
        graph.readData(fileB, fileBBacon);
        assertTrue(graph.hasBacon());
    }

    @Test
    public void testReadDataWithBaconOutOfRange(){
        graph.readData(fileA, 3, fileABacon);
        assertTrue(graph.hasBacon());
    }

    @Test
    public void testReadDataWithRangeGreaterThanFileLength(){
        graph.readData(fileB, 50);
        assertEquals(21, graph.getNumberOfPersons());
    }

    @Test
    public void testReadDataFullFile(){
        graph.readData(fileA);
        graph.readData(fileB);
        assertEquals(31, graph.getNumberOfPersons());
    }

    @Test
    public void testReadDuplicateValues(){
        graph.readData(fileA, 5);
        graph.readData(fileA);
        assertEquals(10, graph.getNumberOfPersons());
    }

    @Test
    public void testNrOfProductionsAndPersons(){
        graph.readData(fileC);
        assertEquals(26, graph.getNumberOfProductions());
    }

    @Test
    public void testNrOfProductionsPerPerson(){
        graph.readData(fileB);
        assertEquals(128, graph.getNumberOfProductions("Kowitz, Peter"));
        assertEquals(1, graph.getNumberOfProductions("Kowl, Ben"));
    }

    @Test
    public void testNrOfPersonsPerProduction(){
        graph.readData(fileC);
        assertEquals(2,graph.getNumberOfPersons("Kidz Care", "2013", "(#1.1)"));
    }

    @Test
    public void initGraph(){
        graph.readData(fileA, fileABacon);
        graph.readData(fileB);
    }

    @Test
    public void clearGraph(){
        graph.readData(fileA);
        assertTrue(graph.getNumberOfPersons()>0);
        graph.clear();
        assertEquals(0, graph.getNumberOfPersons());
    }

    @Test
    public void findBaconNumber(){
        graph.readData(fileC, "Kowitz, Peter");
        assertEquals(0, graph.findBaconNumber("Kowitz, Peter"));
        assertEquals(1, graph.findBaconNumber("Kowdrysh, Sebastian"));
        assertEquals(2, graph.findBaconNumber("Kowdiar, Kumar"));
    }

    @Test
    public void findBaconNumberWithoutBacon(){
        graph.readData(fileC);
        assertThrows(IllegalCallerException.class, ()->{
            graph.findBaconNumber("Kowitz, Peter");
        });

    }

    @Test
    public void findBaconNumberForMissingPerson(){
        graph.readData(fileC, "Kowitz, Peter");
        assertEquals(-1, graph.findBaconNumber("abc, efg"));
    }


}
