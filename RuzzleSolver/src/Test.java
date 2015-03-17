import static org.junit.Assert.*;

import java.io.File;


public class Test {

	@org.junit.Test
	public void test() {
		checkAlphabetization();
	}
	public void checkAlphabetization() {

		Dictionary dictionary_EN = new Dictionary(new File("English.dictionary"));
		for (int i = 0; i < dictionary_EN.size() - 1; i++) {
			if (dictionary_EN.getWord(i).compareTo(dictionary_EN.getWord(i + 1)) >= 0) {
				System.out.println((dictionary_EN.getWord(i) + " comes before " + dictionary_EN.getWord(i + 1) + ", but shouldn't according to the comparator."));
				fail(dictionary_EN.getWord(i) + " comes before " + dictionary_EN.getWord(i + 1) + ", but shouldn't according to the comparator.");
			}
		}
		
		Dictionary dictionary_NO = new Dictionary(new File("Norwegian.dictionary"));
		for (int i = 0; i < dictionary_NO.size() - 1; i++) {
			if (dictionary_NO.getWord(i).compareTo(dictionary_NO.getWord(i + 1)) >= 0) {
				System.out.println((dictionary_NO.getWord(i) + " comes before " + dictionary_NO.getWord(i + 1) + ", but shouldn't according to the comparator."));
				fail(dictionary_NO.getWord(i) + " comes before " + dictionary_NO.getWord(i + 1) + ", but shouldn't according to the comparator.");
			}
		}
	}

}
