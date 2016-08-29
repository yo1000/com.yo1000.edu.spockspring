package com.yo1000.selfstudy.spockspring.repository;

import com.yo1000.selfstudy.spockspring.model.Word;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yo1000
 */
@Repository
@Mapper
public interface WordRepository {
    List<Word> findAllOrderByCreatedDesc();
    int save(String word);
}

