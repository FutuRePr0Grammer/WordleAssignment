import java.util.Random
import java.io.File
/**
 * https://www.nytimes.com/games/wordle/index.html
 * https://wordplay.com/
 * Author: Seikyung Jung, David Rochon and Sean Tammelleo
 * Description: We implemented the following methods after developing the method bodies.
 * The game is now fully functional from the terminal.
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
//fun selectWord(): String {
//    var randomInt = Random().nextInt(0, wordList.count() - 1)
//    return wordList[randomInt]
//}

fun selectWord():String = wordList[Random().nextInt(wordList.size)]


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
fun gameState(guess: String, word: String): String {
    // variable to hold the user's guess as an array of strings once the characters are color-coded
    // var colorCodedGuess = arrayOf<String>()
    var colorCodedGuess = Array(5) { "" }

    // count the occurrences of the letters in the selected word and the user's guess
    // NOTE: maps are not inherently mutable (changeable). Must set this explicitly
    var wordCharacterCount = countCharacterOccurrences(word).toMutableMap()

    // store the user's guess as an array of color-coded strings to signify how correct they were
    for (i in 0..4) {
        if (wordCharacterCount[guess[i]] != 0 && word.contains(guess[i]) && guess[i] != word[i]) {
            colorCodedGuess[i] = "$ANSI_YELLOW${guess[i]}$ANSI_RESET"
            wordCharacterCount[guess[i]] = wordCharacterCount[guess[i]]!!.minus(1)
            continue
        } else if (guess[i] == word[i]) {
            colorCodedGuess[i] = "$ANSI_GREEN${guess[i]}$ANSI_RESET"
            wordCharacterCount[guess[i]] = wordCharacterCount[guess[i]]!!.minus(1)
            continue
        }
        if (wordCharacterCount[guess[i]] == 0 || !word.contains(guess[i])) {
            colorCodedGuess[i] = "$ANSI_BLACK${guess[i]}$ANSI_RESET"
            continue
        }
    }

    // convert array of strings to a string and return it
    // return colorCodedGuess.joinToString(",")

    return colorCodedGuess[0] + colorCodedGuess[1] +
            colorCodedGuess[2] + colorCodedGuess[3] + colorCodedGuess[4]
}




// Determine when the game is over and print out the game state.
// If the game is over, congratulate the user
fun gameOver(userInput: String, word: String): Boolean{
    return userInput == word
}



// 1. call selectWord()
// 2. print selected word (for debugging, show the word to guess)
// 3. Only allow 6 chances to guess based on original Wordle game
//    call legitGuess()
//    If the game is over, exit
// 4. If the user didn't guess the word, show it to the user
fun main() {
    // variable to store the current attempt
    var currentAttempt = 1
    // variable to store the gameOver status
    var gameOver = false

    // get a randomly selected word and print it
    var selectedWord = selectWord()
    println("*** The answer is $ANSI_GREEN$selectedWord$ANSI_RESET ***")

    """// get the character count for the selected word - used for debugging
    var characterCountMap = countCharacterOccurrences(selectedWord)
    // debugging statement, print the map contents to make sure it is populated correctly
    println(characterCountMap.entries)"""

    // get the user input up to six times (if gameOver = true, user won, if false but attempts = 6, lost
    while(currentAttempt <= 6 && !gameOver){
        println("$currentAttempt. Enter a five-letter word: ")
        var userGuess = readln()

        if (userGuess.count() != 5){
            println("The guess must be five letters. Try again.")
            continue
        }

        """// debugging statement to make sure readln() is working as intended
    println("Your guess: $userGuess")"""

        //check if user guess is a word in the file
        var isGuessInFile = legitGuess(userGuess)

        if(!isGuessInFile){
            println("The guessed word is not in the file. Try again.")
            continue
        }

        // get and print the user's guess as a color-coded list to signify how correct they were
        var userGuessColorCoded = gameState(userGuess, selectedWord)
        println(userGuessColorCoded)

        """// debugging statements to check that legitGuess() is working as intended
        if(isGuessInFile){
            println("Your guess exists in the file!")
        }
        else{
            println("Your guess does not exist in the file")
        }"""

        gameOver = gameOver(userGuess, selectedWord)

        if(gameOver){
            println("You won! Congratulations!")
            break
        }

        currentAttempt += 1
    }

    if(currentAttempt > 6){
        println("You lost! The correct word was $selectedWord. free to play again!")
    }
}
