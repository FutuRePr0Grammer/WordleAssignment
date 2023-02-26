import java.util.Random
import java.io.File
/**
 * https://www.nytimes.com/games/wordle/index.html
 * https://wordplay.com/
 * Author: Seikyung Jung, David Rochon and Sean Tammelleo
 * Description: A very brief summary of what you or your team did
 */

// Place ANSI escape sequences in a string to change background color. For example,
// "$ANSI_GREEN$word$ANSI_RESET"
// https://en.wikipedia.org/wiki/ANSI_escape_code
const val ANSI_RESET = "\u001B[0m"   // Reset to default background color
const val ANSI_GREEN = "\u001B[42m"  // Green background color
const val ANSI_YELLOW = "\u001B[43m" // Yellow background color
const val ANSI_BLACK = "\u001B[40m"  // Black background color

// Read a file (list of words) used in the game
val wordList = File("wordle.txt").readLines()


// Pick a word from the file, randomly
fun selectWord(): String {
    var randomInt = Random().nextInt(0, wordList.count() - 1)
    return wordList[randomInt]
}


// Check if user's word exists in the file
fun legitGuess(guess:String):Boolean{
    var isInFile = false
    if(wordList.contains(guess)){
        isInFile = true
    }
    return isInFile
}


// build a map<character,count> for the word
// mutableMapOf<Char, Int>()
// Key is a letter, value counts occurrences of the letter
// TODO: Add actual functionality to body
fun countCharacterOccurrences(str:String):Map<Char, Int>{
    var characterCountMap = mutableMapOf<Char, Int>()
    // count the occurrence of each letter in the word and add it to the map
    // NOTE: until acts as if doing str.count() - 1. Will stop and not iterate once i = str length
    for(i in 0 until str.count()){
        // get the next character in the word
        var characterAtIndex = str[i]
        // if the key doesn't exist, put it in the map with a value of 0
        characterCountMap.putIfAbsent(characterAtIndex, 0)
        // iterate the value of the current key (char) by 1, signifying the count has gone up by 1
        // NOTE: the !! is a non-null assertion (cannot be null)
        characterCountMap[characterAtIndex] = characterCountMap[characterAtIndex]!!.plus(1)
    }
    return characterCountMap
}



// Color-code user's guess using information about the selected word
// 1. Store color-coded user's guess as array of strings. Five letters, index 0 to 4.
// 2. Count occurrences of each letter in the word
// 3. First, highlight in green where the characters lined up properly
//    If the guessed letter matches the word letter, color code in green
//    and decrement the occurrences for the corresponding letter
// 4. Next, highlight letters in the word but in the wrong spot or non-matches
//    If the guessed letter is in the word, highlight in yellow
//    and decrement the occurrences for the corresponding letter
//    Otherwise, highlight non-matches with a black background
// 5. Return the game state (remember to reset the background color)
// TODO: Add actual functionality to body
fun gameState(guess: String, word: String): String{
    return "Temp"
}




// Determine when the game is over and print out the game state.
// If the game is over, congratulate the user
// TODO: Add actual functionality to body
fun gameOver(userInput: String, word: String): Boolean{
    return true
}



// 1. call selectWord()
// 2. print selected word (for debugging, show the word to guess)
// 3. Only allow 6 chances to guess based on original Wordle game
//    call legitGuess()
//    If the game is over, exit
// 4. If the user didn't guess the word, show it to the user
fun main() {
    // get a randomly selected word and print it
    var selectedWord = selectWord()
    println("*** The answer is $ANSI_GREEN$selectedWord$ANSI_RESET ***")

    // get the character count for the selected word
    var characterCountMap = countCharacterOccurrences(selectedWord)
    // debugging statement, print the map contents to make sure it is populated correctly
    println(characterCountMap.entries)

    // get the user input up to six times (if gameOver = true, user won, if false but attempts = 6, lost
    // TODO: make it match the conditions in the comment above. Kept simple for now to check functionality
    println("Guess: ")
    var userGuess = readln()
    println("Your guess: $userGuess")
    //check if user guess is a word in the file
    var isGuessInFile = legitGuess(userGuess)

    // debugging statements to check that legitGuess() is working as intended
    if(isGuessInFile){
        println("Your guess exists in the file!")
    }
    else{
        println("Your guess does not exist in the file")
    }
}
