package com.sensevoca.backend.repository.basic;

import com.sensevoca.backend.entity.basic.Daylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DaylistRepository extends JpaRepository<Daylist, Long> {

    @Query(value = "SELECT dl.daylist_id, dl.basic_id, dl.daylist_title, dl.latest_accessed_at, COUNT(dw.dayword_id) AS daywordCount " +
            "FROM daylist dl LEFT JOIN dayword dw ON dl.daylist_id = dw.daylist_id " +
            "WHERE dl.basic_id = :basicId " +
            "GROUP BY dl.daylist_id, dl.basic_id, dl.daylist_title, dl.latest_accessed_at", nativeQuery = true)
    List<Object[]> findAllWithDaywordCount(@Param("basicId") Long basicId); // nativeQuery는 엔티티 매핑이 아니라 raw 데이터로 반환되기 때문에 Object[] 타입 리스트로 반환한다.
}
