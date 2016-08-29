package com.yo1000.selfstudy.spockspring.util;

import org.springframework.stereotype.Component;

/**
 * @author yo1000
 */
@Component
public class WordSplitter {
    public char getHead(String word) {
        if (word == null || word.isEmpty()) {
            return Character.MIN_VALUE;
        }
        return word.charAt(0);
    }

    public char getTail(String word) {
        if (word == null || word.isEmpty()) {
            return Character.MIN_VALUE;
        }
        return word.charAt(word.length() - 1);
    }
}
