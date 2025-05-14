package com.sensevoca.backend.service;

import com.sensevoca.backend.dto.mywordbook.*;
import com.sensevoca.backend.domain.*;
import com.sensevoca.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyWordbookService {

    private final UserRepository userRepository;
    private final WordInfoRepository wordInfoRepository;
    private final MyWordbookRepository myWordbookRepository;
    private final MyWordMnemonicRepository myWordMnemonicRepository;
    private final WordInfoService wordInfoService;
    private final AiService aiService;
    private final MyWordRepository myWordRepository;

    public Boolean addMyWordbook(AddMyWordbookRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 유저의 관심사 조회
        Interest interest = user.getInterest();
        if (interest == null) {
            throw new IllegalStateException("유저의 관심사가 설정되지 않았습니다.");
        }

        MyWordbook wordbook = myWordbookRepository.save(
                MyWordbook.builder()
                        .user(user)
                        .title(request.getTitle())
                        .wordCount(request.getWords().size())
                        .build()
        );

        // 3. 단어 목록 순회하며 예문 처리 및 관계 저장
        for (MyWordRequest wordItem : request.getWords()) {
            // 1. 단어 정보 준비
            WordInfo wordInfo;
            if (wordItem.getWordId() == null) {
                wordInfo = wordInfoService.findOrGenerateWordInfo(wordItem.getWord(), wordItem.getMeaning());
            } else {
                wordInfo = wordInfoRepository.findById(wordItem.getWordId())
                        .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다: " + wordItem.getWordId()));
            }

            // 2. 연상 예문, 이미지, 영어/한글 예문 준비
            MyWordMnemonic myWordMnemonic = findOrGenerateMnemonicExample(
                    wordInfo,
                    interest.getInterestId(),
                    wordItem.getMeaning(),
                    wordbook
                    );

            myWordRepository.save(
                    MyWord.builder()
                            .myWordbook(wordbook)
                            .myWordMnemonic(myWordMnemonic)
                            .build()
            );
        }

        return true;
    }

    public MyWordMnemonic findOrGenerateMnemonicExample(WordInfo wordInfo, Long interestId, String meaning, MyWordbook myWordbook) {

        Optional<MyWordMnemonic> optionalExample =
                myWordMnemonicRepository.findByWordInfoWordIdAndInterestInterestIdAndMeaning(wordInfo.getWordId(), interestId, meaning);

        if (optionalExample.isPresent()) {
            return optionalExample.get(); // ✅ 동일한 예문이 있으면 그대로 반환
        }

//        // 1. 관심사 + 단어 일치하는 니모닉 예문 있는지 조회
//        List<MnemonicExample> existingExamples = mnemonicExampleRepository.findAllByWordAndInterestId(word, interestId);
//
//        // 2. 뜻(meaning) 유사도 비교 - 예시로 cosine similarity 사용
//        for (MnemonicExample example : existingExamples) {
//            double similarity = embeddingService.getSimilarity(meaning, example.getMeaning());
//            if (similarity >= 0.9) { // ✅ 유사한 뜻이 있으면 반환
//                return example;
//            }
//        }

        // 2. 없으면 AI에게 요청해서 생성
        MyWordMnemonic aiGenerated = aiService.generateMnemonicExample(wordInfo, interestId, meaning);

        // 3. 저장 후 반환
        return myWordMnemonicRepository.save(aiGenerated);
    }

    public List<GetMyWordbookListResponse> getMyWordbookList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        List<MyWordbook> wordbooks = myWordbookRepository.findAllByUserUserId(userId);

        return wordbooks.stream()
                .map(wordbook -> GetMyWordbookListResponse.builder()
                        .id(wordbook.getMyWordbookId())
                        .title(wordbook.getTitle())
                        .wordCount(wordbook.getWordCount())
                        .lastAccessedAt(wordbook.getLastAccessedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<GetMyWordListResponse> getMyWordList(Long wordbookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        MyWordbook wordbook = myWordbookRepository.findById(wordbookId)
                .orElseThrow(() -> new IllegalArgumentException("단어장이 존재하지 않습니다."));

        if (!wordbook.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("자신의 단어장만 조회할 수 있습니다.");
        }

        // 마지막 접속일 업데이트
        wordbook.updateLastAccessed();
        myWordbookRepository.save(wordbook);

        List<MyWord> myWords = myWordRepository.findAllByMyWordbookMyWordbookId(wordbookId);

        List<GetMyWordListResponse> result = myWords.stream()
                .map(myWord -> {
                    MyWordMnemonic m = myWord.getMyWordMnemonic();
                    return new GetMyWordListResponse(
                            myWord.getMyWordId(),
                            m.getMyWordMnemonicId(),
                            m.getWordInfo().getWord(),
                            m.getMeaning()
                    );
                })
                .collect(Collectors.toList());

        // 최종 반환 리스트 출력
        System.out.println("==== 변환된 단어 리스트 ====");
        for (GetMyWordListResponse wordResponse : result) {
            System.out.println(wordResponse);
        }

        return result;
    }

    public List<GetMyWordInfoResponse> getMyWordInfoList(List<Long> wordIds, String phoneticType) {
        List<MyWord> myWords = myWordRepository.findAllById(wordIds);

        return myWords.stream()
                .map(myWord -> {
                    MyWordMnemonic mnemonic = myWord.getMyWordMnemonic();
                    WordInfo wordInfo = mnemonic.getWordInfo();
                    String phoneticSymbol = switch (phoneticType.toLowerCase()) {
                        case "us" -> wordInfo.getPhoneticUs();
                        case "uk" -> wordInfo.getPhoneticUk();
                        case "aus" -> wordInfo.getPhoneticAus();
                        default -> wordInfo.getPhoneticUs(); // 기본 미국식
                    };

                    return GetMyWordInfoResponse.builder()
                            .wordId(myWord.getMyWordId())
                            .word(wordInfo.getWord())
                            .meaning(mnemonic.getMeaning())
                            .phoneticSymbol(phoneticSymbol)
                            .association(mnemonic.getAssociation())
                            .imageUrl(mnemonic.getImageUrl())
                            .exampleEng(mnemonic.getExampleEng())
                            .exampleKor(mnemonic.getExampleKor())
//                            .favorite(false) // 즐겨찾기 로직은 따로
                            .build();
                })
                .toList();
    }
}
