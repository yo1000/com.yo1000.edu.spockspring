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
        words                                    | _
        [w('aaa', '160830'), w('bbb', '160831')] | _
    }

    def "チェインするとリポジトリに保存データが渡り次の一文字が戻る"() {
        setup:
        Mockito.doReturn(1)
                .when(wordRepository)
                .save(Mockito.anyString())

        expect:
        def resultChar = wordChainService.chain(wordText)

        nextChar == resultChar
        // メソッドが呼ばれていることをテスト
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordText) || true

        where:
        wordText | nextChar
        'abcd'   | 'd'
        'xyz'    | 'z'
        'あい'     | 'い'
        null     | Character.MIN_VALUE
        ''       | Character.MIN_VALUE
    }

    def w(String text, String created) {
        return new Word(
                text: text,
                created: new SimpleDateFormat("yyMMdd").parse(created)
        )
    }
}
