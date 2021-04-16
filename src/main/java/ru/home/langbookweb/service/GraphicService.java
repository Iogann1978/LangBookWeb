package ru.home.langbookweb.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
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
        String result = "";
        try(StringWriter sw = new StringWriter()) {
            Configuration cfg = new Configuration(new Version("2.3.0"));
            cfg.setClassLoaderForTemplateLoading(GraphicService.class.getClassLoader(),"/templates/");
            Template template = cfg.getTemplate("roundrobin.ftlh");
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
            template.process(templateData, sw);
            result = sw.toString();
            log.info("svg: {}", result);
        } catch (IOException | TemplateException e) {
            log.error("error svg template reading: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
