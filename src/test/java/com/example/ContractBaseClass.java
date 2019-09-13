package com.example;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class ContractBaseClass {

    @Autowired
    private InvoiceUploadController invoiceUploadController;

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(invoiceUploadController);
    }
}