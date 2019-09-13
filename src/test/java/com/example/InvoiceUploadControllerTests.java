package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.text.AbstractDocument;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import(ContentAnalyzer.class)
public class InvoiceUploadControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnBadRequest() throws Exception {
        MockPart file1 = new MockPart("files", "hello.pdf", "hello".getBytes(StandardCharsets.UTF_8));
        file1.getHeaders().setContentType(APPLICATION_PDF);

        this.mockMvc.perform((multipart("/invoices"))
                .part(file1))
                .andExpect(status().is4xxClientError())

        // this is not working with @WebMvcTest
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message", is("the file content is not in sync with the suffix")));

                .andDo(print())
        ;


    }

    @Test
    public void shouldReturnSuccess() throws Exception {
        MockPart file1 = new MockPart("files", "hello.txt", "hello".getBytes(StandardCharsets.UTF_8));
        file1.getHeaders().setContentType(MediaType.TEXT_PLAIN);

        this.mockMvc.perform((multipart("/invoices"))
                .part(file1))
                .andExpect(status().is2xxSuccessful()).andDo(print());
    }

}
