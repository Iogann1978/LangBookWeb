package ru.home.langbookweb.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GraphicService {
    private static final Integer WIDTH = 800, HEIGHT = 400;
    private Configuration freemarkerConfig;

    @Autowired
    public GraphicService(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    public String getSrc(String text1, String text2) {
        String xmlSvg = getSvg(text1, text2);
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/svg+xml;base64,");
        sb.append(Base64.getEncoder().encodeToString(xmlSvg.getBytes(StandardCharsets.UTF_8)));
        return sb.toString();
    }

    private String getSvg(String text1, String text2) {
        String result = "";
        try {
            Map<String, Object> templateData = new HashMap<>(){
                {
                    put("width", WIDTH);
                    put("height", HEIGHT);
                    put("x1", WIDTH / 2);
                    put("y1", HEIGHT / 2);
                    put("x2", WIDTH / 2);
                    put("y2", HEIGHT / 2 + 40);
                    put("word", text1);
                    put("type", text2);
                }
            };
            result = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate("roundrobin.ftlh"), templateData);
        } catch (IOException | TemplateException e) {
            log.error("error svg template reading: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
