import java.util.Scanner;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;


/**
 * Frontend implementation to provide the user with a clean, console based, menu driven user
 * interface when interacting with the application.
 */
public class Frontend implements FrontendInterface {

  /**
   * Scanner Reference from which to accept the inputs from the user.
   */
  private Scanner in;

  /**
   * Backend Implementation to use.
   */
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

  /**
   * Main method for calling the relevant commands. Repeatedly gives the user an opportunity to
   * issue new commands until they select Q to quit. Uses the scanner passed to the constructor to
   * read user input. <br>
   * </br>
   * Valid commands are as follows:<br>
   * </br>
   * 
   * - [L] Load a File <br>
   * </br>
   * - [G] Get Songs <br>
   * </br>
   * - [F] Set Filter <br>
   * </br>
   * - [D] Display Top 5 <br>
   * </br>
   * - [Q] Quit.
   * 
   * Inputs are also case-insensitive. i.e. 'q' / 'Q' mean Quit.
   * 
   */
  @Override
  public void runCommandLoop() {

    // Main loop when dealing with user input.
    while (true) { // Breaks out of loop when user enters [q] / [Q]
      displayMainMenu();

      System.out.print(">> ");

      String userInput = "";

      // Try block to catch unexpected exceptions when accepting user input, and calling relevant
      // methods.
      try {

        userInput = in.nextLine();
        System.out.println();

        if (userInput == null || userInput.length() == 0) {
          // User did not input any value.
          System.out.println(
              "Provided Input is empty. Please input one of the specified options as a single character based on the character specified in '[]' beside each option.");
          continue;
        }

        // Ensuring Case-insensitivity.
        userInput = userInput.toUpperCase();

        if (userInput.charAt(0) == 'Q')
          break; // Quitting

        // Switch Block for calling relevant method.
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
            System.out.println(
                "Provided Input is not one of the provided options. Please input one of the specified options as a single character based on the character specified in '[]' beside each option.");
            continue;
        }

      } catch (Exception e) {
        // Unexpected Exception Encountered.

        System.out.println("Unexpected Error Encountered. Please try again.");

        // Going back to beginning of main loop, to re-prompt user.
        continue;
      }

    }

