package com.sensevoca.backend.service;

import com.sensevoca.backend.dto.mywordbook.AddMyWordbookRequest;
import com.sensevoca.backend.dto.mywordbook.MyWordRequest;
import com.sensevoca.backend.domain.*;
import com.sensevoca.backend.repository.MnemonicExampleRepository;
import com.sensevoca.backend.repository.MyWordRepository;
import com.sensevoca.backend.repository.MyWordbookRepository;
import com.sensevoca.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyWordbookService {

    private final UserRepository userRepository;
    private final MyWordbookRepository myWordbookRepository;
    private final MnemonicExampleRepository mnemonicExampleRepository;
    private final MyWordRepository myWordRepository;
    private final AiService aiService;

    public Long addMyWordbook(AddMyWordbookRequest request) {
        // 1. 유저 조회
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 유저의 관심사 조회
        Interest interest = user.getInterest();
        if (interest == null) {
            throw new IllegalStateException("유저의 관심사가 설정되지 않았습니다.");
        }

        // 우선 관심사랑 단어가 같은 경선식 찾고 없으면 요청 있으면 가져다 씀
        // 2. Ai에게 단어, 뜻, 관심사를 전달하여 경선식 문장, 이미지 프롬프트, 품사, 발음기호, 영어 예문, 예문 번역 생성 요청
        // 비동기로 처리 순서는 유지


        MyWordbook wordbook = myWordbookRepository.save(
                MyWordbook.builder()
                        .user(user)
                        .title(request.getTitle())
                        .wordCount(request.getWords().size())
                        .build()
        );

        // 3. 단어 목록 순회하며 예문 처리 및 관계 저장
        for (MyWordRequest wordItem : request.getWords()) {
            // 3-1. 기존 예문이 있으면 사용, 없으면 AI로 생성
            MnemonicExample example = findOrGenerateMnemonicExample(
                    wordItem.getWord(),
                    interest.getId(),
                    wordItem.getMeaning()
            );

            myWordRepository.save(
                    MyWord.builder()
                            .wordbook(wordbook)
                            .mnemonic(example)
                            .build()
            );
        }

        return wordbook.getId();
    }

    public MnemonicExample findOrGenerateMnemonicExample(String word, Long interestId, String meaning) {

        Optional<MnemonicExample> optionalExample =
                mnemonicExampleRepository.findByWordAndInterestIdAndMeaning(word, interestId, meaning);

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
        MnemonicExample aiGenerated = aiService.generateMnemonicExample(word, interestId, meaning);

        // 3. 저장 후 반환
        return mnemonicExampleRepository.save(aiGenerated);
    }
}
