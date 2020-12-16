package com.newsletter.axon.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;

public final class HeaderUtil {

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(final String message, final String param) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-axon-alert", message);

        try {
            headers.add("X-axon-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException ignored) {
        }
        return headers;
    }
}
