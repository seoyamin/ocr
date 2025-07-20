package com.haru.cpn.service;

import com.haru.cpn.dto.AccountInfoResponseDto;
import com.haru.cpn.util.ImagePreprocessor;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TesseractOcrService {
    public static final String[] BANKS = {
            "국민은행", "신한은행", "우리은행", "하나은행", "농협", "기업은행", "새마을금고",
            "카카오뱅크", "케이뱅크", "토스뱅크", "수협", "SC제일은행", "씨티은행"
    };

    public static final String TESSERACT_DATA_PATH = "C:/Program Files/tesseract-5.5.1/tessdata";

    public AccountInfoResponseDto extractAccountInfo(MultipartFile file) throws Exception {
        File tempFile = File.createTempFile("upload-", ".png");
        file.transferTo(tempFile);
        ImagePreprocessor.preprocessImage(tempFile);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSERACT_DATA_PATH);
        tesseract.setLanguage("kor+eng");

        String rawText = tesseract.doOCR(tempFile);
        System.out.println("OCR 결과:\n" + rawText);

        String bankName = extractBankName(rawText);
        String accountNumber = extractAccountNumber(rawText);
        String accountHolder = extractAccountHolder(rawText);

        return new AccountInfoResponseDto(bankName, accountNumber, accountHolder);
    }

    private String extractAccountNumber(String text) {
        Pattern patternWithHyphen = Pattern.compile("(\\d{2,6}-\\d{2,4}-\\d{4,6})");
        Matcher matcher = patternWithHyphen.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("\\s", "");
        }

        Pattern patternWithoutHyphen = Pattern.compile("(\\d{8,16})");
        Matcher matcherWithoutHyphen = patternWithoutHyphen.matcher(text);
        if (matcherWithoutHyphen.find()) {
            return matcherWithoutHyphen.group(1).replaceAll("\\s", "");
        }

        return null;
    }

    private String extractBankName(String text) {
        String normalizedText = text.replaceAll("\\s+", "");
        for (String bank : BANKS) {
            if (normalizedText.contains(bank)) return bank;
        }
        return null;
    }

    private String extractAccountHolder(String text) {
        Pattern pattern = Pattern.compile("([가-힣]{1,5}(?:\\s[가-힣]{1,5}){0,2})\\s*님");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("\\s+", "");
        }
        return null;
    }
}