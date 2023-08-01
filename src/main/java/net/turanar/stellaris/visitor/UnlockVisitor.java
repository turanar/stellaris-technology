package net.turanar.stellaris.visitor;

import net.turanar.stellaris.domain.GameObject;
import net.turanar.stellaris.domain.Technology;
import net.turanar.stellaris.antlr.StellarisParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static net.turanar.stellaris.Global.*;

import java.util.Map;

@Component
public class UnlockVisitor {
    @Autowired
    Map<String,Technology> technologies;

    public void visitFile(GameObject type, StellarisParser.FileContext ctx) {
        for(StellarisParser.PairContext pair : ctx.pair()) {
            this.visitPair(type, pair);
        }
    }

    public String clean(String prop) {
        String retval = prop.replaceAll("\\([^\\)]*\\)","");
        return retval.trim();
    }

    public Technology visitPair(GameObject type, StellarisParser.PairContext pair) {
        String tkey = key(pair);
        if(type == GameObject.STARBASE) {
            //System.out.println(tkey);
        }
        Technology tech = null;
        if(pair.value().map() == null) return null;

        for(StellarisParser.PairContext props : pair.value().map().pair()) {
            if(key(props).equals("prerequisites")) {
                if(props.value().array() == null) continue;
                for(StellarisParser.ValueContext ctx : props.value().array().value()) {
                    tech = technologies.get(gs(ctx));
                }
            } else if (key(props).equals("key") || key(props).equals("name")) {
                tkey = gs(props.value());
            } else if (key(props).equals("show_in_tech")) {
                tech = technologies.get(gs(props.value()));
            } else if (key(props).equals("option") && type == GameObject.POLICY) {
                tkey = null;
                tech = visitPair(type, props);
            }
        }

        if(tkey == null) return tech;

        if(i18n(tkey).equals(tkey)) {
            tkey = i18n(type.locale_prefix + tkey);
        } else {
            tkey = i18n(tkey);
        }

        if(tech != null) tech.feature_unlocks.add(clean("<b>" + type.label + "</b>: " + tkey));

        return tech;
    }
}
