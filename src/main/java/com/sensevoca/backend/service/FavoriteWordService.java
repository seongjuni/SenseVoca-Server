package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.*;
import com.sensevoca.backend.dto.basicword.GetBasicWordResponse;
import com.sensevoca.backend.dto.favoriteword.FavoriteWordDetailResponse;
import com.sensevoca.backend.dto.favoriteword.GetFavoriteWordsResponse;
import com.sensevoca.backend.dto.favoriteword.WordIdTypeRequest;
import com.sensevoca.backend.dto.mywordbook.GetMyWordInfoResponse;
import com.sensevoca.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteWordService {

    private final FavoriteWordRepository favoriteWordRepository;
    private final UserRepository userRepository;
    private final MyWordMnemonicRepository myWordMnemonicRepository;
    private final BasicWordRepository basicWordRepository;
    private final DaywordRepository daywordRepository;
    private final MyWordRepository myWordRepository;

    public List<GetFavoriteWordsResponse> getFavoriteWordsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        List<FavoriteWord> favorites = favoriteWordRepository.findAllByUser_UserIdOrderByCreatedAtAsc(userId);

        return favorites.stream()
                .map(fav -> {
                    if (fav.getMyWordMnemonic() != null) {
                        WordInfo wordInfo = fav.getMyWordMnemonic().getWordInfo();
                        return GetFavoriteWordsResponse.builder()
                                .wordId(fav.getMyWordMnemonic().getMyWordMnemonicId())
                                .word(wordInfo.getWord())
                                .meaning(fav.getMyWordMnemonic().getMeaning())
                                .type("MY")
                                .build();
                    } else {
                        WordInfo wordInfo = fav.getDayword().getBasicWord().getWordInfo();
                        return GetFavoriteWordsResponse.builder()
                                .wordId(fav.getDayword().getDaywordId())
                                .word(wordInfo.getWord())
                                .meaning(fav.getDayword().getBasicWord().getMeaning())
                                .type("BASIC")
                                .build();
                    }
                })
                .toList();
    }

    public void addMyWordFavorite(Long myWordMnemonicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 니모닉 조회
        MyWordMnemonic myWordMnemonic = myWordMnemonicRepository.findById(myWordMnemonicId)
                .orElseThrow(() -> new IllegalArgumentException("해당 나만의 단어가 존재하지 않습니다."));

        if (favoriteWordRepository.existsByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(userId, myWordMnemonicId)) {
            throw new IllegalStateException("이미 즐겨찾기 되어 있음");
        }

        FavoriteWord favorite = FavoriteWord.builder()
                .user(user)
                .myWordMnemonic(myWordMnemonic)
                .build();

        favoriteWordRepository.save(favorite);
    }

    public void addBasicWordFavorite(Long daywordId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 1. User 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 기본 단어장 조회
        Dayword dayword = daywordRepository.findById(daywordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기본 단어가 존재하지 않습니다."));

        if (favoriteWordRepository.existsByUser_UserIdAndDayword_DaywordId(userId, daywordId))
            throw new IllegalStateException("이미 즐겨찾기 되어 있음");

        FavoriteWord favorite = FavoriteWord.builder()
                .user(user)
                .dayword(dayword)
                .build();

        favoriteWordRepository.save(favorite);
    }

    public void removeMyWordFavorite(Long myWordMnemonicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        Optional<FavoriteWord> optionalFavorite = favoriteWordRepository
                .findByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(userId, myWordMnemonicId);

        FavoriteWord favorite = optionalFavorite
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기 항목이 존재하지 않습니다."));

        favoriteWordRepository.delete(favorite);
    }

    public void removeBasicWordFavorite(Long daywordId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        Optional<FavoriteWord> optionalFavorite = favoriteWordRepository.
                findByUser_UserIdAndDayword_DaywordId(userId, daywordId);

        FavoriteWord favorite = optionalFavorite
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기 항목이 존재하지 않습니다."));

        favoriteWordRepository.delete(favorite);
    }

    public List<FavoriteWordDetailResponse> getFavoriteWordInfoList(
            List<WordIdTypeRequest> wordIdTypes, String phoneticType) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        List<FavoriteWordDetailResponse> result = new ArrayList<>();

        // 요청에서 MY 타입의 MyWordMnemonicId 수집
        List<Long> myMnemonicIds = wordIdTypes.stream()
                .filter(w -> "MY".equalsIgnoreCase(w.getType()))
                .map(WordIdTypeRequest::getWordId)
                .toList();

        // 해당하는 즐겨찾기 MY 니모닉 ID
        Set<Long> favoriteMyMnemonicIds = favoriteWordRepository
                .findAllByUser_UserIdAndMyWordMnemonic_MyWordMnemonicIdIn(userId, myMnemonicIds)
                .stream()
                .map(fav -> fav.getMyWordMnemonic().getMyWordMnemonicId())
                .collect(Collectors.toSet());

        // BASIC 즐겨찾기 ID 수집
        Set<Long> favoriteDaywordIds = favoriteWordRepository
                .findAllByUser_UserIdOrderByCreatedAtAsc(userId).stream()
                .map(fav -> {
                    Dayword dayword = fav.getDayword();
                    return dayword != null ? dayword.getDaywordId() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (WordIdTypeRequest req : wordIdTypes) {
            String type = req.getType().toUpperCase();

            if ("MY".equals(type)) {
                MyWordMnemonic mnemonic = myWordMnemonicRepository.findById(req.getWordId())
                        .orElseThrow(() -> new IllegalArgumentException("MY 니모닉이 존재하지 않습니다."));

                WordInfo wordInfo = mnemonic.getWordInfo();

                String phoneticSymbol = switch (phoneticType.toLowerCase()) {
                    case "uk" -> wordInfo.getPhoneticUk();
                    case "aus" -> wordInfo.getPhoneticAus();
                    default -> wordInfo.getPhoneticUs();
                };

                GetMyWordInfoResponse data = GetMyWordInfoResponse.builder()
                        .mnemonicId(mnemonic.getMyWordMnemonicId())
                        .word(wordInfo.getWord())
                        .meaning(mnemonic.getMeaning())
                        .phoneticSymbol(phoneticSymbol)
                        .association(mnemonic.getAssociation())
                        .imageUrl(mnemonic.getImageUrl())
                        .exampleEng(mnemonic.getExampleEng())
                        .exampleKor(mnemonic.getExampleKor())
                        .favorite(favoriteMyMnemonicIds.contains(mnemonic.getMyWordMnemonicId()))
                        .build();

                result.add(new FavoriteWordDetailResponse("MY", data));
            }

            else if ("BASIC".equals(type)) {
                Dayword dayword = daywordRepository.findById(req.getWordId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 dayword가 존재하지 않습니다."));

                BasicWord basicWord = dayword.getBasicWord();
                WordInfo wordInfo = basicWord.getWordInfo();

                String phonetic = switch (phoneticType.toLowerCase()) {
                    case "uk" -> wordInfo.getPhoneticUk();
                    case "aus" -> wordInfo.getPhoneticAus();
                    default -> wordInfo.getPhoneticUs();
                };

                GetBasicWordResponse data = GetBasicWordResponse.builder()
                        .daywordId(dayword.getDaywordId())
                        .word(wordInfo.getWord())
                        .meaning(basicWord.getMeaning())
                        .association(basicWord.getAssociation())
                        .imageUrl(basicWord.getImageUrl())
                        .exampleEng(basicWord.getExampleEng())
                        .exampleKor(basicWord.getExampleKor())
                        .phonetic(phonetic)
                        .favorite(favoriteDaywordIds.contains(dayword.getDaywordId()))
                        .build();

                result.add(new FavoriteWordDetailResponse("BASIC", data));
            }
        }

        return result;
    }

}
