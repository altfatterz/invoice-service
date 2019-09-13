package com.example;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

/**
 * Unit test for {@link ContentAnalyzer}
 */
public class ContentAnalyzerTests {

    private ContentAnalyzer contentAnalyzer;

    @Before
    public void setUp() {
        contentAnalyzer = new ContentAnalyzer();
    }

    @Test(expected = InvoiceUploadException.class)
    public void analyzeError() {
        MockMultipartFile file = new MockMultipartFile("hello.pdf",
                "hello.pdf", "application/pdf", "hello".getBytes());
        contentAnalyzer.analyze(file);
    }

    @Test
    public void analyzeSuccess() {
        MockMultipartFile file = new MockMultipartFile("hello.txt",
                "hello.txt", "text/plain", "hello".getBytes());
        contentAnalyzer.analyze(file);
    }
}
