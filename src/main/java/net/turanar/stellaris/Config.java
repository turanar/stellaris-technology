package net.turanar.stellaris;

import net.turanar.stellaris.domain.Technology;
import net.turanar.stellaris.util.StellarisYamlReader;
import net.turanar.stellaris.antlr.StellarisParser;
import net.turanar.stellaris.antlr.StellarisParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.lang.RuntimeException;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import static net.turanar.stellaris.Global.*;

@Configuration
public class Config {
    @Autowired
    StellarisParserFactory factory;

    @Bean("GLOBAL_VARIABLES")
    public Map<String,String> variables() throws IOException {
        HashMap<String,String> retval = new HashMap<>();

        parse("files/common/scripted_variables", "txt", p -> {
            try {
                factory.getParser(p).file().var().forEach(v -> retval.put(v.VARIABLE().getText(), v.NUMBER().getText()));
            }
            catch (RuntimeException e) {
                throw new RuntimeException("Error parsing file " + p.toString(), e);
            }
        });

        parse("files/common/technology", "txt", p -> {
            try {
                factory.getParser(p).file().var().forEach(v -> retval.put(v.VARIABLE().getText(), v.NUMBER().getText()));
            }
            catch (RuntimeException e) {
                throw new RuntimeException("Error parsing file " + p.toString(), e);
            }
        });

        return retval;
    }

    @Bean("GLOBAL_STRINGS")
    public Map<String,String> localisation() throws IOException {
        Map<String,String> retval = new HashMap<>();

        parse("files/localisation/english", "yml", path -> {
            try {
                Yaml yaml = new Yaml();
                Iterable<Object> data = yaml.loadAll(new StellarisYamlReader(path));
                Map<String,Map<Object,Object>> map = (Map<String,Map<Object,Object>>)data.iterator().next();
                Map english_l10n = map.getOrDefault("l_english", Collections.emptyMap());
                if (english_l10n != null) {
                    english_l10n.forEach((k, v) -> {
                        retval.put(k.toString().toLowerCase(), v.toString());
                    });
                };
            }
            catch (RuntimeException e) {
                throw new RuntimeException("Error parsing file " + path.toString(), e);
            }
        });

        return retval;
    }

    @Bean("SCRIPTED_TRIGGERS")
    public Map<String, StellarisParser.PairContext> scriptedTriggers() throws IOException {
        Map<String, StellarisParser.PairContext> retval = new HashMap<>();

        parse("files/common/scripted_triggers", "txt", path -> {
            try {
                factory.getParser(path).file().pair().forEach(pair -> retval.put(key(pair), pair));
            }
            catch (RuntimeException e) {
                throw new RuntimeException("Error parsing file " + path.toString(), e);
            }
        });

        return retval;
    }

    @Bean("technologies")
    public Map<String, Technology> technologies() {
        return new HashMap<>();
    }

}
