import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Backend implements BackendInterface {

    private IterableSortedCollection<Song> tree;

    private ArrayList<Song> rangeSongs;

    private Integer danceabilityThreshold;


    //public Backend(IterableSortedCollection<Song> tree)
    // Your constructor must have the signature above. All methods below must
    // use the provided tree to store, sort, and iterate through songs. This
    // will enable you to create some tests that use the placeholder tree, and
    // others that make use of a working tree.
    public Backend(IterableSortedCollection<Song> tree) {

        this.tree = tree;

        Iterator<Song> iterator = tree.iterator();
        this.rangeSongs = new ArrayList<>();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            rangeSongs.add(song);
        }
    }


    /**
     * Loads data from the .csv file referenced by filename.  You can rely
     * on the exact headers found in the provided songs.csv, but you should
     * not rely on them always being presented in this order or on there
     * not being additional columns describing other song qualities.
     * After reading songs from the file, the songs are inserted into
     * the tree passed to the constructor.
     *
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    @Override
    public void readData(String filename) throws IOException {

        try {
            File data = new File(filename);
            Scanner scnr = new Scanner(data);

            if (scnr.hasNextLine()) {
                scnr.nextLine();
            }
	    scnr.nextLine();

            while (scnr.hasNextLine()) {
                String lineData = scnr.nextLine();
                String[] fields = splitCSV(lineData);

                int year = Integer.parseInt(fields[3]);
                int bpm = Integer.parseInt(fields[4]);
                int energy = Integer.parseInt(fields[5]);
                int danceability = Integer.parseInt(fields[6]);
                int loudness = Integer.parseInt(fields[7]);
                int liveness = Integer.parseInt(fields[8]);


                Song song = new Song(fields[0], fields[1], fields[2], year, bpm,
                        energy, danceability, loudness, liveness);

                tree.insert(song);
            }
            scnr.close();
        } catch (FileNotFoundException e) {
            throw new IOException("Could not read file " + e.getMessage());
        }
    }
    /**
    * A custom method to split CSV lines, accounting for commas inside quoted fields.
    */
