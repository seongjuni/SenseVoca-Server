package com.sensevoca.backend.controller;

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
}
