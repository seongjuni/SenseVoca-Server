package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.basicword.*;
import com.sensevoca.backend.service.BasicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "basic", description = "기본 제공 단어장 API")
@RequestMapping("/api/basic")
@RequiredArgsConstructor
public class BasicController {

    private final BasicService basicService;

    // [1] [BASIC] 기본 제공 단어장 목록 조회
    @GetMapping("/list")
    @Operation(summary = "기본 제공 단어장 목록 & Daylist 개수")
    public ResponseEntity<ResponseDTO<List<GetBasicResponse>>> getBasic()
    {
        List<GetBasicResponse> result = basicService.getBasic();

        ResponseDTO<List<GetBasicResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("기본 제공 단어장 조회 성공");
        response.setData(result);
        return ResponseEntity.ok(response);
    }

    // [2-1] [DAYLIST] daylist 목록 조회 + dayword 수
    @GetMapping("/{basic_id}/daylist/")
    @Operation(summary = "Daylist 목록 & Dayword 개수")
    public ResponseEntity<ResponseDTO<List<GetDaylistResponse>>> getDaylist(@PathVariable("basic_id") Long basicId)
    {
        List<GetDaylistResponse> result = basicService.getDaylist(basicId);

        ResponseDTO<List<GetDaylistResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("Daylist 목록 조회 성공");
        response.setData(result);
        return ResponseEntity.ok(response);
    }

    // [2-2] [DAYLIST] 마지막 접근 시간 UPDATE
    @PatchMapping("{daylist_id}/accessed")
    @Operation(summary = "Daylist 목록 & 최근 접근 시간 업데이터 & Dayword 개수")
    public ResponseEntity<ResponseDTO<Void>> updateDatetime(
            @PathVariable("daylist_id") Long daylistId,
            @RequestBody UpdateDatetimeRequest request)
    {
        basicService.updateDatetime(daylistId, request.getLatestAccessedAt());

        ResponseDTO<Void> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("Daylist 마지막 접근 시간 업데이트 성공");
        return ResponseEntity.ok(response);
    }

    // [3] [DAYWORD] dayword 목록 조회
    @GetMapping("{daylist_id}/dayword")
    @Operation(summary = "Dayword 목록 조회")
    public ResponseEntity<ResponseDTO<List<GetDaywordResponse>>> getDayword(@PathVariable("daylist_id") Long daylistId)
    {
        List<GetDaywordResponse> result = basicService.getDayword(daylistId);

        ResponseDTO<List<GetDaywordResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("Daylist에 해당하는 Dayword 목록 조회 성공");
        response.setData(result);
        return ResponseEntity.ok(response);
    }

    // [4] [BASIC WORD] 단어 상세 정보 조회
    @PostMapping("/basic_word/{country}")
    @Operation(summary = "daywordId로 기본 제공 단어 상세 정보 조회")
    public ResponseEntity<ResponseDTO<List<GetBasicWordResponse>>> getBasicWord(
            @RequestBody BasicWordIdRequest request,
            @PathVariable("country") String country)
    {
        List<GetBasicWordResponse> result = basicService.getBasicWord(request.getDaywordId(), country);

        ResponseDTO<List<GetBasicWordResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("기본 제공 단어 상세 정보 조회 성공");
        response.setData(result);
        return ResponseEntity.ok(response);
    }
}
