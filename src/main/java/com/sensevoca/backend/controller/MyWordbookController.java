package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.mywordbook.AddMyWordbookRequest;
import com.sensevoca.backend.dto.mywordbook.GetMyWordListResponse;
import com.sensevoca.backend.dto.mywordbook.GetMyWordbookListResponse;
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
    public ResponseEntity<ResponseDTO<Long>> addMyWordbook(
            @RequestBody AddMyWordbookRequest myWordbookRequest) {

        Long wordbookId = myWordbookService.addMyWordbook(myWordbookRequest);

        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("");
        response.setData(wordbookId);

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
}
