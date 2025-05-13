package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.Daylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaylistRepository extends JpaRepository<Daylist, Long> {

    // [BASIC] basicId에 해당하는 daylist 목록 개수 계산
    int countByBasicBasicId(Long basicId); // SELECT COUNT(*) FROM daylist WHERE basic.basic_id = :basicId

    // [DAYLIST] basicId에 해당하는 daylist 목록 List 형태로 반환
    List<Daylist> findAllByBasicBasicId(Long basicId);
}
