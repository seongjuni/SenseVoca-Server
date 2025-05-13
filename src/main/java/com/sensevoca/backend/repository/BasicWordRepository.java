package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.BasicWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicWordRepository extends JpaRepository<BasicWord, Long> {

    // [BASICWORD] daywordId에 해당하는 단어 상세 정보
    //List<BasicWord> findAllByDaywordDaywordId(Long daywordId); //SELECT * FROM

}
