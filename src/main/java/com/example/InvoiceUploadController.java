package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
class InvoiceUploadController {

    private final InvoiceUploadContentAnalyzer invoiceUploadContentAnalyzer;

    public InvoiceUploadController(InvoiceUploadContentAnalyzer invoiceUploadContentAnalyzer) {
        this.invoiceUploadContentAnalyzer = invoiceUploadContentAnalyzer;
    }

    @PostMapping("/invoices")
    public void upload(List<MultipartFile> files) {
        // validate the upload
        files.forEach(multipartFile -> invoiceUploadContentAnalyzer.analyze(multipartFile));
        log.info("upload success with {} nr. of files", files.size());

        // business logic
    }

    @ExceptionHandler(InvoiceUploadException.class)
    void handle(HttpServletResponse response, InvoiceUploadException e) throws IOException {
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

}