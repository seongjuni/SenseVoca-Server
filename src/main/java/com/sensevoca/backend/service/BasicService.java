package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.*;
import com.sensevoca.backend.dto.basicword.*;
import com.sensevoca.backend.repository.BasicRepository;
import com.sensevoca.backend.repository.DaylistRepository;
import com.sensevoca.backend.repository.DaywordRepository;
import com.sensevoca.backend.repository.FavoriteWordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicService {

    private final BasicRepository basicRepository;
    private final DaylistRepository daylistRepository;
    private final DaywordRepository daywordRepository;
    private final FavoriteWordRepository favoriteWordRepository;

    // [1] [BASIC] 기본 제공 단어장 목록 조회 + daylist 수
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

    // [2-1] [DAYLIST] daylist 목록 조회 + dayword 수
    public List<GetDaylistResponse> getDaylist(Long basicId)
    {
        List<Daylist> dayList = daylistRepository.findAllByBasicBasicId(basicId);

        return dayList.stream()
                .map(daylist -> {
                    int count = daywordRepository.countByDaylistDaylistId(daylist.getDaylistId());
                    return GetDaylistResponse.builder()
                            .daylistId(daylist.getDaylistId())
                            .daylistTitle(daylist.getDaylistTitle())
                            .latestAccessedAt(daylist.getLatestAccessedAt())
                            .daywordCount(count)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // [2-2] [DAYLIST] 마지막 접근 시간 UPDATE
    public void updateDatetime(Long daylistId, UpdateDatetimeRequest request)
    {
        Daylist daylist = daylistRepository.findById(daylistId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Daylist가 존재하지 않습니다."));

        daylist.setLatestAccessedAt(request.getLatestAccessedAt());
        daylistRepository.save(daylist);
    }

    @Transactional
    public void updateDatetime(Long daylistId, LocalDateTime latestAccessedAt)
    {
        Daylist daylist = daylistRepository.findById(daylistId).orElseThrow(()->new IllegalArgumentException("Daylist가 존재하지 않습니다."));
        daylist.setLatestAccessedAt(latestAccessedAt);
        daylistRepository.save(daylist);
    }

    // [3] [DAYWORD] dayword 목록 조회
    public List<GetDaywordResponse> getDayword(Long daylistId)
    {
        List<Dayword> daywordList = daywordRepository.findAllByDaylistDaylistId(daylistId);

        return daywordList.stream()
                .map(dayword -> {
                    return GetDaywordResponse.builder()
                            .daywordId(dayword.getDaywordId())
                            .word(dayword.getBasicWord().getWordInfo().getWord())
                            .meaning(dayword.getBasicWord().getMeaning())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // [4] [BASIC WORD] 단어 상세 정보 조회
    public List<GetBasicWordResponse> getBasicWord(List<Long> daywordIdList, String country)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        List<Dayword> daywordDetail = daywordRepository.findAllById(daywordIdList);

        // 즐겨찾기된 myWordMnemonicId를 한 번에 조회
        Set<Long> favoriteBasicIds = favoriteWordRepository
                .findAllByUser_UserIdAndDayword_DaywordIdIn(
                        userId,
                        daywordIdList
                )
                .stream()
                .map(fav -> fav.getDayword().getDaywordId())
                .collect(Collectors.toSet());

        return daywordDetail.stream().map(dayword -> {
            BasicWord basicWord = dayword.getBasicWord();
            WordInfo wordInfo = basicWord.getWordInfo();

            // 발음 기호 결정
            String phonetic = switch (country.toLowerCase())
            {
                case "us" -> wordInfo.getPhoneticUs();
                case "uk" -> wordInfo.getPhoneticUk();
                case "aus" -> wordInfo.getPhoneticAus();
                default -> wordInfo.getPhoneticUs();
            };

            boolean isFavorite = favoriteBasicIds.contains(dayword.getDaywordId());

            return GetBasicWordResponse.builder()
                    .daywordId(dayword.getDaywordId())
                    .word(wordInfo.getWord())
                    .meaning(basicWord.getMeaning())
                    .association(basicWord.getAssociation())
                    .imageUrl(basicWord.getImageUrl())
                    .exampleEng(basicWord.getExampleEng())
                    .exampleKor(basicWord.getExampleKor())
                    .phonetic(phonetic)
                    .favorite(isFavorite)
                    .build();
        }).collect(Collectors.toList());
    }
}
