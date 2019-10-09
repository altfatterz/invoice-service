package com.example;

class InvoiceUploadException extends RuntimeException {

    private String errorCode;

    public InvoiceUploadException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

