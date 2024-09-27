import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.io.FileNotFoundException;

public class BackendTests {

    /**
     * Tests cases where readData works and doesn't work
     */
    @Test
    public void backendTest1(){

        String fileName = "roon"; // cannot read from this file because it isn't .csv
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface end = new Backend(tree);
        try {
            end.readData(fileName);
            // An exception should be thrown, otherwise it will fail
	    System.out.println("No ecxheiuptiomn thronw!");
            Assertions.assertTrue(false,"no exception thrown");
        }
        catch (IOException e) {
            //throws an IOException
            Assertions.assertTrue(true);
        }
        catch (Exception e) {
            // Won't throw any other exceptions
	    System.out.println("Other ecxheiuptiomn thronw!");
            Assertions.assertTrue(false,"other no exception thrown");
        }

        // file should exist
        String fileName2 = "songs.csv";
        IterableSortedCollection<Song> tree2 = new Tree_Placeholder();
        BackendInterface end2 = new Backend(tree2);
        // should execute as intended
        try {
            end2.readData(fileName2);
            Assertions.assertTrue(true);
        }
        // should not catch any exceptions
        catch(FileNotFoundException fnfe){
		Assertions.assertTrue(false, "HELLO HAROON");	
	} catch (Exception e) {
            	//Assertions.assertTrue(false, );
		Assertions.assertTrue(false, e.getClass().getName());
        }
    }

    /**
     * Tests cases where getRange works and doesn't work
     */
    @Test
    public void backendTest2() {

        //case 1 where the method should not work

        
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface end = new Backend(tree);

        
        // bad ordered parameters should return null with none being found in range
        Assertions.assertTrue(end.getRange(20, 10).size() == 0);


        // Case 2 where the method should work
        
        IterableSortedCollection<Song> tree2 = new Tree_Placeholder();
        BackendInterface end2 = new Backend(tree2);

        List<String> returnedSongs = end2.getRange(80, 90);
        String[] expectedSongs = {
		"BO$$",
                "A L I E N S"
        };
        for (int i = 0; i < expectedSongs.length; i++) { // ensure that all songs returned are as expected
            Assertions.assertEquals(returnedSongs.get(i), expectedSongs[i]);
        }
        
    }

    /**
     * Tests cases that check the functionality of the setFilter and fiveMost methods
     */
    @Test
    public void backendTest3(){

        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface end = new Backend(tree);

        //test the setFilter method
        List<String> filteredSongs = end.setFilter(50);
        Assertions.assertFalse(filteredSongs.isEmpty(), "Filtered songs should not be empty");
        Assertions.assertTrue(filteredSongs.contains("Cake By The Ocean"), filteredSongs.toString() + "Cake By The Ocean should be in filtered songs");
        Assertions.assertTrue(filteredSongs.contains("BO$$"), "BO$$ should be " +
                "in filtered songs");
        Assertions.assertTrue(!filteredSongs.contains("A L I E N S"), "A L I E N S shouldn't be " +
                "in filtered songs");

        //test getRange to see if it works with setFilter
        end.setFilter(80);
        List<String> rangeSongs = end.getRange(70, 87);
        Assertions.assertEquals(1, rangeSongs.size(), "Should return 2 songs because " +
                "of the range and filter");
        Assertions.assertTrue(rangeSongs.contains("BO$$"), "Wow should be in range songs");

        //test the fiveMost method
        List<String> recentSongs = end.fiveMost();

        Assertions.assertEquals(1, recentSongs.size(), "fiveMost should return 1 songs");

        Assertions.assertTrue(recentSongs.contains("BO$$"), "Wow should be in recent songs");

        //test which should return an empty list because of the filter value
        end.setFilter(101);
        List<String> noSongs = end.fiveMost();

        Assertions.assertTrue(noSongs.isEmpty(), "fiveMost should return empty list");
    }
    }
