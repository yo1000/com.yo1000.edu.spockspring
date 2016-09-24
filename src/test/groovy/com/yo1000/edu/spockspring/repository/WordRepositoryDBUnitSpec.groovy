package com.yo1000.edu.spockspring.repository

import com.yo1000.edu.spockspring.model.Word
import org.dbunit.DataSourceDatabaseTester
import org.dbunit.operation.DatabaseOperation
import org.dbunit.util.fileloader.CsvDataFileLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.ResourceUtils
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource
/**
 *
 * @author yo1000
 */
@Unroll
@SpringBootTest
class WordRepositoryDBUnitSpec extends Specification {
    @Autowired
    DataSource dataSource

    @Autowired
    WordRepository repository

    def setup() {
        def databaseTester = new DataSourceDatabaseTester(dataSource)
        databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

        def loader = new CsvDataFileLoader()
        // URL 指定時の末尾のスラッシュがないと、WordRepositoryDBUnitSpec がファイルとして扱われてしまい
        // DBUnit は、table-ordering.txt を見つけてくれなくなり、結果テストがエラーで終了する
        def url = ResourceUtils.getURL("classpath:dbunit/WordRepositoryDBUnitSpec/")

        databaseTester.dataSet = loader.loadDataSet(url)
        databaseTester.onSetup()
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
