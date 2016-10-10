package com.yo1000.edu.spockspring.service

import com.yo1000.edu.spockspring.model.Word
import com.yo1000.edu.spockspring.repository.WordRepository
import com.yo1000.edu.spockspring.util.WordSplitter
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import java.text.SimpleDateFormat

@Unroll
class WordChainServiceSpec extends Specification {

    WordChainService wordChainService
    WordRepository wordRepository

    def setup() {
        def wordSplitter = new WordSplitter()
        wordRepository = Mockito.mock(WordRepository)
        wordChainService = new WordChainService(wordRepository, wordSplitter)
    }

    def "履歴取得はリポジトリの返却と一致する"() {
        setup:
        Mockito.doReturn(words)
                .when(wordRepository)
                .findAllOrderByCreatedDesc()

        expect:
        words == wordChainService.history()

        where:
        words | _
        [w('aaa', '160830'), w('bbb', '160831')] | _
    }

    def w(String text, String created) {
        return new Word(
                text: text,
                created: new SimpleDateFormat("yyMMdd").parse(created)
        )
    }
}