    System.out.println("Quitting iSongly...");
  }

  /**
   * Displays the menu of command options to the user. Giving the user the instructions of entering
   * L, G, F, D, or Q (case insensitive) to load a file, get songs, set filter, display the top
   * five, or quit respectively.
   * 
   * Displays the options in the following format: <br>
   * </br>
   * <code> [Q] Quit </code><br>
   * </br>
   * With the value in '[]' representing the key to press for the specified option.
   */
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


  /**
   * Provides text-based user interface for prompting the user to select the csv file that they
   * would like to load, provides feedback about whether this is successful vs any errors are
   * encountered. [L]oad Song File
   *
   * When the user enters a valid filename, the file with that name should be loaded. Uses the
   * scanner passed to the constructor to read user input and the backend passed to the constructor
   * to load the file provided by the user. If the backend indicates a problem with finding or
   * reading the file by throwing an IOException, a message is displayed to the user, and they will
   * be asked to enter a new filename.
   */
  @Override
  public void loadFile() {
    System.out.println("iSongly LOAD SONG FILE");

    // Main Loop for dealing with user inputs.
    while (true) { // Only repeats if the user's input is invalid.

      // Prompting User.
      System.out.println("Please enter the filepath:");
      System.out.print(">> ");

      String inputFilepath;

      try {
        // Accepting user input.
        inputFilepath = this.in.nextLine();

        if (inputFilepath == null || inputFilepath.length() == 0) {
          // Filepath cannot be null or empty.
          System.out.println("Provided filepath is empty. Please try again.");

          // Go back to beginning.
          continue;
        }

        this.backend.readData(inputFilepath.trim());
        System.out.println("Load Successful!");
        break;

      } catch (IOException e) { // Thrown by Backend when reading data from the provided CSV File.
        // Only Called if the provided file does not exist or could not be read.

        // Displaying Error Message.
        System.out.println("Provided file could not be found or read. Please try again.");

        // Go back to beginning.
        continue;
      } catch (Exception e) { // Unexpected Exception was Encountered.

        // Displaying Error Message.
        System.out.println("Unexpected Exception encountered. Returning to Main Menu.");

        // Go back to main menu.
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
    // Main Loop for handling User inputs.
    while (true) { // Only repeats if user's input is invalid.

      // Prompt the User for an input.
      System.out.println(prompt);
      System.out.print(">> ");

      // Try and accept the user's input.
      try {
        userIn = this.in.nextInt();

        // As Scanner.nextInt() doesn't progress the cursor to the next line, we call
        // Scanner.nextLine().
        this.in.nextLine();
      } catch (InputMismatchException e) { // Thrown by nextInt if user's input is not an integer.

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


  /**
   * Provides text-based user interface and error handling for retrieving a list of song titles that
   * are sorted by Energy. The user should be given the opportunity to optionally specify a minimum
   * and/or maximum Energy to limit the number of songs displayed to that range. [G]et Songs by
   * Energy
   * 
   * If any error is detected with the user's input eg. Minimum Energy is greater than the Maximum
   * Energy, then the user is prompted again.
   * 
   * If the user enters an invalid integer for one of the inputs, they are only re-prompted for one
   * of those inputs, rather than having to re-enter the minimum again.
   */
  @Override
  public void getSongs() {
    System.out.println("iSongly GET SONGS");

    // Main loop for handling user inputs and processing output.
    while (true) { // Only Repeats if the provided Inputs are invalid.
      // eg. Min > Max

      try {

        // Calling this.getSafeIntegerValue to ensure safe input handling.
        int minEnergy = this.getSafeIntegerValue(
            "Please enter the MINIMUM amount of energy you would like to experience:",
            "Provided Energy Value is an Invalid Number. We can only accept Whole Number inputs. Please try again.");

        // Calling this.getSafeIntegerValue to ensure safe input handling.
        int maxEnergy = this.getSafeIntegerValue(
            "Please enter the MAXIMUM amount of energy you would like to experience:",
            "Provided Energy Value is an Invalid Number. We can only accept Whole Number inputs. Please try again.");

        // If Max is less than min, resets to accept min again.
        if (maxEnergy < minEnergy) {

          // Error Message.
          System.out
              .println("Maximum Energy cannot be less than the Minimum Energy. Please try again.");

          // Go back to beginning and re-prompt user.
          continue;
        }

        this.displaySongTitles(this.backend.getRange(minEnergy, maxEnergy));

        // We can now succesfully exit the loop.
        break;

      } catch (Exception e) { // Unexpected Exception was Encountered.

        // Displaying Error Message.
        System.out.println("Unexpected Exception encountered. Returning to Main Menu.");

        // If an unexpected exception is encountered, we exit and go back to the main menu.
        break;
      }
    }
  }

  /**
   * Provides text-based user interface and error handling for setting a filter threshold. This and
   * future requests to retrieve songs will will only return the titles of songs that are larger
   * than the user specified Danceability. The user should also be able to clear any previously
   * specified filters. [F]ilter Songs by Danceability
   *
   * When the user enters only a single number, that number should be used as the new filter
   * threshold. Uses the scanner passed to the constructor to read user input and the backend passed
   * to the constructor to set the filters provided by the user and retrieve songs that maths the
   * filter criteria.
   */
  @Override
  public void setFilter() {

    // Note: We do not use a loop here as all invalid cases are ensured by getSafeIntegerValue, with
    // no additional invalid inputs aside from that.

    System.out.println("iSongly SET DANCEABILITY FILTER");
    try {

      // Calling this.getSafeIntegerValue to ensure safe input handling.
      int danceabilityMin = this.getSafeIntegerValue(
          "Please enter the MINIMUM Danceability you would like to experience:",
          "Provided Energy Value is an Invalid Number. We can only accept Whole Number inputs. Please try again.");

      this.displaySongTitles(this.backend.setFilter(danceabilityMin));
      return; // Returning back to main menu.

    } catch (Exception e) { // Unexpected Exception was encountered.

      // Displaying Error Message.
      System.out.println("Unexpected Exception encountered. Returning to Main Menu.");

      return; // Returning back to main menu.
    }

  }

  /**
   * Displays the titles of up to five of the most Recent songs within the previously set Energy
   * range and larger than the specified Danceability. If there are no such songs, then this method
   * should indicate that and recommend that the user change their current range or filter settings.
   * [D]isplay five most Recent
   *
   * The user should not need to enter any input when running this command. Uses the backend passed
   * to the constructor to retrieve the list of up to five songs.
   */
  @Override
  public void displayTopFive() {

    System.out.println("iSongly DISPLAY TOP 5");
    try {

      this.displaySongTitles(this.backend.fiveMost());

      return; // Returning back to main menu.

    } catch (Exception e) { // Unexpected Exception Encountered.

      // Displaying Error Message.
      System.out.println("Unexpected Exception encountered. Returning to Main Menu.");

      return; // Returning back to main menu.
    }

  }

}
