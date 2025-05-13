package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.*;
import com.sensevoca.backend.dto.basicword.GetBasicResponse;
import com.sensevoca.backend.dto.basicword.GetBasicWordResponse;
import com.sensevoca.backend.dto.basicword.GetDaylistResponse;
import com.sensevoca.backend.dto.basicword.GetDaywordResponse;
import com.sensevoca.backend.repository.BasicRepository;
import com.sensevoca.backend.repository.BasicWordRepository;
import com.sensevoca.backend.repository.DaylistRepository;
import com.sensevoca.backend.repository.DaywordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.results.graph.basic.BasicResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicService {

    private final BasicRepository basicRepository;
    private final DaylistRepository daylistRepository;
    private final DaywordRepository daywordRepository;
    private final BasicWordRepository  basicWordRepository;

    // [1] [BASIC] 기본 제공 단어장 목록 조회
    public List<GetBasicResponse> getBasic()
    {
        List<Basic> basicList = basicRepository.findAll();

        return basicList.stream()
                .map(basic -> {
                    int count = daylistRepository.countByBasicBasicId(basic.getBasicId());
                    return GetBasicResponse.builder()
                            .basicId(basic.getBasicId())
                            .basicTitle(basic.getBasicTitle())
                            .basicType(basic.getBasicType())
                            .basicOfferedBy(basic.getBasicOfferedBy())
                            .daylistCount(count)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // [2] [DAYLIST] 목록 조회
    public List<GetDaylistResponse> getDaylist(Long basicId)
    {
        List<Daylist> dayList = daylistRepository.findAllByBasicBasicId(basicId);

        return dayList.stream()
                .map(daylist ->{
                    int count = daywordRepository.countByDaylistDaylistId(daylist.getDaylistId());
                    return GetDaylistResponse.builder()
                            .daylistId(daylist.getDaylistId())
                            .basicTitle(daylist.getBasic().getBasicTitle())
                            .daylistTitle(daylist.getDaylistTitle())
                            .daywordCount(count)
                            .latestAccessedAt(daylist.getLatestAccessedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateDatetime(Long daylistId, LocalDateTime latestAccessedAt)
    {
        Daylist daylist = daylistRepository.findById(daylistId).orElseThrow(()->new IllegalArgumentException("Daylist가 존재하지 않습니다."));
        daylist.setLatestAccessedAt(latestAccessedAt);
        daylistRepository.save(daylist);
    }

    // [3] [DAYWORD] 목록 조회
    public List<GetDaywordResponse> getDayword(Long daylistId)
    {
        List<Dayword> daywordList = daywordRepository.findAllByDaylistDaylistId(daylistId);

        return daywordList.stream()
                .map(dayword -> {
                    return GetDaywordResponse.builder()
                            .daywordId(dayword.getDaywordId())
                            .daylistTitle(dayword.getDaylist().getDaylistTitle())
                            .word(dayword.getBasicWord().getWordInfo().getWord())
                            .meaning(dayword.getBasicWord().getMeaning())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // [4] [BASIC WORD] 단어 상세 정보 조회
    public List<GetBasicWordResponse> getBasicWord(Long daylistId, String country)
    {
        // 흐름 : daylist -> basicword -> wordinfo
        List<Dayword> daywordDetail = daywordRepository.findAllByDaylistDaylistId(daylistId);

        return daywordDetail.stream().map(dayword -> {
            BasicWord basicWord = dayword.getBasicWord();
            WordInfo wordInfo = basicWord.getWordInfo();

            // 발음 기호 결정
            String phonetic = switch (country.toLowerCase())
            {
                case "us" -> wordInfo.getPhoneticUs();
                case "en" -> wordInfo.getPhoneticUk();
                case "aus" -> wordInfo.getPhoneticAus();
                default -> wordInfo.getPhoneticUs();
            };

            return GetBasicWordResponse.builder()
                    .basicWordId(basicWord.getBasicWordId())
                    .word(wordInfo.getWord())
                    .meaning(basicWord.getMeaning())
                    .association(basicWord.getAssociation())
                    .associationEng(basicWord.getAssociationEng())
                    .imageUrl(basicWord.getImageUrl())
                    .exampleEng(basicWord.getExampleEng())
                    .exampleKor(basicWord.getExampleKor())
                    .phonetic(phonetic)
                    .build();
        }).collect(Collectors.toList());
    }
}
