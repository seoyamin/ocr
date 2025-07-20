package com.haru.cpn.controller;

import com.haru.cpn.dto.AccountInfoResponseDto;
import com.haru.cpn.service.TesseractOcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ocr/tesseract")
public class TesseractOcrController {
    private final TesseractOcrService tesseractOcrService;

    @PostMapping("/account")
    public ResponseEntity<AccountInfoResponseDto> extractAccount(@RequestParam("file") MultipartFile file) {
        try {
            AccountInfoResponseDto result = tesseractOcrService.extractAccountInfo(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
