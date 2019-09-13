package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
class InvoiceUploadController {

    private final ContentAnalyzer contentAnalyzer;
    private final static List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/jpg", "application/pdf");

    public InvoiceUploadController(ContentAnalyzer contentAnalyzer) {
        this.contentAnalyzer = contentAnalyzer;
    }

    @PostMapping("/invoices")
    public void upload(List<MultipartFile> files) {
        checkArg(files);

        // validate the upload
        files.forEach(multipartFile -> contentAnalyzer.analyze(multipartFile));
        log.info("upload success with {} nr. of files", files.size());

        // business logic
    }

    void checkArg(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            log.info("MultipartFile list is empty");
            throw new InvoiceUploadException("could not find any files in the upload request");
        }
        files.forEach(multipartFile -> {
            if (!ALLOWED_MIME_TYPES.contains(multipartFile.getContentType())) {
                throw new InvoiceUploadException("only " + ALLOWED_MIME_TYPES.toString() + " are allowed");
            }
        });
    }

    @ExceptionHandler(InvoiceUploadException.class)
    void handle(HttpServletResponse response, InvoiceUploadException e) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

}