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
class ContentAnalyzer {

    private static final Tika tika = new Tika();

    void analyze(MultipartFile multipartFile) {
        try {
            if (multipartFile == null || multipartFile.getInputStream() == null
                    || StringUtils.isEmpty(multipartFile.getContentType())) {
                throw new InvoiceUploadException("stream is null or content type is empty");
            }
            String declaredContentType = multipartFile.getContentType();
            String detectedContentType = tika.detect(multipartFile.getInputStream());
            if (!declaredContentType.equals(detectedContentType)) {
                log.warn("upload failed: declared content type: {}, detected content type: {}, filename: {}",
                        declaredContentType, detectedContentType, multipartFile.getOriginalFilename());
                throw new InvoiceUploadException("the file content is not in sync with the file extension");
            }

            tika.parse(multipartFile.getInputStream()).read();
        } catch (IOException e) {
            if (e.getCause() instanceof EncryptedDocumentException) {
                throw new InvoiceUploadException("document encrypted");
            }
            throw new InvoiceUploadException("the file upload stream could not be read");
        }
    }
}
