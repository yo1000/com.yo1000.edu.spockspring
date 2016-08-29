package com.yo1000.selfstudy.spockspring.service;

import com.yo1000.selfstudy.spockspring.model.Word;
import com.yo1000.selfstudy.spockspring.repository.WordRepository;
import com.yo1000.selfstudy.spockspring.util.WordSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (Optional.ofNullable(wordText).orElse("").isEmpty()) {
            throw new WordChainException("wordText is empty");
        }

        List<Word> words = getWordRepository().findAllOrderByCreatedDesc();

        if (getWordSplitter().getTail(words.get(0).getText()) != getWordSplitter().getHead(wordText)) {
            throw new WordChainException("wordText can not chain");
        }

        getWordRepository().save(wordText);
        return getWordSplitter().getTail(wordText);
    }

    public WordRepository getWordRepository() {
        return wordRepository;
    }

    public WordSplitter getWordSplitter() {
        return wordSplitter;
    }

    public static class WordChainException extends IllegalArgumentException {
        public WordChainException(String s) {
            super(s);
        }
    }
}
