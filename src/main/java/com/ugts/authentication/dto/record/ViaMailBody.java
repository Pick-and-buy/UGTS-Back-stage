package com.ugts.authentication.dto.record;

import lombok.Builder;

@Builder
public record ViaMailBody(String to, String subject, String text) {}
