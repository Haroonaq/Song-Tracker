
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

      } catch (UnsupportedOperationException e) {
        System.out.println(
            "Required Operation [\"" + userInput + "\"] is Unimplemented. Try another Operation.");
        continue;
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

  @Override
  public void getSongs() {
    System.out.println("iSongly GET SONGS");
    while (true) {
      try {

        int minEnergy;
        while (true) { // Separate loops for each value to ensure safe input.
          System.out
              .println("Please enter the MINIMUM amount of energy you would like to experience:");
          System.out.print(">> ");
          try {
            minEnergy = this.in.nextInt();
            this.in.nextLine();
          } catch (InputMismatchException e) {
            System.out.println("Invalid Value. Please try again.");
            continue;
          }
          break;
        }

        int maxEnergy;
        while (true) { // Separate loops for each value to ensure safe input.
          System.out
              .println("Please enter the MAXIMUM amount of energy you would like to experience:");
          System.out.print(">> ");
          try {
            maxEnergy = this.in.nextInt();
            this.in.nextLine();
          } catch (InputMismatchException e) {
            System.out.println("Invalid Value. Please try again.");
            continue;
          }
          break;
        }

        // If Max is less than min, resets to accept min again.
        if (maxEnergy < minEnergy) {
          System.out
              .println("Maximum Energy cannot be less than the Minimum Energy. Please try again.");
          continue;
        }

        this.displaySongTitles(this.backend.getRange(minEnergy, maxEnergy));
        break;

      } catch (Exception e) {
        System.out.println("Unexpected Exception encountered. Returning to Main Menu.");
        break;
      }
    }
  }

  @Override
  public void setFilter() {

    System.out.println("iSongly SET DANCEABILITY FILTER");

    while (true) {

      try {

        int danceabilityMin;
        while (true) {
          System.out.println("Please enter the MINIMUM Danceability you would like to experience:");
          System.out.print(">> ");
          try {
            danceabilityMin = this.in.nextInt();
            this.in.nextLine();
          } catch (InputMismatchException e) {
            System.out.println("Invalid Value. Please try again.");
            continue;
          }
          break;
        }

        this.displaySongTitles(this.backend.setFilter(danceabilityMin));
        break;

      } catch (Exception e) {
        System.out.println("Unexpected Exception encountered. Returning to Main Menu.");
        break;
      }

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
