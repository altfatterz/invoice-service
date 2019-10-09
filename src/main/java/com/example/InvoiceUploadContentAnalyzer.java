package com.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.EncryptedDocumentException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
class InvoiceUploadContentAnalyzer {

    private static final Tika tika = new Tika();
    private static final String INVOICE_UPLOAD_ERROR = "INVOICE_UPLOAD_ERROR";

    void analyze(MultipartFile multipartFile) {
        try {
            if (multipartFile == null || multipartFile.getInputStream() == null || StringUtils.isEmpty(multipartFile.getContentType())) {
                log.warn("MultipartFile is null or empty");
                throw new InvoiceUploadException(INVOICE_UPLOAD_ERROR, "MultipartFile is null or empty");
            }
            String declaredContentType = multipartFile.getContentType();
            String detectedContentType = tika.detect(multipartFile.getInputStream());
            if (!declaredContentType.equals(detectedContentType)) {
                log.warn("declared content type: {}, detected content type: {}, filename: {}",
                        declaredContentType, detectedContentType, multipartFile.getOriginalFilename());
                throw new InvoiceUploadException(INVOICE_UPLOAD_ERROR, "the file content does not match with the file extension");
            }

            tika.parse(multipartFile.getInputStream()).read();
        } catch (IOException e) {
            if (e.getCause() instanceof EncryptedDocumentException) {
                log.warn("detected a password protected document: {}", multipartFile.getOriginalFilename());
                throw new InvoiceUploadException(INVOICE_UPLOAD_ERROR, "detected a password protected document");
            }
            log.warn("the file upload stream could not be read");
            throw new InvoiceUploadException(INVOICE_UPLOAD_ERROR, "the file upload stream could not be read");
        }
    }
}
