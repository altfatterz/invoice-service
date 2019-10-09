package com.example;

import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
// SpringBootTest.WebEnvironment.RANDOM_PORT is needed otherwise TestRestTemplate is not auto configured
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvoiceUploadControllerIntegrationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void uploadSuccessPdf() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("files", new ClassPathResource("invoice.pdf"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/invoices", entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void uploadFailed() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("files", new ClassPathResource("invoice.txt"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<ErrorResponse> response = testRestTemplate.postForEntity("/invoices", entity, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError_code()).isEqualTo("INVOICE_UPLOAD_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("the file upload stream could not be read");
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getStatus()).isEqualTo("400");
        assertThat(response.getBody().getPath()).isEqualTo("/invoices");
    }

    @Getter
    static class ErrorResponse {
        private String error_code;
        private String error;
        private String message;
        private String status;
        private String path;
    }
}
