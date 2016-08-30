package com.yo1000.edu.spockspring.service;

import com.yo1000.edu.spockspring.model.Word;
import com.yo1000.edu.spockspring.repository.WordRepository;
import com.yo1000.edu.spockspring.util.WordSplitter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yo1000
 */
@Service
public class WordChainService {
    private WordRepository wordRepository;
    private WordSplitter wordSplitter;

    public WordChainService(WordRepository wordRepository, WordSplitter wordSplitter) {
        this.wordRepository = wordRepository;
        this.wordSplitter = wordSplitter;
    }

    public List<Word> history() {
        return getWordRepository().findAllOrderByCreatedDesc();
    }

    public char chain(String wordText) {
        getWordRepository().save(wordText);
        return getWordSplitter().getTail(wordText);
    }

    public boolean chainable(String from, String to) {
        return getWordSplitter().getTail(from) == getWordSplitter().getHead(to);
    }

    public WordRepository getWordRepository() {
        return wordRepository;
    }

    public WordSplitter getWordSplitter() {
        return wordSplitter;
    }

    public static class WordChainException extends RuntimeException {
        public WordChainException() {}

        public WordChainException(String s) {
            super(s);
        }
    }
}
