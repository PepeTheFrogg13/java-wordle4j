package ru.yandex.practicum.exceptions;

public abstract class WordGameException extends Exception {

    private String word;

    public WordGameException(String message) {
        super(message);
    }

    public WordGameException(String message, String word) {
        this(message);
        this.word = word;
    }

    public String getWord() {
        return word;
    }


}
