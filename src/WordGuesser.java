import java.util.*;
/**
 * This program represents a hangman game that intends to maximize guesses by delaying a word choice until necessary.
 * This class represents the state of the game and does not take input from users.
 */
public class WordGuesser {
    private Set<Character> guesses;
    private Set<String> words;
    private int guessesLeft;
    private String currentPattern;
    /**
     * Initializes the state of the game, storing values that are a certain length from a dictionary
     * @param words dictionary of words
     * @param length of the words that user guesses
     * @param max amount of guesses
     * @throws IllegalArgumentException if length is less than 1 or if max is less than 0
     */
    public WordGuesser(Collection<String> words, int length, int max) throws IllegalArgumentException {
        if (length < 1 || max < 0) throw new IllegalArgumentException();
        guessesLeft = max;
        guesses = new TreeSet<Character>();
        this.words = new TreeSet<String>();
        for (String word : words) {
            if (word.length() == length) {
                this.words.add(word);
            }
        }
        currentPattern = "";
        for (int i = 0; i < length; i++) {
            currentPattern += '-';
        }
    }
    /**
     * Getter for the set of words that could be the chosen word
     * @return current words that could be made with the current pattern
     */
    public Set<String> words() {
        return words;
    }
    /**
     * Getter for the number of guesses a player has left
     * @return int guesses
     */
    public int guessesLeft() {
        return guessesLeft;
    }
    /**
     * Getter for the set of letters that the player has already guessed
     * @return set of letters guessed
     */
    public Set<Character> guesses() {
        return guesses;
    }
    /**
     * Getter for the current pattern with the guesses that have been made
     * @return String representation of pattern. Letters that have not been guessed yet are represented by "_";
     * @throws IllegalStateException if set of words is empty
     */
    public String pattern() throws IllegalStateException {
        if (words.isEmpty()) {
            throw new IllegalStateException();
        }
        String pattern = "";
        for (int i = 0; i < currentPattern.length(); i++) {
            pattern += currentPattern.charAt(i) + " ";
        }
        return pattern.substring(0, pattern.length() - 1);
    }
    /**
     * Records the next guess of the user and decrements the number of guesses if the character guessed did not change
     * the current pattern
     * @param guess the character the player guessed
     * @return number of characters replaced
     * @throws IllegalStateException if number guesses is less than 1 or if set of words is empty
     * @throws IllegalArgumentException if character was already guessed
     */
    public int record(char guess) throws IllegalArgumentException, IllegalStateException {
        if (words().isEmpty() || guessesLeft < 1) throw new IllegalStateException();
        if (guesses.contains(guess)) throw new IllegalArgumentException();
        guesses.add(guess);
        updatePattern(guess);
        int count = 0;
        for (int i = 0; i < currentPattern.length(); i++) {
            if (currentPattern.charAt(i) == guess) {
                count++;
            }
        }
        if (count == 0) {
            guessesLeft--;
        }
        return count;
    }
    /**
     * Creates and chooses a pattern from the current pattern and set of words that could be the chosen word given a
     * guess
     * @param guess char user guess
     */
    private void updatePattern(char guess) {
        char[] charPattern;
        Map<String, Set<String>> wordChoice = new HashMap<String, Set<String>>();
        //checks the available words
        for (String word : words) {
            //converts currentPattern to char array and back to String
            charPattern = currentPattern.toCharArray();
            for (int i = 0; i < word.length(); i++) {
                if (guess == word.charAt(i)) {
                    charPattern[i] = guess;
                }
            }
            String pattern = new String(charPattern);
            if (!wordChoice.containsKey(pattern)) {
                wordChoice.put(pattern, new HashSet<String>());
            }
            wordChoice.get(pattern).add(word);
        }
        choose(wordChoice);
    }
    /**
     * Updates the currentPattern instance given the patterns that could be made from the available words
     * @param wordChoice Map of patterns and set of words belonging to the pattern
     */
    private void choose(Map<String, Set<String>> wordChoice) {
        int max = -1;
        String newPattern = null;
        //iterates over the available patterns and finds the pattern with the most words
        for (String pattern : wordChoice.keySet()) {
            if (wordChoice.get(pattern).size() > max) {
                max = wordChoice.get(pattern).size();
                newPattern = pattern;
            }
        }
        //second iteration to remove the unneeded words from the available words set
        for (String pattern : wordChoice.keySet()) {
            if (!pattern.equals(newPattern)) {
                for (String word : wordChoice.get(pattern)) {
                    words.remove(word);
                }
            }
        }
        currentPattern = newPattern;
    }
}