private String[] splitCSV(String line) {
    boolean insideQuote = false;
    StringBuilder field = new StringBuilder();
    ArrayList<String> fields = new ArrayList<>();

    for (int i = 0; i < line.length(); i++) {
        char currentChar = line.charAt(i);

        if (currentChar == '"') {
            // Toggle whether we're inside a quoted string
            insideQuote = !insideQuote;
        } else if (currentChar == ',' && !insideQuote) {
            // If it is  a comma and we're not inside quotes, it marks the end of a field
            fields.add(field.toString().trim());
            field.setLength(0); // Reset the field builder
        } else {
            // append the character to the current field
            field.append(currentChar);
        }
    }

    
    fields.add(field.toString().trim());

    // convert the list to an array 
    return fields.toArray(new String[0]);
}

     

    /**
     * Retrieves a list of song titles from the tree passed to the contructor.
     * The songs should be ordered by the songs' Energy, and that fall within
     * the specified range of Energy values.  This Energy range will
     * also be used by future calls to the setFilter and getmost Recent methods.
     * <p>
     * If a Danceability filter has been set using the setFilter method
     * below, then only songs that pass that filter should be included in the
     * list of titles returned by this method.
     * <p>≈
     * When null is passed as either the low or high argument to this method,
     * that end of the range is understood to be unbounded.  For example, a null
     * high argument means that there is no maximum Energy to include in
     * the returned list.
     *
     * @param low  is the minimum Energy of songs in the returned list
     * @param high is the maximum Energy of songs in the returned list
     * @return List of titles for all songs from low to high, or an empty
     * list when no such songs have been loaded
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        ArrayList<Song> filteredSongs = new ArrayList<>();
        Iterator<Song> iterator = tree.iterator();
        
        if(low == null){
            low = Integer.MIN_VALUE;
        }
        if(high == null){
            high = Integer.MAX_VALUE;
        }
        
        List<String> result = new ArrayList<>(); 
        // Return an empty list for invalid range
        if (low > high) {
            return new ArrayList<>();
        }

        while (iterator.hasNext()) {
            Song song = iterator.next();
            int energy = song.getEnergy();
		
            // check if song's energy is in the range
	    if ((energy >= low) && (energy <= high)) {
                // apply danceability filter if set
                if (this.danceabilityThreshold == null || song.getDanceability() > this.danceabilityThreshold) {
			filteredSongs.add(song);
                }
            }
        }

     
        this.rangeSongs = filteredSongs;

        // Sort filteredSongs by Energy
        filteredSongs.sort(Comparator.comparing(Song::getEnergy));

        // create result list of song titles
        for (Song song : filteredSongs) {
            result.add(song.getTitle());
        }


        return result;
    }

    /* 
     * Retrieves a list of song titles that have a Danceability that is
     * larger than the specified threshold.  Similar to the getRange
     * method: this list of song titles should be ordered by the songs'
     * Energy, and should only include songs that fall within the specified
     * range of Energy values that was established by the most recent call
     * to getRange.  If getRange has not previously been called, then no low
     * or high Energy bound should be used.  The filter set by this method
     * will be used by future calls to the getRange and getmost Recent methods.
     * <p>
     * When null is passed as the threshold to this method, then no Danceability
     * threshold should be used.  This effectively clears the filter.
     *
     * @param threshold filters returned song titles to only include songs that
     *                  have a Danceability that is larger than this threshold.
     * @return List of titles for songs that meet this filter requirement, or
     * an empty list when no such songs have been loaded
     */
    @Override
    public List<String> setFilter(Integer threshold) {
        this.danceabilityThreshold = threshold;

      
        if (this.rangeSongs == null || this.rangeSongs.isEmpty()) {
            return new ArrayList<>();
        }

        //list to store the filtered results
        List<Song> filteredSongs = new ArrayList<>();

        for (Song song : this.rangeSongs) {
            // Include songs with Danceability greater than the threshold or all songs if the
            // threshold is null
            if (threshold == null || song.getDanceability() > threshold) {
                filteredSongs.add(song);
            }
        }

    
        Collections.sort(filteredSongs, Comparator.comparing(Song::getEnergy));

        // convert the sorted Song objects to a list of titles
        List<String> result = new ArrayList<>();

        for (Song song : filteredSongs) {
            result.add(song.getTitle());
        }

        return result;
    }

    /**
     * This method returns a list of song titles representing the five
     * most Recent songs that both fall within any attribute range specified
     * by the most recent call to getRange, and conform to any filter set by
     * the most recent call to setFilter.  The order of the song titles in this
     * returned list is up to you.
     * <p>
     * If fewer than five such songs exist, return all of them.  And return an
     * empty list when there are no such songs.
     *
     * @return List of five most Recent song titles
     */
    @Override
    public List<String> fiveMost() {
        List<String> result = new ArrayList<>();

       
        if (this.rangeSongs == null || this.rangeSongs.isEmpty()) {
            return result;
        }

        // list to hold filtered songs that meet the danceability threshold
        List<Song> filteredSongs = new ArrayList<>();

        for (Song song : this.rangeSongs) {
            // Check if the song meets the danceability filter
            if (this.danceabilityThreshold == null || song.getDanceability() >= this.danceabilityThreshold) {
                filteredSongs.add(song);
            }
        }

        // If there are fewer than five songs, return all of them
        if (filteredSongs.size() < 5) {
            for (Song song : filteredSongs) {
                result.add(song.getTitle());
            }
            return result;
        }

        //sort the filtered songs by year in descending order to get
        // the most recent songs

        Collections.sort(filteredSongs, Comparator.comparing(Song::getYear).reversed());

        // Add the top 5 or fewer song titles to the result list
        for (int i = 0; i < 5; i++) {
            result.add(filteredSongs.get(i).getTitle());
        }

        return result;
    }
}


