package com.example;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

/**
 * Unit test for {@link InvoiceUploadContentAnalyzer}
 */
public class InvoiceUploadContentAnalyzerTests {

    private InvoiceUploadContentAnalyzer invoiceUploadContentAnalyzer;

    @Before
    public void setUp() {
        invoiceUploadContentAnalyzer = new InvoiceUploadContentAnalyzer();
    }

    @Test(expected = InvoiceUploadException.class)
    public void analyzeError() {
        MockMultipartFile file = new MockMultipartFile("hello.pdf", "", "application/pdf", "hello".getBytes());
        invoiceUploadContentAnalyzer.analyze(file);
    }

    @Test
    public void analyzeSuccess() throws IOException {
        MockMultipartFile file = new MockMultipartFile("hello.pdf", "", "application/pdf",
                new ClassPathResource("invoice.pdf").getInputStream());
        invoiceUploadContentAnalyzer.analyze(file);
    }
}
