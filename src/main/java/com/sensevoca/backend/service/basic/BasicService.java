package com.sensevoca.backend.service.basic;

import com.sensevoca.backend.dto.basic.*;
import com.sensevoca.backend.entity.basic.Daylist;
import com.sensevoca.backend.entity.basic.Dayword;
import com.sensevoca.backend.entity.basic.MnemonicExample;
import com.sensevoca.backend.entity.basic.Words;
import com.sensevoca.backend.repository.basic.BasicRepository;
import com.sensevoca.backend.repository.basic.DaylistRepository;
import com.sensevoca.backend.repository.basic.DaywordRepository;
import com.sensevoca.backend.repository.basic.WordsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BasicService {

    private final BasicRepository basicRepository;
    private final DaylistRepository daylistRepository;
    private final WordsRepository wordsRepository;
    private final DaywordRepository daywordRepository;

    public BasicService(BasicRepository basicRepository, DaylistRepository daylistRepository, WordsRepository wordsRepository, DaywordRepository daywordRepository)
    {
        this.basicRepository = basicRepository;
        this.daylistRepository = daylistRepository;
        this.wordsRepository = wordsRepository;
        this.daywordRepository = daywordRepository;
    }

    // [1] 기본 제공 단어장 목록
    @Transactional
    public List<BasicResponse> getBasic()
    {
        // basic테이블 + daylist 개수 결과를 Object[] 배열로 조회한다.
        List<Object[]> rawArray = basicRepository.findAllWithDaylistCount();

        // Object[] 배열을 순환하며 BasicResponse 타입으로 리스트를 반환한다.
        return rawArray.stream()
                .map(obj -> {
                    Number countValue = (Number) obj[4];
                    return new BasicResponse(
                            ((Long) obj[0]),    // basicId
                            (String) obj[1],    // basicTitle
                            (String) obj[2],    // basicType
                            (String) obj[3],    // basicOfferedBy
                            ((Long) obj[4]));   // daylistCount
                }).collect(Collectors.toList());
    }

    // [2] DAY 리스트 목록
    @Transactional
    public List<DaylistResponse> getDaylist(Long basicId)
    {
        // daylist 테이블 + dayword 개수 결과를 Object[] 배열로 조회한다.
        List<Object[]> rawArray = daylistRepository.findAllWithDaywordCount(basicId);

        // Object[] 배열을 순환하며 DaylistResponse 타입으로 리스트를 반환한다.
        return rawArray.stream()
                .map(obj ->
                {
                    Number countValue = (Number) obj[4]; // MySQL의 DATETIME을 JAVA로 가져오면 Timestamp로 매핑된다.
                    Timestamp timestamp = (Timestamp) obj[3];
                    return new DaylistResponse(
                            ((Long) obj[0]),
                            (Long) obj[1],
                            (String) obj[2],
                            timestamp != null? timestamp.toLocalDateTime() : null,
                            (Long) obj[4]);
                }).collect(Collectors.toList());
    }

    // [3] 단어 상세 정보
    @Transactional
    public List<WordsResponse> getWords(Long wordId)
    {
        Optional<Words> wordsList = wordsRepository.findById(wordId);

        return wordsList.stream()
                .map(word -> {
                    MnemonicExample mnemonic = word.getMnemonicExample();
                    return new WordsResponse(
                            word.getWordId(),
                            mnemonic.getWord(),
                            mnemonic.getMeaning(),
                            mnemonic.getPartOfSpeech(),
                            mnemonic.getPhoneticUs(),
                            mnemonic.getPhoneticUk(),
                            mnemonic.getPhoneticAus(),
                            mnemonic.getAssociation(),
                            mnemonic.getImageUrl(),
                            mnemonic.getExampleEng(),
                            mnemonic.getExampleKor());
                }).collect(Collectors.toList());
    }

    // [4] DAY 단어 목록
    public List<DaywordResponse> getDayword(Long daylist_id)
    {
        List<Object[]> daywordList = daywordRepository.findAllByDaylistId(daylist_id);

        return daywordList.stream()
                .map(obj ->
                {
                    return new DaywordResponse(
                            ((Long) obj[0]),
                            (String) obj[1],
                            (String) obj[2],
                            (String) obj[3]);
                }).collect(Collectors.toList());
    }
}
