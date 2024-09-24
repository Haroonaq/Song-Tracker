import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Scanner;

/**
 * Tester class for the Frontend Implementation.
 */
public class FrontendTests {

  /**
   * Tests the Load File option in the Frontend Implementation. Uses the Backend Placeholder for
   * testing.
   * 
   * @return True if all tests pass, False otherwise.
   */
  @Test
  public void frontendTest1() {
    // Test Load File
    {
      Scanner sc = new Scanner("testFilepath\n");
      TextUITester uiTester = new TextUITester("");

      IterableSortedCollection<Song> tree = new Tree_Placeholder();
      BackendInterface backend = new Backend_Placeholder(tree);
      FrontendInterface frontend = new Frontend(sc, backend);

      try {
        frontend.loadFile();
      } catch (Exception e) {
        Assertions.assertTrue(false, "Unexpected Exception Encountered.");
      }
      sc.close();

      // Can't check much else...
    }
  }

  /**
   * Tests the Get Songs and set Filter options in the Frontend Implementation. Uses the Backend
   * Placeholder for testing.
   * 
   * @return True if all tests pass, False otherwise.
   */
  @Test
  public void frontendTest2() {
    // Test Get Songs & Filter

    { // Test GET with 1 song out.
      Scanner sc = new Scanner("2\n16\n");
      TextUITester uiTester = new TextUITester("");

      IterableSortedCollection<Song> tree = new Tree_Placeholder();
      BackendInterface backend = new Backend_Placeholder(tree);
      FrontendInterface frontend = new Frontend(sc, backend);

      try {
        frontend.getSongs();
      } catch (Exception e) {
        Assertions.assertTrue(false, "Unexpected Exception Encountered.");
      }

      String out = uiTester.checkOutput();
      if (!out.contains("Cake By The Ocean")
          || (out.contains("BO$$") || out.contains("A L I E N S")))
        Assertions.assertTrue(false, "Displaying Invalid Songs.");
    }

    { // Test GET with 2 songs out.
      Scanner sc = new Scanner("1\n20\n");
      TextUITester uiTester = new TextUITester("");

      IterableSortedCollection<Song> tree = new Tree_Placeholder();
      BackendInterface backend = new Backend_Placeholder(tree);
      FrontendInterface frontend = new Frontend(sc, backend);

      try {
        frontend.getSongs();
      } catch (Exception e) {
        Assertions.assertTrue(false, "Unexpected Exception Encountered.");
      }

      String out = uiTester.checkOutput();
      if (!(out.contains("Cake By The Ocean") && out.contains("BO$$"))
          || (out.contains("A L I E N S")))
        Assertions.assertTrue(false, "Displaying Invalid Songs.");
    }

    { // Test FILTER.
      Scanner sc = new Scanner("8\n");
      TextUITester uiTester = new TextUITester("");

      IterableSortedCollection<Song> tree = new Tree_Placeholder();
      BackendInterface backend = new Backend_Placeholder(tree);
      FrontendInterface frontend = new Frontend(sc, backend);

      try {
        frontend.setFilter();
      } catch (Exception e) {
        Assertions.assertTrue(false, "Unexpected Exception Encountered.");
      }

      String out = uiTester.checkOutput();
      if (!(out.contains("Cake By The Ocean") && out.contains("BO$$")
          && out.contains("A L I E N S")))
        // With given placeholder, all songs are returned regardless of input.
        Assertions.assertTrue(false, "Displaying Invalid Songs.");
    }

    // Not much else to test...

  }

  /**
   * Tests the Display Top 5 option in the Frontend Implementation (Including after setting filter
   * and energy). Uses the Backend Placeholder for testing.
   * 
   * @return True if all tests pass, False otherwise.
   */
  @Test
  public void frontendTest3() {
    // Test Display Top 5

    { // Test Display 5.
      Scanner sc = new Scanner("");
      TextUITester uiTester = new TextUITester("");

      IterableSortedCollection<Song> tree = new Tree_Placeholder();
      BackendInterface backend = new Backend_Placeholder(tree);
      FrontendInterface frontend = new Frontend(sc, backend);

      try {
        frontend.displayTopFive();
      } catch (Exception e) {
        Assertions.assertTrue(false, "Unexpected Exception Encountered.");
      }

      String out = uiTester.checkOutput();
      if (!(out.contains("Cake By The Ocean") && out.contains("BO$$")
          && out.contains("A L I E N S")))
        Assertions.assertTrue(false, "Displaying Invalid Songs.");
    }

  }
}
