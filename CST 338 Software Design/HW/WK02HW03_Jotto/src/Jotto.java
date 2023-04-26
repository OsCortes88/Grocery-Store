/**
 * Author: Oswaldo Cortes-Tinoco
 * Assignment Name: WK02HW03: Jotto
 * Date: 7 - Feb - 2023
 * Description: This program simulates a game of jotto by taking in a text file
 * with a list of words. It then uses that list to randomly select a word. The program
 * is limited to 5 letter words. Then the user can enter their guess and the program will
 * determine the correctness and keep track of guesses and words played
 */

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Jotto {
    // Constant Fields
    private static final int WORD_SIZE = 5;
    private static final boolean DEBUG = true;
    // Members
    private final String filename;
    private final ArrayList<String> playedWords  = new ArrayList<> ();
    private final ArrayList<String> wordList = new ArrayList<> ();
    private final ArrayList<String> playerGuesses = new ArrayList<> ();
    private String currentWord;
    private int score = 0;

    // Constructor
    public Jotto(String filename) {
        this.filename = filename;
        readWords();
    }
    // public methods
    public int getLetterCount(String wordGuess) {
        int count = 0;
        String currentWordCopy = wordGuess;
        for(int i = 0; i < currentWordCopy.length(); i++) {
            // If the character exists in current word replace all chars in currentGuess with empty char ' '
            if(currentWordCopy.charAt(i) != ' ' && currentWord.indexOf(currentWordCopy.charAt(i)) != -1){
                count += 1;
                currentWordCopy = currentWordCopy.replace(wordGuess.charAt(i), ' ');
            }
        }
        return count;
    }

    public void play() {
        Scanner scan = new Scanner(System.in);
        String choice = "";
        // Display menu that will repeat as long as zz is not entered
        System.out.println("Welcome to the game.");
        System.out.println("Current Score: " + score);
        do {
            System.out.println("=-=-=-=-=-=-=-=-=-=-=");
            System.out.println("Choose one of the following:");
            System.out.println("1:	 Start the game");
            System.out.println("2:	 See the word list");
            System.out.println("3:	 See the chosen words");
            System.out.println("4:	 Show Player guesses");
            System.out.println("zz to exit");
            System.out.println("=-=-=-=-=-=-=-=-=-=-=");
            System.out.print("What is your choice: ");
            choice = scan.nextLine();
            switch (choice) {
                case "1", "one" -> {  // Play round
                    if (pickWord()) {
                        score = guess() + score;
                    }
                }
                case "2", "two" -> {  // Show the word list in the file
                    showWordList();
                    System.out.println("Press enter to continue");
                    scan.nextLine();
                }
                case "3", "three" -> {  // show played words
                    showPlayedWords();
                    System.out.println("Press enter to continue");
                    scan.nextLine();
                }
                case "4", "four" ->  // show player guesses
                        showPlayerGuesses();
                case "zz" ->  // Display score and end
                        System.out.println("\nFinal score: " + score + "\nThank you for playing");
                default -> {  // Don't recognize input message
                    System.out.println("I don't know what \"" + choice + "\" is.");
                    System.out.println("Press enter to continue");
                    scan.nextLine();
                }
            }
        } while(!choice.equals("zz"));
    }

    // Private methods
    private int score() { // Return the score
        return score;
    }

    private int guess() { // runs a round
        ArrayList<String> currentGuesses = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int letterCount = 0;
        int score = WORD_SIZE + 1;
        String wordGuess;
        do {
            System.out.println("Current score: " + score);
            System.out.print("What is your guess (q to quit): ");
            wordGuess = scan.nextLine().toLowerCase();
            if(wordGuess.equals("q")) {
                playedWords.remove(currentWord);
                // Punish player be setting the score as low as possible either 0 or negative score
                if(score < 0) {
                    this.score = score;
                    return 0;
                } else {
                    this.score = 0;
                    return 0;
                }
            } else {
                if(wordGuess.length() != WORD_SIZE) { // not valid word
                    System.out.println("Word must be 5 characters (" + wordGuess + " is " + wordGuess.length() + ")");
                } else { // Valid word entered
                    addPlayerGuess(wordGuess);
                    if(!currentGuesses.contains(wordGuess)) { // Not reoccurring guess
                        currentGuesses.add(wordGuess);
                        if(wordGuess.equals(currentWord)) { // Exact match to current word
                            System.out.println("DINGDINGDING!!! the word was " + currentWord);
                            playerGuessScores(currentGuesses);
                            System.out.println("\nYour score is " + (score()+score));
                            System.out.println("Press enter to continue");
                            scan.nextLine();
                            return score;
                        } else { // Similar chars to current word
                            letterCount = getLetterCount(wordGuess);
                            if(letterCount != getLetterCount(currentWord)) { // not same count of matches
                                System.out.println(wordGuess + " has a Jotto score of " + letterCount);
                                score -= 1;
                            } else { // same count char matches means anagram
                                score -= 1;
                                boolean anagram = true;
                                for(int i = 0; i < wordGuess.length(); i++) { // check if all there is an unexpected letter
                                    if(currentWord.indexOf(wordGuess.charAt(i)) == -1) { // when there is a different character it is not an anagram
                                        anagram = false;
                                    }
                                }
                                if(anagram) {
                                    System.out.println("Your guess is an ANAGRAM");
                                } else {
                                    System.out.println(wordGuess + " has a Jotto score of " + letterCount);
                                }

                            }
                        }
                        playerGuessScores(currentGuesses);
                    } else { // reoccurring guess
                        System.out.println("The word '" + wordGuess + "' has already been entered.");
                    }
                }
            }
        } while(!wordGuess.equals("q"));
        return score;
    }

    private boolean pickWord() {
        if(playedWords.size() == wordList.size() && playedWords.contains(currentWord)) { //playerwords has same amount as wordlist it means all words were played
            System.out.println("You've guessed them all!");
            showPlayerGuesses();
            return false;
        } else {
            Random index = new Random(); // Make a random number to access a random element in the wordlist
            currentWord = wordList.get(index.nextInt(wordList.size()));
            if(playedWords.contains(currentWord) && playedWords.size() != wordList.size() ) { // if the word was played choose another random element
                return pickWord();
            } else { // return the new word
                playedWords.add(currentWord);
                if(DEBUG) {
                    System.out.println(currentWord);
                }
                return true;
            }
        }
    }

    // Display methods
    private void showWordList() {
        System.out.println("Current word list:");
        for (String s : wordList) { // Prints words in wordlist
            System.out.println(s);
        }
    }

    private void showPlayedWords() {
        if(playedWords.isEmpty()) {
            System.out.println("No words have been played");
        } else {
            System.out.println("Current list of played words:");
            for (String playedWord : playedWords) {
                System.out.println(playedWord);
            }
        }
    }

    private void showPlayerGuesses() {
        Scanner uInput = new Scanner(System.in);
        if(playerGuesses.isEmpty()) {
            System.out.println("No guesses yet");
        } else { // Show the guesses as long as they have made a guess
            System.out.println("Current player guesses:");
            for (String playerGuess : playerGuesses) {
                System.out.println(playerGuess);
            }
            // Request adding guesses to the word list
            System.out.println("Would you like to add the words to the word list? (y/n)");
            if(uInput.nextLine().equals("y")) {
                System.out.println("updating word list");
                updateWordList();
                showWordList();
            }
        }
        System.out.println("Press enter to continue");
        uInput.nextLine();
    }

    private void updateWordList() { // Writes words in player guess to the file and adds them to the wordlist arraylist

        try { // Iit is needed for file not found exception or any other IO exception
            FileWriter wordFile = new FileWriter(filename);
            for(String playerGuess : playerGuesses) { // Adds guesses to the wordlist array list
                if(!wordList.contains(playerGuess)) {
                    wordList.add(playerGuess);
                }
            }
            for(String words : wordList) { // Updates the txt file with all the words in the wordList array list
                wordFile.write(words + "\n");
            }
            wordFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find " + filename);
        } catch (IOException e) {
            System.out.println("Something went wrong when trying to write to the file.");
        }

    }

    private void readWords() {
        File f = null;
        Scanner fileIn = null;

        try {
            f = new File(filename);
            fileIn = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open " + filename);
        }

        while(fileIn != null && fileIn.hasNext()) {
            String word = fileIn.nextLine();
            if(!wordList.contains(word)) {
                wordList.add(word);
            }
        }
    }


    private void playerGuessScores(ArrayList<String> guesses) {
        System.out.println("Guesses\t\tScores");
        for(String guess : guesses) { // Loops through all the guesses and gets the score via function
            System.out.println(guess + "\t\t" + getLetterCount(guess));
        }
        System.out.println();
    }

    private void addPlayerGuess(String wordGuess) {
        if(!playerGuesses.contains(wordGuess)) {
            playerGuesses.add(wordGuess);
        }
    }

}
