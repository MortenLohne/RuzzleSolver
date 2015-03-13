import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


enum Language {
	ENGLISH
}

public class Dictionary {
	
	private final ArrayList<String> dictionary;
	private final Language language;
	
	/**
	 * Reads words from the given file into an ArrayList. Assumes each word is on a separate lines.
	 * @param fileName
	 * @param language Language of the dictionary
	 * @return
	 */
	
	public Dictionary(String fileName, Language language) {
		this.language = language;
		dictionary = new ArrayList<>();
		Scanner scanner;
		
		while (!new File(fileName).isFile()) {
			System.out.println("Error: Couldn't find dictionary file \"" + fileName + "\". Enter a different filename: ");
			scanner = new Scanner(System.in);
			fileName = scanner.nextLine();
		}
		
		try {
			scanner = new Scanner(new File(fileName));
			int words = 0;
			while (scanner.hasNextLine()) {
				dictionary.add(scanner.nextLine());
				words++;
			}
			System.out.println("Read " + words + " words from " + fileName);
			scanner.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error: Dictionary file \"" + fileName + "\" was found, but couldn't be opened. Exiting...");
			System.exit(0);
			return;
			
		}
	}
	
	/**
	 * Returns whether the ArrayList contains the key. Assumes the dictionary is sorted, and uses binary search
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		return this.contains(key, 0, dictionary.size() - 1);
	}
	/**
	 * Returns whether the ArrayList contains the key within the given indexes. Assumes the dictionary is sorted, and uses binary search
	 * @param key
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public boolean contains(String key, int fromIndex, int toIndex) {
		int indexDiff = toIndex - fromIndex;
		
		if (indexDiff == 0) {
			return dictionary.get(fromIndex).equals(key);
		}
		else if (indexDiff == 1) {
			return dictionary.get(fromIndex).equals(key) || dictionary.get(fromIndex + 1).equals(key);
		}
		else {
			int comparisonResult = dictionary.get(fromIndex + indexDiff / 2).compareTo(key);
			if (comparisonResult < 0) {
				return this.contains(key, fromIndex + indexDiff / 2, toIndex);				
			}
			else if (comparisonResult == 0) {
				return true;
			}
			else {
				return this.contains(key, fromIndex, fromIndex + indexDiff / 2);
			}	
		}		
	}
	
	/**
	 * Returns the value of a word, not counting double words etc //TODO
	 * @param word
	 * @return
	 */
	public int getPoints(String word) {
		int points = word.length() > 4 ? (word.length() - 4) * 5 : 0;
		if (this.contains(word)) {
			for (char letter : word.toCharArray()) {
				points = points + getLetterValue(letter, language);
			}
			return points;
		}
		else {
			return 0;
		}
	}
	/**
	 * Returns the value of a single letter in the language, when inside a valid word //TODO
	 * @param letter
	 * @return
	 */
	public static int getLetterValue(char letter, Language language) {
		switch (language) {
		case ENGLISH:
			switch (letter) {
			case 'a': return 1;
			case 'b': return 4;
			case 'c': return 4;
			case 'd': return 2;
			case 'e': return 1;
			case 'f': return 4;
			case 'g': return 3;
			case 'h': return 4;
			case 'i': return 1;
			case 'j': return 10;
			case 'k': return 5;
			case 'l': return 1;
			case 'm': return 3;
			case 'n': return 1;
			case 'o': return 1;
			case 'p': return 4;
			case 'q': return 10;
			case 'r': return 1;
			case 's': return 1;
			case 't': return 1;
			case 'u': return 2;
			case 'v': return 4;
			case 'w': return 4;
			case 'x': return 8;
			case 'y': return 4;
			case 'z': return 10;
			default: throw new IllegalArgumentException("Tried to find value of capital letter or otherwise invalid character");
			}
		default: throw new UnsupportedOperationException("Language not supported");
		}
	}
}
