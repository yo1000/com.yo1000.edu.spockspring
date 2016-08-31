package com.yo1000.edu.spockspring.repository

import com.ninja_squad.dbsetup.DbSetup
import com.ninja_squad.dbsetup.Operations
import com.ninja_squad.dbsetup.destination.Destination
import com.yo1000.edu.spockspring.model.Word
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll
/**
 *
 * @author yo1000
 */
@Unroll
@SpringBootTest
class WordRepositorySpec extends Specification {
    @Autowired
    Destination destination

    @Autowired
    WordRepository repository

    def setup() {
        def insertWords = Operations.insertInto('WORD')
                .columns('TEXT'       , 'CREATED')
                .values('しりとり'     , '2016-08-30 19:06:00')
                .values('りんご'       , '2016-08-30 19:07:00')
                .values('ごりら'       , '2016-08-30 19:08:00')
                .values('らっぱ'       , '2016-08-30 19:09:00')
                .values('ぱいなっぷる'  , '2016-08-30 19:10:00')
                .build()

        new DbSetup(destination,
                Operations.sequenceOf(
                        Operations.truncate('WORD'),
                        insertWords
                )
        ).launch()
    }

    def "日付の降順にテストデータが取得できること"() {
        expect:
        def words = repository.findAllOrderByCreatedDesc()
        Word prev = null

        words.each {
            if (prev == null) {
                prev = it
                return
            }

            assert prev.created.getTime() > it.created.getTime()
            prev = it
        }
    }

    def "保存したデータを確認できること"() {
        expect:
        def updates = repository.save(text);
        def item = repository.findAllOrderByCreatedDesc().get(0)

        assert 1 == updates
        assert text == item.text

        where:
        text    | _
        'あああ' | _
        'いいい' | _
    }
}
