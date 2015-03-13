import java.util.Arrays;
import java.util.Optional;

public class LetterGrid {
	private final char[][] letters;
	private final boolean[][] isUsed;
	private final Integer BOARD_SIZE = Main.BOARD_SIZE;
	
	
	/**
	 * Creates a grid based on the specified char array
	 * @param letters
	 */
	public LetterGrid(char[][] letters) {
		System.out.println("Creating new letter grid");
		
		isUsed = new boolean[BOARD_SIZE][BOARD_SIZE];
		Arrays.fill(isUsed, new boolean[]{false, false, false, false});
		assert(isUsed[0][0] == false);
		if (letters.length == BOARD_SIZE && letters[0].length == BOARD_SIZE) {
			this.letters = letters.clone();
		}
		else {
			throw new IllegalArgumentException("Grid must have size " + BOARD_SIZE);
		}
	}
	/**
	 * Creates a grid using a reference to the specified letters and a deep copy of isUsed
	 * @param letters
	 * @param isUsed
	 */
	private LetterGrid(char[][] letters, boolean[][] isUsed) {
		this.letters = letters;
		this.isUsed = isUsed;
		// Main.printGrid(this);
	}
	
	/**
	 * Returns the character in that square, or Optional.empty is the character is used
	 * @param x
	 * @param y
	 * @return
	 */
	public Optional<Character> get(int x, int y) {
		if (isUsed[x][y]) {
			return Optional.empty();
		}
		else {
			return Optional.of(letters[x][y]);
		}
	}
	
	/**
	 * Returns a new LetterGrid, with a letter marked as used
	 * @param x
	 * @param y
	 * @return
	 */
	public LetterGrid use(int x, int y) {
		boolean[][]newIsUsed = deepClone(isUsed);
		// System.out.println("use(): this grid has " + getUnusedSquares(isUsed) + " unused squares.");
		newIsUsed[x][y] = true;
		// System.out.println("(" + x + ", " + y + ") is now used.");
		// System.out.println("use(): the new grid has " + getUnusedSquares(newIsUsed) + " unused squares.");
		return new LetterGrid(letters, newIsUsed);
	}
	
	public static int getUnusedSquares(boolean[][] isUsed) {
		int unusedSquares = 0;
		for (boolean[] array : isUsed) {
			for (boolean b : array) {
				if (!b) {
					unusedSquares++;
				}
			}
		}
		return unusedSquares;
	}
	public static boolean[][] deepClone(boolean[][] array) {
		boolean[][] newArray = new boolean[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				newArray[i][j] = array[i][j];
			}
		}
		return newArray;
	}
	
}
