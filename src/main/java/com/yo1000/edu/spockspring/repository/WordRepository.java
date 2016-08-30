package com.yo1000.edu.spockspring.repository;

import com.yo1000.edu.spockspring.model.Word;
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
    int save(String wordText);
}

