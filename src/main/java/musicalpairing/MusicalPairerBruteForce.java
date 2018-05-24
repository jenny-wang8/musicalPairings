package musicalpairing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MusicalPairerBruteForce {

	/*
	 * So the first iteration is just to pair up each of the artists in the lists to 
	 * figure out which ones are paired.
	 * 
	 * This is the first iteration of the program and isn't optimized.
	 */
	public static void main(String[] args) {
		MusicalPairerBruteForce pairer = new MusicalPairerBruteForce();
		pairer.run();
	}

	private static final String INPUT_FILE_NAME = "input.txt";
	private static final int SIZE = 50;
	
	private final Map<Set<String>, Integer> pairingMap = new HashMap<Set<String>, Integer>();
	
	public void run() {
		long started = System.currentTimeMillis();
		//read the file & collect artist count
		try {
			readFile();
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
					continue;
				}
				
				int counter = 0;
				List<String> aList = Arrays.asList(artists);
				
				for (String a1 : aList) {
					for (String a2 : aList.subList(counter, aList.size())) {
						if (!a1.equals(a2)) {
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
			System.out.println(pairingMap.size());
			
			int counter = 0;
			int myCounter = 0;
			Set<String> a = new HashSet<String>();
			
			for (Entry<Set<String>, Integer> entry : pairingMap.entrySet()) {
				
				if (entry.getValue() >= SIZE) {
					a.addAll(entry.getKey());
					counter++;
				}
			}
			System.out.println(counter);
			System.out.println(myCounter);
		}
	}
	
}