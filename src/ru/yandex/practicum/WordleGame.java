package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordGameException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;
import ru.yandex.practicum.exceptions.WordWrongLengthException;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private static final int MAX_STEPS = 6;

    private static final int WORD_LENGTH = 5;

    private final String answer;

    private int steps;

    private final WordleDictionary dictionary;

    private final PrintWriter log;

    private final Set<Character> wrongLetters;

    private final Set<Character> existLetters;

    private final Set<String> usedWords;

    char[] rightLetters;


    public WordleGame(PrintWriter log, WordleDictionary dictionary) {
        this.steps = MAX_STEPS;
        this.dictionary = dictionary;
        this.log = log;
        this.answer = dictionary.getRandomAnswer();
        existLetters = new HashSet<>();
        wrongLetters = new HashSet<>();
        usedWords = new HashSet<>();
        rightLetters = new char[5];
    }

    public WordleGame(PrintWriter log, WordleDictionary dictionary, String answer) {
        this.steps = MAX_STEPS;
        this.dictionary = dictionary;
        this.log = log;
        this.answer = answer;
        existLetters = new HashSet<>();
        wrongLetters = new HashSet<>();
        usedWords = new HashSet<>();
        rightLetters = new char[5];
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isEnded() {
        return steps <= 0;
    }

    public HashMap<Character, Integer> getAnswerLetters() {
        HashMap<Character, Integer> result = new HashMap<>();
        for (Character c : answer.toCharArray()) {
            if (result.containsKey(c)) {
                int count = result.get(c);
                count++;
                result.put(c, count);
            } else {
                result.put(c, 1);
            }
        }
        return result;
    }

    public void checkWord(String word) throws WordGameException {
        if (!dictionary.wordExists(word) && !word.isEmpty()) {
            throw new WordNotFoundInDictionary(word);
        }
        if (word.length() != WORD_LENGTH) {
            throw new WordWrongLengthException(word);
        }

    }

    public void guessWord(String word) throws WordGameException {
        String normalizedWord = WordleDictionary.normalizeWord(word);
        checkWord(normalizedWord);
        System.out.println(wordsCompare(normalizedWord));

    }

    public String wordsCompare(String word) {

        StringBuilder stringBuilder = new StringBuilder();

        HashMap<Character, Integer> answerLettersCount = getAnswerLetters();

        //Здесь будем хранить наши символы - подсказки
        char[] letters = new char[5];

        /*
            Обработаем сначала все точные метчи с ответом, чтобы исключить повторяшки перед уже правильными буквами
            Например ответ кошка, слово кашка должен выдать +-+++, так как в слове "кошка" одна буква а, а в "кашка" их 2
         */
        for (int i = 0; i < word.length(); i++) {
            if (answer.charAt(i) == word.charAt(i)) {
                int count = answerLettersCount.get(word.charAt(i));
                count--;
                answerLettersCount.put(word.charAt(i), count);
                existLetters.add(word.charAt(i));
                rightLetters[i] = word.charAt(i);
                letters[i] = '+';
            }
        }

        for (int i = 0; i < word.length(); i++) {
            if (letters[i] == '+') continue;
            if (answer.indexOf(word.charAt(i)) > -1) {
                existLetters.add(word.charAt(i));
                int count = answerLettersCount.get(word.charAt(i));
                if (count == 0) {
                    letters[i] = '-';
                } else {
                    count--;
                    answerLettersCount.put(word.charAt(i), count);
                    letters[i] = '^';
                }
            } else {
                wrongLetters.add(word.charAt(i));
                letters[i] = '-';
            }
        }
        for (char c : letters) {
            stringBuilder.append(c);
        }

        if (word.equals(answer)) {
            steps = 0;
            log.println("Пользователь отгадал слово " + answer);
            stringBuilder.append(" поздравляем, вы угадали слово!");
        } else {
            steps--;
            usedWords.add(word);
            log.println("Пользователь не угадал слово, осталось попыток: " + steps);
            stringBuilder.append(" попыток осталось ");
            stringBuilder.append(steps);
            stringBuilder.append("/6");
        }
        return stringBuilder.toString();
    }


    public String getTip() {
        Random random = new Random();
        //Копируем словарь
        List<String> wordsForTip = new ArrayList<>();

        for (String word : dictionary.getWords()) {

            boolean notFits = false;

            if (word.length() != 5) continue;

            if (usedWords.contains(word)) continue;

            for (int i = 0; i <= rightLetters.length - 1; i++) {
                if ((rightLetters[i] != 0) && (rightLetters[i] != word.charAt(i))) {
                    notFits = true;
                }
            }
            if (notFits) continue;

            for (Character c : wrongLetters) {
                if (word.contains(c.toString())) {
                    notFits = true;
                }
            }
            if (notFits) continue;

            for (Character c : existLetters) {
                if (!word.contains(c.toString())) notFits = true;
            }
            if (notFits) continue;
            wordsForTip.add(word);
        }
        if (wordsForTip.isEmpty()) {
            return "Нет идей для подсказки, введите слово вручную!";
        } else {
            return wordsForTip.get(random.nextInt(wordsForTip.size()));
        }

    }

}
