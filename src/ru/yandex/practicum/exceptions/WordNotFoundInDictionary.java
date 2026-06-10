package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionary extends WordGameException {

    public WordNotFoundInDictionary(String word) {
        super("Слова нет в словаре", word);
    }
}
