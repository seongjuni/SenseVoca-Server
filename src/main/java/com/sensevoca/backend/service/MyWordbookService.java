package com.sensevoca.backend.service;

import com.sensevoca.backend.dto.mywordbook.AddMyWordbookRequest;
import com.sensevoca.backend.entity.MyWordbook;
import com.sensevoca.backend.entity.User;
import com.sensevoca.backend.repository.MyWordbookRepository;
import com.sensevoca.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyWordbookService {

    private final UserRepository userRepository;
    private final MyWordbookRepository myWordbookRepository;

    public Long addMyWordbook(AddMyWordbookRequest request) {
        // 1. 유저 조회
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

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

        return null;
    }
}
