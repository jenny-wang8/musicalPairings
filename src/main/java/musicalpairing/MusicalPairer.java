package musicalpairing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MusicalPairer {

	/*
	 * So the idea here is to take the lists and reduce the number of times we have to iterate through.
	 * My first pass is to get rid of anything that doesn't meet the 50 count minimum with the 
	 * idea that if the artist isn't in any list 50 times, it can't be part of a pairing 50 times.
	 * 
	 * Second is to iterate through the lines once more and only keep the popular artists from each line.
	 * Creating pairings from them and add them to a counting map to then determine the final list.
	 */
	public static void main(String[] args) {
		MusicalPairer pairer = new MusicalPairer();
		pairer.run();
	}

	private static final String INPUT_FILE_NAME = "input.txt";
	private static final String OUTPUT_FILE_NAME = "./output.csv";
	private static final int SIZE = 50;
	
	private final Map<String, Integer> artistCount = new HashMap<String, Integer>();
	private final List<Set<String>> artistLines = new ArrayList<Set<String>>();
	private final Map<Set<String>, Integer> pairingMap = new HashMap<Set<String>, Integer>();
	
	public void run() {
		long started = System.currentTimeMillis();
		//read the file & collect artist count
		try {
			readFile();
			retainPopularArtists();
			groupPairings();
			filterAndWriteFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("processing time " + (System.currentTimeMillis() - started));
	}
	
	private void readFile() throws Exception {
		String line;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(INPUT_FILE_NAME).getFile());
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while ((line = reader.readLine()) != null) {
				String[] artists = line.split(",");
				
				if (artists.length < 2) {
					//System.out.println("skipping line " + line);
					continue;
				}
				
				Set<String> artistSet = new HashSet<String>();
				for (String artist : artists) {
					int count = artistCount.getOrDefault(artist, 0) + 1;
					artistCount.put(artist, count);
					artistSet.add(artist);
				}
				
				artistLines.add(artistSet);
			}
			System.out.println(artistCount.size());
		}
	}
	
	private void retainPopularArtists() {
		List<String> a = new ArrayList<String>();
		/*
		 * get rid of anything under 50, idea is that
		 * if there's less than 50 artists, they can't
		 * be part of a pair
		 */
		Set<String> keys = new HashSet<String>();
		keys.addAll(artistCount.keySet());
		for (String key : keys) {
			if (artistCount.get(key) < SIZE) {
				artistCount.remove(key);
			} else {
				a.add(key);
			}
		}
		Collections.sort(a);
		for (String string : a) {
			System.out.println(string);
		}
		System.out.println("popular artist count: " + artistCount.keySet().size());
	}
	
	private void groupPairings() {
		/*
		 * Iterate through the lines again and just keep the artists
		 * that are in the popular set
		 */
		System.out.println("processing " + artistLines.size() + " lines in grouping");
		for (Set<String> artistSet : artistLines) {
			artistSet.retainAll(artistCount.keySet());
			if (artistSet.size() >= 2) {	//if the set is less an 2, can't be a pairing
				/*
				 * with the popular artists in the retained set
				 * create pairings and add them to the map
				 */
				List<String> aList = new ArrayList<String>();
				aList.addAll(artistSet);
				int counter = 0;
				
				for (String a1 : aList) {
					for (String a2 : aList.subList(counter, aList.size())) {
						if (!a1.equals(a2)) {	//the same artist isn't a pair
							Set<String> pairing = new HashSet<String>();
							pairing.add(a1);
							pairing.add(a2);
							
							int count = pairingMap.getOrDefault(pairing, 0) + 1;
							pairingMap.put(pairing, count);	
						}
					}
					counter++;
				}
			}
		}
		System.out.println("group pairings " + pairingMap.keySet().size());
	}
	
	private void filterAndWriteFile() throws Exception {
		int counter = 0;
		/*
		 * Keep only the ones that have more than 50
		 */
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_FILE_NAME)))) {
			for (Entry<Set<String>, Integer> entry : pairingMap.entrySet()) {
				if (entry.getValue() >= SIZE) {
					
					String delimiter = "";
					for (String key : entry.getKey()) {
						writer.write(delimiter);
						writer.write(key);
						delimiter = ",";
					}
					writer.newLine();
					counter++;
				}
			}
			writer.flush();
		}
		System.out.println(counter);
	}

}