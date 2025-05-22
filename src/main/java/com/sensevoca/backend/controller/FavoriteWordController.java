package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.service.FavoriteWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "favoritewords", description = "즐겨찾기 단어 API")
@RestController
@RequestMapping("/api/favoritewords")
@RequiredArgsConstructor
public class FavoriteWordController {

    private final FavoriteWordService favoriteWordService;

    @PostMapping("/add-myword")
    @Operation(summary = "내 단어 즐겨찾기 등록")
    public ResponseEntity<ResponseDTO<Void>> addMyWordFavorite(@RequestParam Long myWordMnemonicId) {
        favoriteWordService.addMyWordFavorite(myWordMnemonicId);

        ResponseDTO<Void> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("나만의 단어 즐겨찾기 등록 완료");
        response.setData();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

//    @PostMapping("/add-basicword")
//    @Operation(summary = "기본 단어 즐겨찾기 등록")
//    public ResponseEntity<ResponseDTO<Void>> addBasicWordFavorite(@RequestParam Long basicWordId) {
//    }
}
