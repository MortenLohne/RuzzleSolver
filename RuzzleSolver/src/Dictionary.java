import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


enum Language {
	ENGLISH,
	NORWEGIAN
}

public class Dictionary {
	
	private final ArrayList<String> dictionary;
	private final Language language;
	
	public static Dictionary chooseDictionary () {
		
		ArrayList<File> dictionaryFiles = new ArrayList<>();
		File currentDir = new File(System.getProperty("user.dir"));
		
		// Look through all files in current directory for dictionary files
		for (String fileName : currentDir.list()) {
			if (fileName.endsWith(".dictionary")) {
				File file = new File(fileName);
				if (file.isFile()) {
					dictionaryFiles.add(file);
				}
			}
		}
		if (dictionaryFiles.size() > 0) {
			System.out.println("Found " + dictionaryFiles.size() + " dictionary " + (dictionaryFiles.size() == 1 ? " file " : "files " + "in current folder."));
			
			// If only one dictionary was found, use that
			if (dictionaryFiles.size() == 1) {
				
				System.out.println("Using " + dictionaryFiles.get(0).getName() + ": ");
				Language dictionaryLanguage;
				/*
				for (Language language : Language.values()) {
					if (dictionaryFiles.get(0).getName().toUpperCase().startsWith(language.name())) {
						return new Dictionary(dictionaryFiles.get(0).getName(), language);						
					}
				}
				*/
				Language language = chooseLanguage(dictionaryFiles.get(0));
				return new Dictionary(dictionaryFiles.get(0), language);

				
			}
			
			// If more than one is found, let the user choose among the found dictionaries
			else {
				System.out.println("Choose a dictionary by number: ");
				for (int i = 0; i < dictionaryFiles.size(); i++) {
					System.out.println((i + 1) + ": " + dictionaryFiles.get(i).getName());
				}
				Scanner scanner = new Scanner(System.in);
				try {
					File chosenFile = dictionaryFiles.get(Integer.parseInt(scanner.nextLine()) - 1);
					Language language = chooseLanguage(chosenFile);
					return new Dictionary(chosenFile, language);					
				}
				catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Out of range. Trying again: ");
					return chooseDictionary();
				}
			}
		}
		// TODO: Fallback option for missing dictionary files
		else {
			System.out.println("No .dictionary files were found. Exiting...");
			System.exit(0);
			return null;
		}
	}
	
	public static Language chooseLanguage (File dictionary) {
		
		// Attempt to detect language based on the name of the file
		for (Language language : Language.values()) {
			if (dictionary.getName().toUpperCase().startsWith(language.name())) {
				System.out.println("Detected language " + language.name().toLowerCase());
				return language;						
			}
		}
		
		// If language could not be detected
		System.out.println("Choose a language for the dictionary by number. This will determine how many points each letter gives ");
		Scanner scanner = new Scanner(System.in);
		for (Language language : Language.values()) {
			System.out.println((language.ordinal() + 1) + ": " + language.name());
		}
		try {
			Language chosenLanguage = Language.values()[(Integer.parseInt(scanner.nextLine()) - 1)];
			return chosenLanguage;
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Language out of range. Try again: ");
			return chooseLanguage(dictionary);
		}
		
	}
	
	public Dictionary (File file) {
		this(file, chooseLanguage(file));
	}
	
	/**
	 * Reads words from the given file into an ArrayList. Assumes each word is on a separate lines.
	 * @param file
	 * @param language Language of the dictionary
	 * @return
	 */
	
	public Dictionary(File file, Language language) {
		this.language = language;
		System.out.println("Creating dictionary of language " + language);
		dictionary = new ArrayList<>();
		Scanner scanner;
		
		/*
		while (!new File(fileName).isFile()) {
			System.out.println("Error: Couldn't find dictionary file \"" + fileName + "\". Enter a different filename: ");
			scanner = new Scanner(System.in);
			fileName = scanner.nextLine();
		}
		*/
		
		try {
			scanner = new Scanner(file);
			System.out.println("Reading dictionary file...");
			int words = 0;
			while (scanner.hasNextLine()) {
				dictionary.add(scanner.nextLine());
				words++;
			}
			System.out.println("Read " + words + " words from " + file.getName());
			scanner.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error: Dictionary file \"" + file.getName() + "\" was found, but couldn't be opened. Exiting...");
			System.exit(0);
			return;
			
		}
	}
	
	public int size() {
		return dictionary.size();
	}
	public String getWord(int i) {
		return dictionary.get(i);
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
	 * Returns the value of a single letter in the language, when inside a valid word //TODO add norwegian
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
		case NORWEGIAN:
			switch (letter) {
			case 'a': return 1;
			case 'b': return 4;
			case 'c': return 10;
			case 'd': return 1;
			case 'e': return 1;
			case 'f': return 2;
			case 'g': return 4;
			case 'h': return 3;
			case 'i': return 2;
			case 'j': return 4;
			case 'k': return 3;
			case 'l': return 2;
			case 'm': return 2;
			case 'n': return 1;
			case 'o': return 3;
			case 'p': return 4;
			case 'q': return 10;
			case 'r': return 1;
			case 's': return 1;
			case 't': return 1;
			case 'u': return 4;
			case 'v': return 5;
			case 'w': return 10;
			case 'x': return 10;
			case 'y': return 8;
			case 'z': return 10;
			case 'æ': return 8;
			case 'ø': return 4;
			case 'å': return 4;
			default: throw new IllegalArgumentException("Tried to find value of capital letter or otherwise invalid character");
			}
		default: throw new UnsupportedOperationException("Language not supported");
		}
	}
}
