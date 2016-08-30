package com.yo1000.edu.spockspring.service

import com.yo1000.edu.spockspring.model.Word
import com.yo1000.edu.spockspring.repository.WordRepository
import com.yo1000.edu.spockspring.util.WordSplitter
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import java.text.SimpleDateFormat
/**
 *
 * @author yo1000
 */
@Unroll
class WordChainServiceSpec extends Specification {
    WordChainService service
    WordRepository repository

    def setup() {
        def splitter = new WordSplitter()
        repository = Mockito.mock(WordRepository)
        service = new WordChainService(repository, splitter)
    }

    def "履歴取得はリポジトリの返却と一致する"() {
        setup:
        Mockito.doReturn(words)
                .when(repository)
                .findAllOrderByCreatedDesc()

        expect:
        assert words == service.history()

        where:
        words                                    | _
        [w('aaa', '160830'), w('bbb', '160831')] | _
        [w('xxx', '160901')]                     | _
    }

    def "チェインするとリポジトリに保存データが渡り次の一文字が戻る"() {
        setup:
        Mockito.doReturn(1)
                .when(repository)
                .save(Mockito.anyString())

        expect:
        def resultChar = service.chain(wordText)

        assert nextChar == resultChar
        Mockito.verify(repository, Mockito.times(1)).save(wordText) || true

        where:
        wordText | nextChar
        'abcd'   | 'd'
        'xyz'    | 'z'
        'あい'    | 'い'
        null     | Character.MIN_VALUE
        ''       | Character.MIN_VALUE
    }

    def "#from と #to はチェイン可能"() {
        expect:
        assert service.chainable(from, to)

        where:
        from    | to
        'aaa'   | 'aaa'
        'aiue'  | 'eoeo'
        'あいう' | 'ううう'
        'こーら' | 'らんちょんまっと'
        '*#$'   | '$!@'
    }

    def "#from と #to はチェイン不可"() {
        expect:
        assert !service.chainable(from, to)

        where:
        from    | to
        'aab'   | 'aaa'
        'aiu'   | 'eoeo'
        'あい'   | 'ううう'
        'こ'     | 'らんちょんまっと'
        '*#'    | '$!@'
        'あい'   | ''
        'あい'   | null
        ''      | 'ううう'
        null    | 'ううう'
    }

    def w(String text, String created) {
        return new Word(
                text: text,
                created: new SimpleDateFormat("yyMMdd").parse(created)
        )
    }
}
