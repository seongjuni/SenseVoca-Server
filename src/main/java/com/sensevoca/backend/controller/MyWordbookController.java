package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.mywordbook.RegenerateMnemonicExampleResponse;
import com.sensevoca.backend.dto.mywordbook.*;
import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.service.MyWordbookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "mywordbooks", description = "나만의 단어 API")
@RestController
@RequestMapping("/api/mywordbooks")
@RequiredArgsConstructor
public class MyWordbookController {
    private final MyWordbookService myWordbookService;

    @PostMapping("/add-book")
    @Operation(summary = "나만의 단어장 생성")
    public ResponseEntity<ResponseDTO<Boolean>> addMyWordbook(
            @RequestBody AddMyWordbookRequest myWordbookRequest) {

        Boolean isAdded = myWordbookService.addMyWordbook(myWordbookRequest);

        ResponseDTO<Boolean> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("");
        response.setData(isAdded);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list")
    @Operation(summary = "나만의 단어장 리스트")
    public ResponseEntity<ResponseDTO<List<GetMyWordbookListResponse>>> getMyWordbookList() {
        List<GetMyWordbookListResponse> wordbooks = myWordbookService.getMyWordbookList();

        ResponseDTO<List<GetMyWordbookListResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("");
        response.setData(wordbooks);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{wordbookId}/words")
    @Operation(summary = "나만의 단어 리스트")
    public ResponseEntity<ResponseDTO<List<GetMyWordListResponse>>> getMyWordList(
            @PathVariable Long wordbookId) {
        List<GetMyWordListResponse> words = myWordbookService.getMyWordList(wordbookId);

        ResponseDTO<List<GetMyWordListResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("");
        response.setData(words);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/myword-info")
    @Operation(summary = "나만의 단어 상세정보")
    public ResponseEntity<ResponseDTO<List<GetMyWordInfoResponse>>> getWordInfoList(
            @RequestBody GetMyWordInfoRequest request) {

        List<GetMyWordInfoResponse> wordInfos = myWordbookService.getMyWordInfoList(request.getWordIds(), request.getPhoneticType());

        ResponseDTO<List<GetMyWordInfoResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("");
        response.setData(wordInfos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{count}/random-myword")
    @Operation(summary = "랜덤 나만의 단어장", description = "사용자의 나만의 단어 중 랜덤으로 N개 반환")
    public ResponseEntity<ResponseDTO<List<GetRandomWordResponse>>> getRandomMyWords(
            @PathVariable int count) {

        List<GetRandomWordResponse> result = myWordbookService.getRandomMyWords(count);

        ResponseDTO<List<GetRandomWordResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("랜덤 단어 반환 성공");
        response.setData(result);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wordbookId}/words/{wordId}")
    @Operation(summary = "나만의 단어 삭제")
    public ResponseEntity<ResponseDTO<Void>> deleteMyWord(
            @PathVariable Long wordbookId,
            @PathVariable Long wordId) {

        myWordbookService.deleteMyWord(wordbookId, wordId);

        ResponseDTO<Void> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("단어 삭제 완료");
        response.setData(null);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wordbookId}")
    @Operation(summary = "나만의 단어장 삭제")
    public ResponseEntity<ResponseDTO<Void>> deleteMyWordbook(@PathVariable Long wordbookId) {
        myWordbookService.deleteMyWordbook(wordbookId);

        ResponseDTO<Void> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("단어장 삭제 완료");
        response.setData(null);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{wordbookId}/rename")
    @Operation(summary = "나만의 단어장 이름 수정")
    public ResponseEntity<ResponseDTO<Boolean>> renameMyWordbook(
            @PathVariable Long wordbookId,
            @RequestBody RenameMyWordbookRequest request) {

        boolean renamed = myWordbookService.renameMyWordbook(wordbookId, request.getTitle());

        ResponseDTO<Boolean> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("단어장 이름 수정 완료");
        response.setData(renamed);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/regenerate-example")
    @Operation(summary = "마음에 들지 않는 예문 다시 생성", description = "단어 ID와 뜻을 기반으로 새로운 예문을 생성합니다.")
    public ResponseEntity<ResponseDTO<RegenerateMnemonicExampleResponse>> regenerateExample(
            @RequestBody RegenerateMnemonicExampleRequest request) {

        RegenerateMnemonicExampleResponse newExample = myWordbookService.regenerateMnemonicExample(request.getWordId(), request.getWord());

        ResponseDTO<RegenerateMnemonicExampleResponse> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("예문 재생성 성공");
        response.setData(newExample);

        return ResponseEntity.ok(response);
    }
}
