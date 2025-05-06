package com.sensevoca.backend.repository.basic;

import com.sensevoca.backend.entity.basic.Dayword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DaywordRepository extends JpaRepository<Dayword, Long> {

    @Query(value = "SELECT dw.word_id, mne.word, mne.meaning, mne.part_of_speech " +
                   "FROM dayword dw " +
                   "JOIN words w ON dw.word_id = w.word_id " +
                   "JOIN mnemonic_example mne ON w.mnemonic_id = mne.id " +
                   "WHERE dw.daylist_id = :daylistId", nativeQuery = true)
    List<Object[]> findAllByDaylistId(@Param("daylistId") Long daylistId); // nativeQuery는 엔티티 매핑이 아니라 raw 데이터로 반환되기 때문에 Object[] 타입 리스트로 반환한다.
}
