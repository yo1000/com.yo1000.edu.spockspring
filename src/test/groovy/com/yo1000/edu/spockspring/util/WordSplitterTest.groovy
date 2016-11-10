package com.yo1000.edu.spockspring.util

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class WordSplitterTest extends Specification {

    def wordSplitter = new WordSplitter()

    def "#wordText の先頭1文字を返す"() {
        expect:
        wordHead == wordSplitter.getHead(wordText)

        where:
        wordText || wordHead
        'あいうえ'   || 'あ' as char
        'いうえお'   || 'い' as char
        'ABCD'   || 'A' as char
        '#$!'    || '#' as char
    }

    def "#wordText の末尾一文字を返す"() {
        expect:
        wordHead == wordSplitter.getTail(wordText)

        where:
        wordText || wordHead
        'あいうえ'   || 'え' as char
        'いうえお'   || 'お' as char
        'ABCD'   || 'D' as char
        '#$!'    || '!' as char
    }

    def "nullや空文字の場合はChar# MINをかえす"() {
        expect:
        wordSplitter.getHead(emptyVal) == Character.MIN_VALUE
        wordSplitter.getTail(emptyVal) == Character.MIN_VALUE

        where:
        emptyVal << [null, "", '']
    }
}
