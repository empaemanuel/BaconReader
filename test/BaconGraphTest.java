


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class BaconGraphTest {
    private static boolean setUpIsDone = false;
    private BaconGraph graph;
    private String fileA = "test/actorsTestWithBacon.list"; //10 persons
    private String fileB = "test/actorsTestWithoutBacon.list"; //21 persons
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
        assertEquals(5, graph.getNumberOfNodes());
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
        assertEquals(21, graph.getNumberOfNodes());
    }

    @Test
    public void testReadDataFullFile(){
        graph.readData(fileA);
        graph.readData(fileB);
        assertEquals(31, graph.getNumberOfNodes());
    }

    @Test
    public void testReadDuplicateValues(){
        graph.readData(fileA, 5);
        graph.readData(fileA);
        assertEquals(10, graph.getNumberOfNodes());
    }

    @Test
    public void testNrOfProductionsAndPersons(){
        graph.readData("test/actorsTestNumberOfProductions.list");
        assertEquals(26, graph.getNumberOfProductions());
    }

    @Test
    public void initGraph(){
        graph.readData(fileA, fileABacon);
        graph.readData(fileB);
    }

    @Test
    public void clearGraph(){
        graph.readData(fileA);
        assertTrue(graph.getNumberOfNodes()>0);
        graph.clear();
        assertEquals(0, graph.getNumberOfNodes());
    }


    public void runThrough(){//24s
        try{
            BaconReader br = new BaconReader("actors.list");
            br.move();
            while(true){
                br.move();
                if(br.getNext()==null) break;
            }
        } catch(Exception e) {
            System.out.println(e);
        }

    }

}
