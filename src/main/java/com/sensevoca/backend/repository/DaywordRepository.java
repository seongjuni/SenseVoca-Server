package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.Daylist;
import com.sensevoca.backend.domain.Dayword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaywordRepository extends JpaRepository<Dayword, Long> {

    // [DAYLIST] daylistId에 해당하는 dayword 목록 개수 조회
    int countByDaylistDaylistId(Long daylistId); // SELECT COUNT(*) FROM dayword WHERE daylist.daylist_id = :daylistId;

    // [DAYWORD] daylistId에 해당하는 dayword 목록 List 형태로 반환
    List<Dayword> findAllByDaylistDaylistId(Long daylistId);
}
