import fileReading.Loader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoaderTest {
    //small files
    private static final int actorsSize = 39279;
    private static final int actressesSize = 76936;
    private static final int totalNumberOfProductions = 667559;
    private static final String actorsFilePath = "test/actors_short.list";
    private static final String actressesFilePath = "test/actresses_short.list";


    @Test
    public void testNumberOfPersons(){
        Loader loader = new Loader(actorsFilePath, actressesFilePath, actorsSize + actressesSize);
        assertEquals(actorsSize+actressesSize, Loader.getPersons().size());
    }

}
