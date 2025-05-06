package com.sensevoca.backend.repository.basic;

import com.sensevoca.backend.dto.basic.BasicResponse;
import com.sensevoca.backend.entity.basic.Basic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BasicRepository extends JpaRepository<Basic, Long> {
    @Query(value = "SELECT b.basic_id, b.basic_title, b.basic_type, b.basic_offered_by, COUNT(d.daylist_id) AS daylistCount " +
            "FROM basic b LEFT JOIN daylist d ON b.basic_id = d.basic_id " +
            "GROUP BY b.basic_id, b.basic_title, b.basic_type, b.basic_offered_by", nativeQuery = true)
    List<Object[]> findAllWithDaylistCount(); // nativeQuery는 엔티티 매핑이 아니라 raw 데이터로 반환되기 때문에 Object[] 타입 리스트로 반환한다.
}
