package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;

    private PrintWriter log;

    public WordleDictionary(PrintWriter log, List<String> words) {
        this.log = log;
        this.words = words;
    }

    public List<String> getWords() {
        return words;
    }

    public boolean wordExists(String word) {
        return words.contains(word);
    }

    public String getRandomAnswer() {
        Random random = new Random();
        List<String> filtredWords = new ArrayList<>();
        for (String s : words) {
            if (s.length() == 5) {
                filtredWords.add(s);
            }
        }
        return filtredWords.get(random.nextInt(filtredWords.size()));
    }

    public String normalizeWord(String word) {
        return word.toLowerCase().replace('ё', 'е');
    }

}
