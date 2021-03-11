package ru.home.langbookweb.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class GraphicService {
    private static final Integer WIDTH = 800, HEIGHT = 400;

    public static String getSrc(String text1, String text2) {
        String xmlSvg = getSvg(text1, text2);
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/svg+xml;base64,");
        sb.append(Base64.getEncoder().encodeToString(xmlSvg.getBytes(StandardCharsets.UTF_8)));
        return sb.toString();
    }

    private static String getSvg(String text1, String text2) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<svg width=%s height=%s xmlns=%s viewBox=%s preserveAspectRatio=%s>",
                StringUtils.quote(WIDTH.toString()),
                StringUtils.quote(HEIGHT.toString()),
                StringUtils.quote("http://www.w3.org/2000/svg"),
                StringUtils.quote(StringUtils.collectionToDelimitedString(List.of("0","0",WIDTH.toString(),HEIGHT.toString()), " ")),
                StringUtils.quote("none")));
        sb.append("<g>");
        sb.append(String.format("<rect width=%s height=%s fill=%s>",
                StringUtils.quote(WIDTH.toString()),
                StringUtils.quote(HEIGHT.toString()),
                StringUtils.quote("#777")));
        sb.append("</rect>");
        sb.append("<g>");
        sb.append(String.format("<text x=%s y=%s text-anchor=%s style=%s>",
                StringUtils.quote(String.valueOf(WIDTH / 2)),
                StringUtils.quote(String.valueOf(HEIGHT / 2)),
                StringUtils.quote("middle"),
                StringUtils.quote("fill:#555;font-weight:normal;font-family:Helvetica, monospace;font-size:40pt")));
        sb.append(text1);
        sb.append("</text>");
        sb.append(String.format("<text x=%s y=%s text-anchor=%s style=%s>",
                StringUtils.quote(String.valueOf(WIDTH / 2)),
                StringUtils.quote(String.valueOf(HEIGHT / 2 + 40)),
                StringUtils.quote("middle"),
                StringUtils.quote("fill:#555;font-weight:normal;font-family:Helvetica, monospace;font-size:30pt")));
        sb.append(text2);
        sb.append("</text>");
        sb.append("</g>");
        sb.append("</g>");
        sb.append("</svg>");
        return sb.toString();
    }
}
