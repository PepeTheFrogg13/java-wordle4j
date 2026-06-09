package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private final PrintWriter log;

    private final String dictionayPath;

    public WordleDictionaryLoader(PrintWriter log, String dictionayPath) {
        this.dictionayPath = dictionayPath;
        this.log = log;
    }

    public WordleDictionary loadDictionary() {

        List<String> words = new ArrayList<>();

        File file = new File(dictionayPath);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()){
                String word = bufferedReader.readLine();
                words.add(word);
            }
            log.println("Словарь загружен");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new WordleDictionary(log,words);

    }
}
