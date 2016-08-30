package com.yo1000.edu.spockspring.util

import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author yo1000
 */
@Unroll
class WordSplitterSpec extends Specification {
    def "#wordText の先頭1文字を返す"() {
        setup:
        def wordSplitter = new WordSplitter()

        expect:
        assert wordHead == wordSplitter.getHead(wordText)

        where:
        wordText  || wordHead
        'あいうえ' || 'あ'
        'いうえお' || 'い'
        'ABCDE'   || 'A'
        '#$!'     || '#'
    }

    def "#wordText の末尾一文字を返す"() {
        setup:
        def wordSplitter = new WordSplitter()

        expect:
        assert wordHead == wordSplitter.getTail(wordText)

        where:
        wordText  || wordHead
        'あいうえ' || 'え'
        'いうえお' || 'お'
        'ABCDE'   || 'E'
        '#$!'     || '!'
    }

    def "null や空文字の場合は Char#MIN を返す"() {
        setup:
        def wordSplitter = new WordSplitter()
        def val = ''

        when: val = wordSplitter.getHead(null)
        then: assert Character.MIN_VALUE == val

        when: val = wordSplitter.getHead('')
        then: assert Character.MIN_VALUE == val

        when: val = wordSplitter.getTail(null)
        then: assert Character.MIN_VALUE == val

        when: val = wordSplitter.getTail('')
        then: assert Character.MIN_VALUE == val
    }
}
