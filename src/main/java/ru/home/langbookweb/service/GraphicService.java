package ru.home.langbookweb.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class GraphicService {
    private static final Integer WIDTH = 800, HEIGHT = 400;

    public static String getSrc(String text) {
        String xmlSvg = getSvg(text);
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/svg+xml;charset=UTF-8,");
        sb.append(URLEncoder.encode(xmlSvg, StandardCharsets.UTF_8).replace("+","%20"));
        return sb.toString();
    }

    private static String getSvg(String word) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<svg width=%s height=%s xmlns=%s viewBox=%s preserveAspectRatio=%s>",
                StringUtils.quote(WIDTH.toString()),
                StringUtils.quote(HEIGHT.toString()),
                StringUtils.quote("http://www.w3.org/2000/svg"),
                StringUtils.quote(StringUtils.collectionToDelimitedString(List.of("0","0",WIDTH.toString(),HEIGHT.toString()), " ")),
                StringUtils.quote("none")));
        sb.append("<defs>");
        sb.append(String.format("<style type=%s>#holder_178217c20b4 text { fill:#555;font-weight:normal;font-family:Helvetica, monospace;font-size:40pt } </style>",
                StringUtils.quote("text/css")));
        sb.append("</defs>");
        sb.append("<g id=\"holder_178217c20b4\">");
        sb.append(String.format("<rect width=%s height=%s fill=%s>",
                StringUtils.quote(WIDTH.toString()),
                StringUtils.quote(HEIGHT.toString()),
                StringUtils.quote("#777")));
        sb.append("</rect>");
        sb.append("<g>");
        sb.append(String.format("<text x=%s y=%s>%s</text>",
                StringUtils.quote(String.valueOf(WIDTH / 2 - 10 * word.length())),
                StringUtils.quote(String.valueOf(HEIGHT / 2 - 10)),
                StringUtils.quote(word)));
        sb.append("</g>");
        sb.append("</g>");
        sb.append("</svg>");
        return sb.toString();
    }
}
