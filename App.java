import java.util.Scanner;

/**
 * Main entry point for running the Song Tracker app
 */
public class App {
    public static void main(String[] args) {
	IterableSortedCollection<Song> tree = new IterableRedBlackTree<Song>(); 
	BackendInterface backend = new Backend(tree);
	Scanner in = new Scanner(System.in);
	FrontendInterface frontend = new Frontend(in,backend);

	System.out.println("Welcome to iSongly");
	System.out.println("==================");
	
	frontend.runCommandLoop();

	System.out.println();
	System.out.println("====================");
	System.out.println("Thanks, and Goodbye.");
    }
}
