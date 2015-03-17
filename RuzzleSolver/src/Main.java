import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;


public class Main {
	
	public static final int MAX_WORD_LENGTH = 8;
	public static final Integer BOARD_SIZE = 4;
	
	
	public static void main(String[] args) {
		// Dictionary dictionary = new Dictionary("dictionary_EN.txt", Language.ENGLISH);
		Dictionary dictionary = Dictionary.chooseDictionary();
		
		LetterGrid grid = readGrid();
		
		long startTime = System.currentTimeMillis();
		
		// Data structure that stores all the words that give points
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		
		//
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				getWordsStartingFromSquare(dictionary, grid, words, i, j, "");				
			}
		}
		System.out.println("\nFound " + words.size() + " words worth a total of " + getAvailablePoints(words) + " points in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.\n");
		// System.out.println("Words:\n" + words);
		
		printSortedWords(words, 1);
		
	}
	
	/**
	 * Reads and returns a grid from standard input, consisting of 16 (if BOARD_SIZE = 4) letters separated by whitespace.
	 * @return
	 */
	public static LetterGrid readGrid() {
		Scanner scanner = new Scanner(System.in);
		char[][] charGrid = new char[BOARD_SIZE][BOARD_SIZE]; 
		try {
			System.out.println("Enter the grid:");
			for (int i = 0; i < BOARD_SIZE; i++) {
				 for (int j = 0; j < BOARD_SIZE; j++) {
					 String next = scanner.next().toLowerCase();
					 if (next.length() == 1 && Character.isLetter(next.charAt(0))) {
						 charGrid[i][j] = next.charAt(0);
					 }
					 else {
						 System.out.println("Error: Wrong input. Start over: ");
						 return readGrid();
					 }
				 }
			}
			LetterGrid letterGrid = new LetterGrid(charGrid);
			printGrid(letterGrid);
			return letterGrid;
		}
		finally {
			scanner.close();
		}
	}
	
	/**
	 * Checks every possible word combination in the grid starting from the given cell.
	 * @param grid
	 * @param wordsSoFar
	 * @param x x-coordinate of the cell to check from
	 * @param y
	 * @param lastWord
	 */
	public static void getWordsStartingFromSquare (Dictionary dictionary, LetterGrid grid, HashMap<String, Integer> wordsSoFar, int x, int y, String lastWord) {
		
		if (lastWord.length() >= MAX_WORD_LENGTH) {
			if (dictionary.getPoints(lastWord) > 0) {
				wordsSoFar.put(lastWord, dictionary.getPoints(lastWord));				
			}
			return;		
		}
		
		// This method should never be called with a used coordinate
		assert(grid.get(x, y).isPresent());
		
		// Mark the current character as used. 
		char thisChar = grid.get(x, y).get();
		grid = grid.use(x, y);
		
		String currentWord = lastWord + thisChar;
		int points = dictionary.getPoints(currentWord);
		
		if (points > 0) {
			System.out.println("Word " + currentWord + " is worth " + points + (points > 0 ? " points" : " point"));
			wordsSoFar.put(currentWord, points);
		}
		
		// For every neighbouring square, 
		for (Coordinate cord : getNeighbours(grid, x, y)) {
			getWordsStartingFromSquare(dictionary, grid, wordsSoFar, cord.x, cord.y, currentWord);
		}
		return;
	}
	
	/**
	 * Returns all the unused, neighbouring cells of the given cell
	 * @param grid
	 * @param x
	 * @param y
	 * @return
	 */
	public static ArrayList<Coordinate> getNeighbours(LetterGrid grid, int x, int y) {
		ArrayList<Coordinate> coords = new ArrayList<>();
		
		// System.out.println("Getting neighbours for (" + x + ", " + y + "):");
		
		for (int i : new int[]{-1, 0, 1}) {
			for (int j : new int[]{-1, 0, 1}) {
				addIfValid(coords, x + i, y + j, grid);
				
			}
		}
		return coords;
	}
	/**
	 * Checks if the coordinate is valid, and if the letter is unused. If both are true, it adds the coordinate to the coords array
	 * @param coords The mutable array to store the coordinates in
	 * @param x x-coordinate to check
	 * @param y y-coordinate to check
	 * @param grid
	 */
	public static void addIfValid(ArrayList<Coordinate> coords, int x, int y, LetterGrid grid) {
		try {
			if (grid.get(x, y).isPresent()) {
				coords.add(new Coordinate(x, y));	
			}
			else {
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			
		}
	}
	
	public static void printSortedWords(HashMap<String, Integer> words, int cutoff) {
		int wordsPrinted = 0;
		while (words.size() > 0) {
			boolean isFirst = true;
			String best = null;
			for (String word : words.keySet()) {
				if (isFirst) {
					best = word;
					isFirst = false;
				}
				else if (words.get(word)> words.get(best)) {
					best = word;
				}
			}
			if (words.get(best) < cutoff) {
				return;
			}
			System.out.printf("%-" + (MAX_WORD_LENGTH + 2) + "s", best + ": ");
			System.out.printf("%-5d", words.get(best));
			wordsPrinted++;
			if (wordsPrinted % 5 == 0) {
				System.out.println("");
			}
			words.remove(best);
		}
		return;
	}
	
	public static int getAvailablePoints(HashMap<String, Integer> words) {
		int sum = 0;
		for (String word : words.keySet()) {
			sum = sum + words.get(word);
		}
		return sum;
	}
	
	/**
	 * Prints a human-readable grid to standard output
	 * @param grid
	 */
	public static void printGrid(LetterGrid grid) {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (grid.get(i, j).isPresent()) {
					System.out.print(grid.get(i, j).get() + "  ");
				}
				else {
					System.out.print("[] ");
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * Immutable object to represent two integers. Cause Java.
	 * @author Morten
	 *
	 */
	public static class Coordinate {
		final int x;
		final int y;
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
