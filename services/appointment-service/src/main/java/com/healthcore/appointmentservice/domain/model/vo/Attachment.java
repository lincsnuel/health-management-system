package com.healthcore.appointmentservice.domain.model.vo;

import java.util.Objects;

public class Attachment {

    private final String fileUrl;
    private final String fileType;

    public Attachment(String fileUrl, String fileType) {
        this.fileUrl = Objects.requireNonNull(fileUrl);
        this.fileType = fileType;
    }
}