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
    private final MyWordRepository myWordRepository;

    public List<GetFavoriteWordsResponse> getFavoriteWordsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        List<FavoriteWord> favorites = favoriteWordRepository.findAllByUser_UserId(userId);

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
                        WordInfo wordInfo = fav.getBasicWord().getWordInfo();
                        return GetFavoriteWordsResponse.builder()
                                .wordId(fav.getBasicWord().getBasicWordId())
                                .word(wordInfo.getWord())
                                .meaning(fav.getBasicWord().getMeaning())
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

    public void addBasicWordFavorite(Long basicWordId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 1. User 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 기본 단어장 조회
        BasicWord basicWord = basicWordRepository.findById(basicWordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기본 단어가 존재하지 않습니다."));

        if (favoriteWordRepository.existsByUser_UserIdAndBasicWord_BasicWordId(userId, basicWordId))
            throw new IllegalStateException("이미 즐겨찾기 되어 있음");

        FavoriteWord favorite = FavoriteWord.builder()
                .user(user)
                .basicWord(basicWord)
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

    public void removeBasicWordFavorite(Long basicWordId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        Optional<FavoriteWord> optionalFavorite = favoriteWordRepository.
                findByUser_UserIdAndBasicWord_BasicWordId(userId, basicWordId);

        FavoriteWord favorite = optionalFavorite
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기 항목이 존재하지 않습니다."));

        favoriteWordRepository.delete(favorite);
    }

    public List<FavoriteWordDetailResponse> getFavoriteWordInfoList(
            List<WordIdTypeRequest> wordIdTypes, String phoneticType) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        List<FavoriteWordDetailResponse> result = new ArrayList<>();

        // 미리 즐겨찾기된 MY 단어 예문 ID 추출
        List<Long> myWordIds = wordIdTypes.stream()
                .filter(w -> "MY".equalsIgnoreCase(w.getType()))
                .map(WordIdTypeRequest::getWordId)
                .toList();

        Set<Long> favoriteMyMnemonicIds = favoriteWordRepository
                .findAllByUser_UserIdAndMyWordMnemonic_MyWordMnemonicIdIn(
                        userId,
                        myWordRepository.findAllById(myWordIds).stream()
                                .map(my -> my.getMyWordMnemonic().getMyWordMnemonicId())
                                .toList()
                )
                .stream()
                .map(fav -> fav.getMyWordMnemonic().getMyWordMnemonicId())
                .collect(Collectors.toSet());

        Set<Long> favoriteBasicWordIds = favoriteWordRepository
                .findAllByUser_UserId(userId).stream()
                .map(fav -> {
                    BasicWord basicWord = fav.getBasicWord();
                    return basicWord != null ? basicWord.getBasicWordId() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (WordIdTypeRequest req : wordIdTypes) {
            String type = req.getType().toUpperCase();

            if ("MY".equals(type)) {
                MyWord myWord = myWordRepository.findById(req.getWordId())
                        .orElseThrow(() -> new IllegalArgumentException("MY 단어가 존재하지 않습니다."));

                MyWordMnemonic mnemonic = myWord.getMyWordMnemonic();
                WordInfo wordInfo = mnemonic.getWordInfo();

                String phoneticSymbol = switch (phoneticType.toLowerCase()) {
                    case "uk" -> wordInfo.getPhoneticUk();
                    case "aus" -> wordInfo.getPhoneticAus();
                    default -> wordInfo.getPhoneticUs();
                };

                GetMyWordInfoResponse data = GetMyWordInfoResponse.builder()
                        .wordId(myWord.getMyWordId())
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
                BasicWord basicWord = basicWordRepository.findById(req.getWordId())
                        .orElseThrow(() -> new IllegalArgumentException("기본 단어가 존재하지 않습니다."));

                WordInfo wordInfo = basicWord.getWordInfo();

                String phonetic = switch (phoneticType.toLowerCase()) {
                    case "uk" -> wordInfo.getPhoneticUk();
                    case "aus" -> wordInfo.getPhoneticAus();
                    default -> wordInfo.getPhoneticUs();
                };

                GetBasicWordResponse data = GetBasicWordResponse.builder()
                        .basicWordId(basicWord.getBasicWordId())
                        .word(wordInfo.getWord())
                        .meaning(basicWord.getMeaning())
                        .association(basicWord.getAssociation())
                        .imageUrl(basicWord.getImageUrl())
                        .exampleEng(basicWord.getExampleEng())
                        .exampleKor(basicWord.getExampleKor())
                        .phonetic(phonetic)
                        .favorite(favoriteBasicWordIds.contains(basicWord.getBasicWordId()))
                        .build();

                result.add(new FavoriteWordDetailResponse("BASIC", data));
            }
        }
        return result;
    }

}
