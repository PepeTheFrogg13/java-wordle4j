package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.WordGameException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;
import ru.yandex.practicum.exceptions.WordWrongLengthException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private final String LOG_FILE = "log.txt";

    private final String WORDS_FILE = "words_ru.txt";

    WordleDictionaryLoader wordleDictionaryLoader;

    WordleDictionary wordleDictionary;

    WordleGame wordleGame;

    @BeforeEach
    public void beforeEach() throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(LOG_FILE);
             Writer writer = new PrintWriter(fileOutputStream);
             PrintWriter log = new PrintWriter(writer, true)) {

            try {
                wordleDictionaryLoader = new WordleDictionaryLoader(log, WORDS_FILE);
                wordleDictionary = wordleDictionaryLoader.loadDictionary();
                wordleGame = new WordleGame(log, wordleDictionary,"шишка");
            } catch (Exception e) {
                e.printStackTrace(log);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNormalize() {

        String checkWord = wordleDictionary.normalizeWord("ПоСЁлок");

        assertEquals("поселок", checkWord);
    }

    @Test
    public void testValidateWordWrongLength() {

        String checkWord = wordleDictionary.normalizeWord("РебЁнок");

        Exception exception = assertThrows(WordWrongLengthException.class, () -> wordleGame.checkWord(checkWord));

        assertEquals("Длина слова не равна 5 символам", exception.getMessage());

    }

    @Test
    public void testValidateWordNotFoundInDictionary() {

        String checkWord = wordleDictionary.normalizeWord("Венера-11");

        Exception exception = assertThrows(WordNotFoundInDictionary.class, () -> wordleGame.checkWord(checkWord));

        assertEquals("Слова нет в словаре", exception.getMessage());

    }

    @Test
    public void testWordsCompare() {

        String checkWord = "шашка";

        String checkLetters = "+^+++ попыток осталось 5/6";

        assertEquals(checkLetters,wordleGame.wordsCompare(checkWord));

    }

    @Test
    public void testTip(){
        //Для слова "шишка" после ввода слова "шашка", подсказка должна быть 100% верной
        String checkWord = "шашка";

        wordleGame.wordsCompare(checkWord);

        String answer = wordleGame.getAnswer();

        assertEquals(answer,wordleGame.getTip());

    }

}
