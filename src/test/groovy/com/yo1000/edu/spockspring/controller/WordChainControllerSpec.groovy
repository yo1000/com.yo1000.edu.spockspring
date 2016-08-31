package com.yo1000.edu.spockspring.controller

import com.yo1000.edu.spockspring.model.Word
import com.yo1000.edu.spockspring.service.WordChainService
import org.hamcrest.Matchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll

import java.text.SimpleDateFormat
/**
 *
 * @author yo1000
 */
@Unroll
@SpringBootTest
@AutoConfigureMockMvc
class WordChainControllerSpec extends Specification {
    @Autowired
    MockMvc mockMvc

    @SpyBean
    WordChainService wordChainService

    def "ページにモデルが渡り正しいビューを返すこと"() {
        setup:
        Mockito.doReturn(words).when(wordChainService).history()

        expect:
        mockMvc.perform(MockMvcRequestBuilders
                .get('/word-chain')
        ).andExpect(MockMvcResultMatchers
                .status().is(status)
        ).andExpect(MockMvcResultMatchers
                .model().attributeHasNoErrors('word')
        ).andExpect(MockMvcResultMatchers
                .model().attribute('words', words)
        ).andExpect(MockMvcResultMatchers
                .view().name(view)
        )

        Mockito.verify(wordChainService, Mockito.times(1)).history() || true

        where:
        words                                        | status | view
        [w('うえお', '160901'), w('あいう', '160831')] | 200    | 'word-chain'
        [w('くえば', '160903'), w('かきく', '160902')] | 200    | 'word-chain'
    }

    def "正しく繋がる単語をつづけた場合GETへリダイレクトされること"() {
        setup:
        Mockito.doReturn(words).when(wordChainService).history()

        expect:
        mockMvc.perform(MockMvcRequestBuilders
                .post('/word-chain')
                .param('text', wordText)
        ).andExpect(MockMvcResultMatchers
                .status().is(status)
        ).andExpect(MockMvcResultMatchers
                .redirectedUrl('/word-chain')
        )

        Mockito.verify(wordChainService, Mockito.times(1))
                .chainable(Mockito.anyString(), Mockito.anyString()) || true
        Mockito.verify(wordChainService, Mockito.times(1))
                .chain(Mockito.anyString()) || true

        where:
        words                                        | wordText | status
        [w('うえお', '160901'), w('あいう', '160831')] | 'おから'  | 302
        [w('くえば', '160903'), w('かきく', '160902')] | 'ばいと'  | 302
    }

    def "正しく繋がらない単語をつづけた場合エラーが表示されること"() {
        setup:
        Mockito.doReturn(words).when(wordChainService).history()

        expect:
        mockMvc.perform(MockMvcRequestBuilders
                .post('/word-chain')
                .param('text', wordText)
        ).andExpect(MockMvcResultMatchers
                .status().is(status)
        ).andExpect(MockMvcResultMatchers
                .model().attributeHasFieldErrorCode('word', 'text', 'NotChain')
        ).andExpect(MockMvcResultMatchers
                .model().attribute('words', words)
        ).andExpect(MockMvcResultMatchers
                .view().name(view)
        )

        Mockito.verify(wordChainService, Mockito.times(1))
                .chainable(Mockito.anyString(), Mockito.anyString()) || true
        Mockito.verify(wordChainService, Mockito.times(0))
                .chain(Mockito.anyString()) || true

        where:
        words                                        | wordText | status | view
        [w('うえお', '160901'), w('あいう', '160831')] | 'んんん'  | 200    | 'word-chain'
        [w('くえば', '160903'), w('かきく', '160902')] | 'あああ'  | 200    | 'word-chain'
    }

    def "単語が入力されていない場合エラーが表示されること"() {
        setup:
        Mockito.doReturn(words).when(wordChainService).history()

        expect:
        mockMvc.perform(MockMvcRequestBuilders
                .post('/word-chain')
                .param('text', wordText)
        ).andExpect(MockMvcResultMatchers
                .status().is(status)
        ).andExpect(MockMvcResultMatchers
                .model().attributeHasFieldErrorCode('word', 'text',
                Matchers.anyOf(
                        Matchers.is('NotChain'),
                        Matchers.is('NotEmpty')))
        ).andExpect(MockMvcResultMatchers
                .model().attribute('words', words)
        ).andExpect(MockMvcResultMatchers
                .view().name(view)
        )

        Mockito.verify(wordChainService, Mockito.times(1))
                .chainable(Mockito.anyString(), Mockito.anyString()) || true
        Mockito.verify(wordChainService, Mockito.times(0))
                .chain(Mockito.anyString()) || true

        where:
        words                                        | wordText | status | view
        [w('うえお', '160901'), w('あいう', '160831')] | ''       | 200    | 'word-chain'
        [w('くえば', '160903'), w('かきく', '160902')] | ''       | 200    | 'word-chain'
    }

    def w(String text, String created) {
        return new Word(
                text: text,
                created: new SimpleDateFormat("yyMMdd").parse(created)
        )
    }
}
