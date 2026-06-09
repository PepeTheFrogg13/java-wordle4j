package ru.yandex.practicum.exceptions;

public class WordWrongLengthException extends WordGameException {

    public WordWrongLengthException(String word) {
        super("Длина слова не равна 5 символам", word);
    }
}
