import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

     /**
     * Method designed to test basic functionality of app, see's if it can load a file, read it
     * and output desired songs
     */
    @Test
    public void integrationTest1(){

        //call the methods within the app
        TextUITester tester = new TextUITester("L\nsongs.csv\nG\n98\n100\nQ\n");

        //instantiate the tree and backend
        IterableSortedCollection<Song> tree = new IterableRedBlackTree<Song>();
        BackendInterface backend = new Backend(tree);

        //instantiate the frontend
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
        
        //The file should load properly and get songs should run properly
        //The songs in the get range include Hello but not Cake By The Ocean

        Assertions.assertTrue(output.contains("Load Successful!"), "CSV file should be loaded successfully");
        Assertions.assertTrue(output.contains("Available Songs"), "Filtered songs should be displayed");
        Assertions.assertTrue(output.contains("Hello"), "The song Hello should be available");
        Assertions.assertFalse(output.contains("Cake By The Ocean"), "Song does not meet the filter");

    }

    /**
     * Method designed to see if the get songs method correctly works with the set filter
     * method to output the correct results
     */
    @Test
    public void integrationTest2(){

        //call the methods within the app
        TextUITester tester = new TextUITester("L\nsongs.csv\nG\n96\n100\nF\n50\nQ\n");

        //instantiate the tree and backend
        IterableSortedCollection<Song> tree = new IterableRedBlackTree<Song>();
        BackendInterface backend = new Backend(tree);

        //instantiate the frontend
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
	        
        //These songs below meet both get range and set filters criteria
        
        Assertions.assertTrue(output.contains("Don't Stop the Party"), "The song Don't Stop the Party should be available");
        Assertions.assertTrue(output.contains("Hello"), "The song Hello should be available");
        Assertions.assertTrue(output.contains("Pom Poms"), "The song Pom Poms should be available");
        
        //Bad Liar does not meet the set filter criteria 
        Assertions.assertFalse(output.contains("Bad Liar"), "Song does not meet the filter");

    }


    /**
     * Method designed to see if the display five most recent songs method works after loading
     * the file and getting a range of songs
     */
    @Test
    public void integrationTest3(){

        //call the methods within the app
        TextUITester tester = new TextUITester("L\nsongs.csv\nG\n95\n100\nD\nQ\n");

        //instantiate the tree and backend
        IterableSortedCollection<Song> tree = new IterableRedBlackTree<Song>();
        BackendInterface backend = new Backend(tree);

        //instantiate the frontend
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();
        
        //5 of the most recent songs should be displayed given the earlier get range criteria
        Assertions.assertTrue(output.contains("Blown"), "The song Blown should be available");
        Assertions.assertTrue(output.contains("Booty"), "The song Booty should be available");
        Assertions.assertTrue(output.contains("She Looks So Perfect"), "The song She Looks So Perfect should be available");
        Assertions.assertTrue(output.contains("How Ya Doin'?"), "The song How Ya Doin'? should be available");
        Assertions.assertTrue(output.contains("Rock N Roll"), "The song Rock N Roll should be available");
        
        //This is not a recent song
        Assertions.assertFalse(output.contains("Bad Liar"), "Not apart of the 5 most recent songs");
        
    }


    /**
     * Another Method designed to see if the get songs method correctly works with the set filter
     * however the order in which these two methods are called are now different. Set filter is
     * called before get songs and that should not affect the outcome
     */
    @Test
    public void integrationTest4(){

        //call the methods within the app
        TextUITester tester = new TextUITester("L\nsongs.csv\nF\n95\nG\n0\n100\nQ\n");

        //instantiate the tree and backend
        IterableSortedCollection<Song> tree = new IterableRedBlackTree<Song>();
        BackendInterface backend = new Backend(tree);

        //instantiate the frontend
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = tester.checkOutput();

        //Should contain three songs Bad Liar, Drip, and Anaconda
        //The other songs though they meet the get songs criteria, they don't meet the set filter criteria

        Assertions.assertTrue(output.contains("Bad Liar"), "The song Bad Liar should be available");
        Assertions.assertTrue(output.contains("Drip"), "The song Drip should be available");
        Assertions. assertTrue(output.contains("Anaconda"), "The song Pom Poms should be available");
        Assertions.assertFalse(output.contains("Rock N Roll"), "Song is not in the set filter range");
        Assertions.assertFalse(output.contains("Hello"), "Song is not in the set filter range");

    }
 }
