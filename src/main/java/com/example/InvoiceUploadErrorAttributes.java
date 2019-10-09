package com.example;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Adds the "error_code" error attribute in case of InvoiceUploadException
 */
@Component
public class InvoiceUploadErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

        Throwable throwable = getError(webRequest);
        if (throwable instanceof InvoiceUploadException) {
            InvoiceUploadException e = (InvoiceUploadException) throwable;
            errorAttributes.put("error_code", e.getErrorCode());
        }

        return errorAttributes;
    }
}
