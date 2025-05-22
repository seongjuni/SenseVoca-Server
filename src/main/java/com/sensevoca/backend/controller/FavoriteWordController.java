package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.favoriteword.GetFavoriteWordsResponse;
import com.sensevoca.backend.dto.mywordbook.GetMyWordbookListResponse;
import com.sensevoca.backend.service.FavoriteWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "favoritewords", description = "즐겨찾기 단어 API")
@RestController
@RequestMapping("/api/favoritewords")
@RequiredArgsConstructor
public class FavoriteWordController {

    private final FavoriteWordService favoriteWordService;

    @GetMapping("/list")
    @Operation(summary = "즐겨찾기 단어 리스트")
    public ResponseEntity<ResponseDTO<List<GetFavoriteWordsResponse>>> getFavoriteWords() {

        List<GetFavoriteWordsResponse> favorites = favoriteWordService.getFavoriteWordsByUser();

        ResponseDTO<List<GetFavoriteWordsResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("즐겨찾기 단어 목록 조회 성공");
        response.setData(favorites);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-myword/{myWordMnemonicId}")
    @Operation(summary = "내 단어 즐겨찾기 등록")
    public ResponseEntity<ResponseDTO<Void>> addMyWordFavorite(@PathVariable Long myWordMnemonicId) {
        favoriteWordService.addMyWordFavorite(myWordMnemonicId);

        ResponseDTO<Void> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("나만의 단어 즐겨찾기 등록 완료");

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

//    @PostMapping("/add-basicword")
//    @Operation(summary = "기본 단어 즐겨찾기 등록")
//    public ResponseEntity<ResponseDTO<Void>> addBasicWordFavorite(@RequestParam Long basicWordId) {
//    }

    @DeleteMapping("/remove-myword/{myWordMnemonicId}")
    @Operation(summary = "내 단어 즐겨찾기 삭제")
    public ResponseEntity<ResponseDTO<Void>> removeMyWordFavorite(@PathVariable Long myWordMnemonicId) {
        favoriteWordService.removeMyWordFavorite(myWordMnemonicId);

        ResponseDTO<Void> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("나만의 단어 즐겨찾기 삭제 완료");

        return ResponseEntity.ok(response);
    }
}
