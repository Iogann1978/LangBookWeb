package ru.home.langbookweb;

import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.Word;
import ru.home.langbookweb.model.xml.Card;
import ru.home.langbookweb.model.xml.Dictionary;
import ru.home.langbookweb.model.xml.Example;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractLoadService<T extends Word> {
    protected void loadDicts(User user, Resource resource, boolean isPhrases) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Dictionary.class);
        int depth = isPhrases ? 2 : 1;
        Files.find(Paths.get(resource.getURI()), depth,
                (path, basicFileAttributes) -> path.getFileName().toString().endsWith("ER.xml")).forEach(f -> {
            try(Reader reader = new InputStreamReader(new FileInputStream(f.toFile()), StandardCharsets.UTF_8) ) {
                Dictionary dict = (Dictionary) context.createUnmarshaller().unmarshal(reader);
                String source = dict.getTitle().trim();
                for (Card card : dict.getCard()) {
                    String tooltip = card.getMeanings().getMeaning().getTranslations().getWord().getValue().trim();
                    String strWord = processWord(card.getWord().getValue());
                    if (strWord.contains(" ") == isPhrases)
                        saveEntry(user, source, strWord, tooltip, card);
                }
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static String processWord(String word) {
        String result = word.toLowerCase().trim();
        if (result.startsWith("\"") && result.endsWith("\""))
            result = result.substring(1, result.length() - 1);
        if (result.startsWith(".") || result.startsWith(",") || result.startsWith(":") || result.startsWith(";"))
            result = result.substring(1);
        if (result.endsWith(".") || result.endsWith(",") || result.endsWith(":") || result.endsWith(";"))
            result = result.substring(0, result.length() - 1);
        if (result.startsWith("a "))
            result = result.substring(2);
        if (result.startsWith("an "))
            result = result.substring(2);
        if (result.startsWith("to "))
            result = result.substring(3);
        if (result.startsWith("the "))
            result = result.substring(4);
        if (result.endsWith(" 3f"))
            result = result.substring(0, result.length() - 3);
        if (word.equals(result))
            return result;
        else
            result = processWord(result);
        return result;
    }

    protected void fillEntry(T entry, String tooltip, String source, Card card) {
        if (CollectionUtils.isEmpty(entry.getTranslations())) {
            entry.setTranslations(new HashSet<>());
        }
        Set<Translation> translations = entry.getTranslations();
        translations.add(Translation.builder().description(tooltip).source(source).word(entry).build());
        for (Example example : card.getMeanings().getMeaning().getExamples().getContent()) {
            String[] lines = example.getValue().replace("_Id:", "_Ex:").split("_Ex:");
            if (lines != null && lines.length > 0) {
                Translation trnForExmp = null;
                for (String tr : lines[0].split("[,;]")) {
                    Translation trn = Translation.builder().description(tr.trim()).source(source).word(entry).build();
                    if (trnForExmp == null)
                        trnForExmp = trn;
                    translations.add(trn);
                }
                if (lines.length > 1) {
                    for (int i = 1; i < lines.length; i++) {
                        Pattern pattern = Pattern.compile("([a-zA-Z][^а-я^А-Я]+) ([а-яА-Я].+)");
                        Matcher matcher = pattern.matcher(lines[i]);
                        if (matcher.matches() && trnForExmp != null) {
                            if (trnForExmp.getExamples() == null)
                                trnForExmp.setExamples(new HashSet<>());
                            if (matcher.groupCount() == 1)
                                trnForExmp.getExamples().add(ru.home.langbookweb.model.Example.builder().text1(matcher.group(1).trim()).translation(trnForExmp).build());
                            if (matcher.groupCount() == 2)
                                trnForExmp.getExamples().add(ru.home.langbookweb.model.Example.builder().text1(matcher.group(1).trim()).text2(matcher.group(2).trim()).translation(trnForExmp).build());
                        }
                    }
                }
            }
        }
    }

    protected abstract void saveEntry(User user, String source, String strWord, String tooltip, Card card);
}
