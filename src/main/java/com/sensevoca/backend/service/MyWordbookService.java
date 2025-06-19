package com.sensevoca.backend.service;

import com.sensevoca.backend.dto.ai.CreateMnemonicExampleResponse;
import com.sensevoca.backend.dto.mywordbook.*;
import com.sensevoca.backend.domain.*;
import com.sensevoca.backend.dto.wordinfo.GetWordInfosResponse;
import com.sensevoca.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyWordbookService {

    private final UserRepository userRepository;
    private final WordInfoRepository wordInfoRepository;
    private final MyWordbookRepository myWordbookRepository;
    private final MyWordMnemonicRepository myWordMnemonicRepository;
    private final MyWordRepository myWordRepository;
    private final FavoriteWordRepository favoriteWordRepository;
    private final BasicWordRepository basicWordRepository;
    private final WordInfoService wordInfoService;
    private final AiService aiService;

    @Transactional
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
        long startTime = System.currentTimeMillis();

//        /*동기*/
//        for (MyWordRequest wordItem : request.getWords()) {
//            WordInfo wordInfo = (wordItem.getWordId() == null)
//                    ? wordInfoService.findOrGenerateWordInfo(wordItem.getWord(), wordItem.getMeaning())
//                    : wordInfoRepository.findById(wordItem.getWordId())
//                    .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다: " + wordItem.getWordId()));
//
//            MyWordMnemonic myWordMnemonic = findOrGenerateMnemonicExample(
//                    wordInfo,
//                    interest.getInterestId(),
//                    wordItem.getMeaning()
//            );
//
//            myWordRepository.save(
//                    MyWord.builder()
//                            .myWordbook(wordbook)
//                            .myWordMnemonic(myWordMnemonic)
//                            .build()
//            );
//        }
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("⏱ 동기 처리 시간(ms): " + (endTime - startTime));
//        /*동기*/

        /*비동기*/
        List<MyWordRequest> words = request.getWords();

        List<CompletableFuture<MyWord>> futures = words.stream()
                .map(wordItem -> CompletableFuture.supplyAsync(() -> {
                    try {
                        WordInfo wordInfo = (wordItem.getWordId() == null)
                                ? wordInfoService.findOrGenerateWordInfo(wordItem.getWord(), wordItem.getMeaning())
                                : wordInfoRepository.findById(wordItem.getWordId())
                                .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다: " + wordItem.getWordId()));

                        MyWordMnemonic mnemonic = findOrGenerateMnemonicExample(
                                wordInfo,
                                interest.getInterestId(),
                                wordItem.getMeaning()
                        );

                        return MyWord.builder()
                                .myWordbook(wordbook)
                                .myWordMnemonic(mnemonic)
                                .build();
                    } catch (Exception e) {
                        System.out.println("🟥 단어 처리 실패: " + wordItem.getWord() + " / 이유: " + e.getMessage());
                        return null;  // 실패 시 null 반환
                    }
                }))
                .toList();

        List<MyWord> myWords = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)  // 실패한 단어(null) 제외
                .toList();

        for (MyWord word : myWords) {
            myWordRepository.save(word);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("🚀 비동기 처리 시간(ms): " + (endTime - startTime));
        /*비동기*/

        return true;
    }

    public MyWordMnemonic findOrGenerateMnemonicExample(WordInfo wordInfo, Long interestId, String meaning) {

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
                        .createdAt(wordbook.getCreatedAt())
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
                            myWord.getMyWordMnemonic().getMyWordMnemonicId(),
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

    public List<GetMyWordInfoResponse> getMyWordInfoList(List<Long> mnemonicIds, String phoneticType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 1. 니모닉 ID로 전체 조회 (순서 보장 안됨)
        List<MyWordMnemonic> mnemonics = myWordMnemonicRepository.findAllById(mnemonicIds);

        // 2. Map으로 변환: id → mnemonic
        Map<Long, MyWordMnemonic> mnemonicMap = mnemonics.stream()
                .collect(Collectors.toMap(MyWordMnemonic::getMyWordMnemonicId, m -> m));

        // 3. 즐겨찾기된 ID 조회
        Set<Long> favoriteMnemonicIds = favoriteWordRepository
                .findAllByUser_UserIdAndMyWordMnemonic_MyWordMnemonicIdIn(userId, mnemonicIds)
                .stream()
                .map(fav -> fav.getMyWordMnemonic().getMyWordMnemonicId())
                .collect(Collectors.toSet());

        // 4. 원래 전달받은 ID 순서대로 정렬 및 응답 생성
        return mnemonicIds.stream()
                .map(id -> {
                    MyWordMnemonic mnemonic = mnemonicMap.get(id);
                    if (mnemonic == null) return null; // 혹은 throw 예외 처리

                    WordInfo wordInfo = mnemonic.getWordInfo();
                    String phoneticSymbol = switch (phoneticType.toLowerCase()) {
                        case "uk" -> wordInfo.getPhoneticUk();
                        case "aus" -> wordInfo.getPhoneticAus();
                        default -> wordInfo.getPhoneticUs();
                    };

                    boolean isFavorite = favoriteMnemonicIds.contains(id);

                    return GetMyWordInfoResponse.builder()
                            .mnemonicId(id)
                            .word(wordInfo.getWord())
                            .meaning(mnemonic.getMeaning())
                            .phoneticSymbol(phoneticSymbol)
                            .association(mnemonic.getAssociation())
                            .imageUrl(mnemonic.getImageUrl())
                            .exampleEng(mnemonic.getExampleEng())
                            .exampleKor(mnemonic.getExampleKor())
                            .favorite(isFavorite)
                            .build();
                })
                .filter(Objects::nonNull) // 혹시 null이 있을 경우 제거
                .toList();
    }

    public List<GetRandomWordResponse> getRandomMyWords(int count) {
        List<GetWordInfosResponse> allWords = basicWordRepository.findAll().stream()
                .map(basicWord -> {
                    WordInfo wordInfo = basicWord.getWordInfo();
                    return GetWordInfosResponse.builder()
                            .wordId(wordInfo.getWordId())
                            .word(wordInfo.getWord())
                            .meaning(basicWord.getMeaning())
                            .build();
                })
                .collect(Collectors.toList());

        // 섞기
        Collections.shuffle(allWords);

        // count만큼 잘라서 변환
        return allWords.stream()
                .limit(count)
                .map(word -> GetRandomWordResponse.builder()
                        .wordId(word.getWordId())
                        .word(word.getWord())
                        .meaning(word.getMeaning())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMyWord(Long wordbookId, Long wordId) {
        // 1. 단어 존재 여부 및 소속 확인
        MyWord myWord = myWordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단어입니다."));

        // 2. 단어장 단어 존재 여부 확인
        if (!myWord.getMyWordbook().getMyWordbookId().equals(wordbookId)) {
            throw new IllegalArgumentException("단어가 해당 단어장에 속하지 않습니다.");
        }

        // 3. 삭제
        myWordRepository.delete(myWord);
    }

    @Transactional
    public void deleteMyWordbook(Long wordbookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        MyWordbook wordbook = myWordbookRepository.findById(wordbookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단어장입니다."));

        if (!wordbook.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("자신의 단어장만 삭제할 수 있습니다.");
        }

        // 단어는 DB가 자동 삭제 처리
        myWordbookRepository.delete(wordbook);
    }

    @Transactional
    public boolean renameMyWordbook(Long wordbookId, String title) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        MyWordbook wordbook = myWordbookRepository.findById(wordbookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단어장입니다."));

        if (!wordbook.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("자신의 단어장만 수정할 수 있습니다.");
        }

        wordbook.updateTitle(title);
        return true;
    }

    @Transactional
    public RegenerateMnemonicExampleResponse regenerateMnemonicExample(Long wordId, String word) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 1. 기존 니모닉 예문 조회
        MyWordMnemonic oldMnemonic = myWordMnemonicRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예문(Mnemonic)을 찾을 수 없습니다."));

        // 2. AI로 새 예문 생성
        MyWordMnemonic newMnemonic = aiService.regenerateMnemonicExample(
                word,
                oldMnemonic
        );

        // 3. 새 예문 저장
        MyWordMnemonic savedMnemonic = myWordMnemonicRepository.save(newMnemonic);

        // 4. 이 예문을 참조하고 있는 모든 MyWord 조회
        List<MyWord> myWords = myWordRepository
                .findAllByMyWordMnemonic_MyWordMnemonicIdAndMyWordbook_User_UserId(wordId, userId);

        // 5. 각 MyWord의 예문 참조를 새 것으로 교체
        for (MyWord myWord : myWords) {
            myWord.setMyWordMnemonic(savedMnemonic);
        }
        myWordRepository.saveAll(myWords);

        // 6. 응답 반환
        return RegenerateMnemonicExampleResponse.builder()
                .association(savedMnemonic.getAssociation())
                .imageUrl(savedMnemonic.getImageUrl())
                .build();
    }
}
