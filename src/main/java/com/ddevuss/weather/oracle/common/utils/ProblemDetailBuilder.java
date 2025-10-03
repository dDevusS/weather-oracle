package com.ddevuss.weather.oracle.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class ProblemDetailBuilder {

    private HttpStatus status;
    private String title;
    private String detail;
    private URI uri;

    public static ProblemDetailBuilder forStatus(HttpStatus status) {
        ProblemDetailBuilder problemDetailBuilder = new ProblemDetailBuilder();
        problemDetailBuilder.status(status);
        return problemDetailBuilder;
    }

    public ProblemDetailBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ProblemDetailBuilder detail(String detail) {
        this.detail = detail;
        return this;
    }

    public ProblemDetailBuilder uri(HttpServletRequest req) {
        this.uri = URI.create(req.getRequestURI());
        return this;
    }

    public ProblemDetail build() {
        return problem(status, title, detail, uri);
    }

    private void status(HttpStatus status) {
        this.status = status;
    }

    private static ProblemDetail problem(HttpStatus status, String title, String detail, URI uri) {
        var pd = ProblemDetail.forStatus(status);
        pd.setType(URI.create("https://ddevuss.github.io/weather-oracle/"));
        if (title != null) pd.setTitle(title);
        if (detail != null) pd.setDetail(detail);
        if (uri != null) pd.setInstance(uri);
        return pd;
    }
}
