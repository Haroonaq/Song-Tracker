import java.util.Scanner;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;


/**
 * Frontend implementation to provide the user with a clean, console based, menu driven user
 * interface when interacting with the application.
 */
public class Frontend implements FrontendInterface {

  private Scanner in;
  private BackendInterface backend;

  /**
   * Helper method for displaying a list of song titles to the user.
   * 
   * @param songTitles The list of song titles to display.
   */
  private void displaySongTitles(List<String> songTitles) {
    System.out.println("\nAvailable Songs:");

    int cnt = 1;
    for (String title : songTitles)
      System.out.println("[" + (cnt++) + "] - " + title);
  }

  /**
   * Creates a new Frontend Instance that reads inputs from the Scanner and operates on the backend
   * interface provided.
   * 
   * @param in      The Scanner reference to read inputs from.
   * @param backend The Backend reference to operate on user inputs.
   */
  public Frontend(Scanner in, BackendInterface backend) {
    this.in = in;
    this.backend = backend;
  }

  @Override
  public void runCommandLoop() {

    while (true) { // Breaks out of loop when user enters [q] / [Q]
      displayMainMenu();

      System.out.print(">> ");

      String userInput = "";
      try {

        userInput = in.nextLine();
        System.out.println();

        if (userInput == null || userInput.length() == 0) {
          System.out.println("Invalid input. Try again.");
          continue;
        }

        userInput = userInput.toUpperCase();

        if (userInput.charAt(0) == 'Q')
          break; // Quitting

        switch (userInput.charAt(0)) {

          case 'L':
            loadFile();
            continue;

          case 'G':
            getSongs();
            continue;

          case 'F':
            setFilter();
            continue;

          case 'D':
            displayTopFive();
            continue;

          default:
            System.out.println("Invalid input. Try again.");
            continue;
        }

      } catch (Exception e) {
        System.out.println("Unexpected Error Encountered. Try again.");
        continue;
      }

    }

    System.out.println("Quitting iSongly...");
  }

  @Override
  public void displayMainMenu() {
    System.out.println("");
    System.out.println("iSongly MAIN MENU");
    System.out.println("[L] Load a File");
    System.out.println("[G] Get Songs");
    System.out.println("[F] Set Filter");
    System.out.println("[D] Display Top 5");
    System.out.println("[Q] Quit");
  }

  @Override
  public void loadFile() {
    System.out.println("iSongly LOAD SONG FILE");

    while (true) {

      System.out.println("Please enter the filepath:");
      System.out.print(">> ");

      String userIn = "";

      try {
        userIn = this.in.nextLine();
        if (userIn == null || userIn.length() == 0) {
          // Filepath cannot be null or empty.
          System.out.println("Invalid Filepath. Please try again.");
          continue;
        }

        this.backend.readData(userIn.trim());
        System.out.println("Load Successful!");
        break;

      } catch (IOException e) {
        System.out.println("Provided file could not be found or read. Please try again.");
        continue;
      } catch (Exception e) {
        System.out.println("Unexpected Exception encountered. Returning to Main Menu.");
        break;
      }

    }
  }


  /**
   * Private Helper method that accepts an integer input from the user in a safe manner,
   * re-prompting the user for an input if necessary.
   * 
   * @param prompt The prompt to display to the user when retrieving value.
   * @return The user's value.
   */
  private int getSafeIntegerValue(String prompt, String errorMessage) {
    int userIn;
    while (true) {

      // Prompt the User for an input.
      System.out.println(prompt);
      System.out.print(">> ");

      // Try and accept the user's input.
      try {
        userIn = this.in.nextInt();

        // As Scanner.nextInt() doesn't progress the cursor to the next line, we call
        // Scanner.nextLine().
        this.in.nextLine();
      } catch (InputMismatchException e) {

        // If the user's input was not a valid integer,
        // we print the appropriate error message.
        System.out.println(errorMessage);

        // And now we effectively go back to the beginning of the loop to re-prompt the user.
        continue;
      }

      // If no errors occured when inputting the integer, we can safely exit the loop.
      break;
    }

    // Finally returning the user's input.
    return userIn;
  }


  @Override
  public void getSongs() {
    System.out.println("iSongly GET SONGS");

    // Main loop for handling user inputs and processing output.
    while (true) {
      try {

        // Calling this.getSafeIntegerValue to ensure safe input handling.
        int minEnergy = this.getSafeIntegerValue(
            "Please enter the MINIMUM amount of energy you would like to experience:",
            "Invalid Integer Value. Please try again.");

        int maxEnergy = this.getSafeIntegerValue(
            "Please enter the MAXIMUM amount of energy you would like to experience:",
            "Invalid Integer Value. Please try again.");

        // If Max is less than min, resets to accept min again.
        if (maxEnergy < minEnergy) {
          System.out
              .println("Maximum Energy cannot be less than the Minimum Energy. Please try again.");

          // Go back to beginning and re-prompt user.
          continue;
        }

        this.displaySongTitles(this.backend.getRange(minEnergy, maxEnergy));

        // We can now succesfully exit the loop.
        break;

      } catch (Exception e) {
        System.out.println("Unexpected Exception encountered. Returning to Main Menu.");

        // If an unexpected exception is encountered, we exit and go back to the main menu.
        break;
      }
    }
  }

  @Override
  public void setFilter() {

    System.out.println("iSongly SET DANCEABILITY FILTER");
    try {

      // Calling this.getSafeIntegerValue to ensure safe input handling.
      int danceabilityMin = this.getSafeIntegerValue(
          "Please enter the MINIMUM Danceability you would like to experience:",
          "Invalid Integer Value. Please try again.");

      this.displaySongTitles(this.backend.setFilter(danceabilityMin));
      return; // Returning back to main menu.

    } catch (Exception e) {
      System.out.println("Unexpected Exception encountered. Returning to Main Menu.");
      return; // Returning back to main menu.
    }

  }

  @Override
  public void displayTopFive() {

    System.out.println("iSongly DISPLAY TOP 5");
    try {
      this.displaySongTitles(this.backend.fiveMost());
    } catch (Exception e) {
      System.out.println("Unexpected Exception encountered. Returning to Main Menu.");
    }

  }

}
