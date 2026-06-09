package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordGameException;

import java.io.*;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    private static final String LOG_FILE = "log.txt";

    private static final String WORDS_FILE = "words_ru.txt";

    private final PrintWriter log;

    Wordle(PrintWriter log) {
        this.log = log;
    }


    public static void main(String[] args) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(LOG_FILE);
             Writer writer = new PrintWriter(fileOutputStream);
             PrintWriter log = new PrintWriter(writer, true)) {
            try {
                WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader(log, WORDS_FILE);
                WordleDictionary wordleDictionary = wordleDictionaryLoader.loadDictionary();
                WordleGame wordleGame = new WordleGame(log, wordleDictionary);
                Wordle wordle = new Wordle(log);
                wordle.playGeme(wordleGame);
            } catch (Exception e) {
                e.printStackTrace(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playGeme(WordleGame wordleGame) {
        log.println("Начало игры");
        Scanner scanner = new Scanner(System.in);
        System.out.println("У вас 6 попыток, для получения подсказки нажмите enter без ввода слова");
        while (!wordleGame.isEnded()) {
            String word = scanner.nextLine();
            try {
                if (word.isBlank()) {
                    word = wordleGame.getTip();
                    log.println("Пользователь взял подсказку " + word);
                    System.out.println(word);
                }
                log.println("Проверка слова " + word);
                wordleGame.guessWord(word);
            } catch (WordGameException e) {
                log.println("Ошибка валидации слова: " + e.getMessage() + " " + e.getWord());
                e.printStackTrace(log);
            }
        }
        System.out.println("Ответ:" + wordleGame.getAnswer());
        log.println("Конец игры " + wordleGame.getAnswer());
    }

}
