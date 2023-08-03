package net.turanar.stellaris.domain;

import net.turanar.stellaris.antlr.StellarisParser.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import static net.turanar.stellaris.Global.*;

public enum ModifierType {
    has_ascension_perk("Has %s Ascension Perk"),
    has_authority("Has %s Authority"),
    has_blocker("Has £blocker£ Tile Blocker: %s"),
    has_technology("Has Technology: %s"),
    has_valid_civic("Has Government Civic: %s"),
    has_civic("Has Government Civic: %s"),
    has_modifier("Has the %s modifier"),
    has_ethic("Has %s Ethic"),
    host_has_dlc("Has DLC %s"),
    has_tradition("Has %s Tradition"),
    has_country_flag("Has the %s country flag"),
    has_global_flag("Has the %s global flag"),
    has_deposit("Has deposit %s"),
    is_country_type("Is of country type: %s"),
    is_planet_class("Is %s"),
    has_communications("Has communication with our Empire"),
    pop_has_trait("Pop has trait %s"),
    has_policy_flag((p) -> f("Has policy %s", i18n(gs(p) + "_name"))),
    owns_any_bypass((p) -> f("Controls a system with a %s", i18n("bypass_" + gs(p).toLowerCase()))),
    has_seen_any_bypass((p) -> f("Has encountered a %s", i18n("bypass_" + gs(p).toLowerCase()))),

    // FIXME: proper parsing of the following
    count_archaeological_site((p)->{
        String type = "";
        String count = "";
        for(PairContext prop : p.value().map().pair()) {
            if(key(prop).equals("type")) {
                type = i18n(gs(prop));
            } else if (key(prop).equals("value")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "Number of " + type + " is " + count;
    }),
    exists("Exists"),
    federation("Is Federation"),
    has_federation("Has %s federation"),
    has_first_contact_dlc("Has DLC First Contact"),
    has_nemesis("Has Nemesis"),
    has_origin("Has %s federation"),
    has_paragon_dlc("Has DLC galactic paragons"),
    has_trait_in_council("Has trait %s in council"),
    is_eager_explorer_empire("Is eager explorer empire"),
    is_same_value("IS SAME VALUE"),
    is_specialist_subject_type("Is specialist subject"),
    mid_game_years_passed("In Mid-game"),
    num_buildings("Has %s buildings"),
    count_owned_pop("Owns %s pops"),
    harvested_leviathan_dna("Has harvested leviathan DNA"),
    has_lithoids("Has Lithoids"),
    has_overlord_dlc("Has DLC Overlord"),
    has_plantoids("Has Plantoids"),
    has_relic("Has Relic"),
    has_space_monster_dlc("Has DLC Space Monsters"),
    is_catalytic_empire("Is Catalytic Empire"),
    is_galactic_community_member("Is member of the galactic community"),
    is_homicidal("Is homicidal"),
    is_lithoid_empire("Is lithoid empire"),
    // until here


    is_xenophile(DefaultParser.SCRIPTED),
    is_pacifist(DefaultParser.SCRIPTED),
    is_materialist(DefaultParser.SCRIPTED),
    is_egalitarian(DefaultParser.SCRIPTED),
    is_authoritarian(DefaultParser.SCRIPTED),
    is_militarist(DefaultParser.SCRIPTED),
    is_xenophobe(DefaultParser.SCRIPTED),

    is_spiritualist(DefaultParser.SCRIPTED),
    is_gestalt(DefaultParser.SCRIPTED),
    is_mechanical_empire(DefaultParser.SCRIPTED),
    is_regular_empire(DefaultParser.SCRIPTED),
    is_machine_empire(DefaultParser.SCRIPTED),
    is_hive_empire(DefaultParser.SCRIPTED),
    is_megacorp(DefaultParser.SCRIPTED),
    allows_slavery(DefaultParser.SCRIPTED),
    has_ancrel(DefaultParser.SCRIPTED),

    is_ai("Is AI|Is NOT AI", DefaultParser.SIMPLE_BOOLEAN),

    is_enslaved("Pop is enslaved|Pop is NOT enslaved", DefaultParser.SIMPLE_BOOLEAN),
    is_sapient("Pop is Sapient|Pop is NOT Sapient", DefaultParser.SIMPLE_BOOLEAN),
    has_any_megastructure_in_empire("Has any Megastructure|Does NOT have any Megastructure",DefaultParser.SIMPLE_BOOLEAN),
    always("Always|Never", DefaultParser.SIMPLE_BOOLEAN),

    years_passed("Number of years since game start is %s %s", DefaultParser.SIMPLE_OPERATION),
    num_owned_planets("Number of owned planets is %s %s", DefaultParser.SIMPLE_OPERATION),
    num_communications("Number of communications is %s %s", DefaultParser.SIMPLE_OPERATION),
    has_level("Skill level is %s %s", DefaultParser.SIMPLE_OPERATION),

    any_neighbor_country("Any Neighbor Country", DefaultParser.CONDITIONAL),
    any_owned_planet("Any Owned Planet", DefaultParser.CONDITIONAL),
    any_planet_within_border("Any Planet within borders", DefaultParser.CONDITIONAL),
    any_planet("Any Planet", DefaultParser.CONDITIONAL),
    any_owned_pop("Any Owned Pop", DefaultParser.CONDITIONAL),
    any_system_within_border("Any System within borders", DefaultParser.CONDITIONAL),
    any_system_planet("Any Planet", DefaultParser.CONDITIONAL),
    any_relation("Any Country Relation",DefaultParser.CONDITIONAL),
    any_pop("Any Pop", DefaultParser.CONDITIONAL),
    owner_species("Founder Species :", DefaultParser.CONDITIONAL),
    no_scope("", DefaultParser.CONDITIONAL),

    NOR("All must be false", DefaultParser.CONDITIONAL),
    OR("One must be true", DefaultParser.CONDITIONAL),
    NAND("One or more must be false", DefaultParser.CONDITIONAL),
    AND("All must be true", DefaultParser.CONDITIONAL),

    has_trait((p) -> {
        String expertise = i18n(gs(p));
        if(expertise.contains("Expertise: ")) expertise = expertise.replaceAll("Expertise: ","") + " Expert";
        return "Is " + expertise;
    }),
    area((p) -> StringUtils.capitalize(gs(p))),
    research_leader((p) -> {
        String area = "";
        List<String> conditions = new ArrayList<>();
        for(PairContext prop : p.value().map().pair()) {
            Modifier m = visitCondition(prop);
            if(m.type.equals(ModifierType.area)) area = m.toString();
            else conditions.add(m.toString());
        }
        String retval = "Research Leader (" + area + "): ";
        for(int i = 0; i < conditions.size(); i++) {
            retval = retval + "\n" + LS + conditions.get(i);
        }
        return retval;
    }),

    has_resource((p) -> {
        String type = "";
        String count = "";
        for(PairContext prop : p.value().map().pair()) {
            if(key(prop).equals("type")) {
                type = gs(prop);
            } else if (key(prop).equals("amount")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "Has £" + type + "£ " + i18n(type) + " " +  count;
    }),

    count_starbase_sizes((p) -> {
        String retval = "Number of %s is %s %s";
        String size = null, operator = null, count = null;
        for(PairContext prop : p.value().map().pair()) {
            if(key(prop).equals("starbase_size")) {
                size = i18n(gs(prop));
            } else if (key(prop).equals("count")) {
                operator = op(prop);
                count = gs(prop);
            }
        }
        return String.format(retval, size, operator, count);
    }),

    num_districts((p)->{
        String type = "";
        String count = "";
        for(PairContext prop : p.value().map().pair()) {
            if(key(prop).equals("type")) {
                type = i18n(gs(prop));
            } else if (key(prop).equals("value")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "Number of " + type + " is " + count;
    }),

    count_owned_pops((p) -> {
        String limits = "";
        String count = "";
        for(PairContext prop : p.value().map().pair()) {
            if(key(prop).equals("limit")) {
                for(PairContext l : prop.value().map().pair()) {
                    Modifier m = visitCondition(l);
                    limits += "\n" + LS + m.toString();
                }
            } else if(key(prop).equals("count")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "Has a number of pop " + count + limits;
    }),

    NOT((p) -> {
        if(p.value().map().pair().size() > 1) return NOR.parser.apply(p);
        Modifier m = visitCondition(p.value().map().pair().get(0));
        if(m.type.equals(OR)) return NOR.parser.apply(p.value().map().pair().get(0));

        String retval = m.toString();
        if(retval.startsWith("Has")) {
            return "Does NOT " + retval.replaceFirst("Has", "have");
        }
        else if(retval.startsWith("Is")) {
            return "Is NOT " + retval.replaceFirst("Is","");
        }  else if(retval.startsWith("Any")) {
            return retval.replaceFirst("Any","No");
        } else {
            return "NOT " + retval;
        }
    }),

    DEFAULT((p) -> {
        String retval = p.getText();
        System.out.println(retval);
        return retval;
    })
    ;

    private static enum DefaultParser {
        SIMPLE((format,p) -> String.format(format,i18n(gs(p.value())))),
        SIMPLE_OPERATION((format,p) -> String.format(format, op(p), gs(p))),
        SIMPLE_BOOLEAN((format,p) -> {
            String[] sentence = format.split("\\|");
            if(gs(p).equals("yes")) return sentence[0]; else return sentence[1];
        }),
        CONDITIONAL((format, p) -> {
            List<String> conditions = new ArrayList<>();

            for(PairContext prop : p.value().map().pair()) {
                Modifier m = visitCondition(prop);
                conditions.add(m.toString());
            }

            String retval = format;
            for(int i = 0; i < conditions.size(); i++) {
                retval = retval + "\n" + LS + conditions.get(i).replaceAll(LS, "\t" + LS);
            }
            return retval;
        }),
        SCRIPTED((format, p) -> {
            PairContext q = GLOBAL_TRIGGERS.get(key(p));
            boolean value = gs(p).equals("yes");
            List<String> conditions = new ArrayList<>();

            if(!value) {
                return ModifierType.NOT.parse(q);
            }

            for(PairContext prop : q.value().map().pair()) {
                Modifier m = visitCondition(prop);
                conditions.add(m.toString());
            }
            String retval = format;

            if(conditions.size() < 2) {
                retval = conditions.get(0);
                return retval;
            }

            for(int i = 0; i < conditions.size(); i++) {
                retval = retval + "\n" + LS + conditions.get(i).replaceAll(LS, "\t" + LS);
            }
            return retval;
        });

        private BiFunction<String, PairContext, String> parser;

        private DefaultParser(BiFunction<String, PairContext,String> parser) {
            this.parser = parser;
        }

        public String apply(String format, PairContext pair) {
            return this.parser.apply(format, pair);
        }
    }

    private Function<PairContext,String> parser;

    ModifierType(String format, DefaultParser parser) {
        this.parser = (p) -> parser.apply(format, p);
    }

    ModifierType(DefaultParser parser) {
        this.parser = (p) -> parser.apply(null, p);
    }

    ModifierType(Function<PairContext,String> parser) {
        this.parser = parser;
    }

    ModifierType(String format) {
        this(format, DefaultParser.SIMPLE);
    }

    public String parse(PairContext pair) {
        return parser.apply(pair);
    }

    public static ModifierType value(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return DEFAULT;
        }
    }

    public static Modifier visitCondition(PairContext pair) {
        Modifier retval = new Modifier();
        retval.type = ModifierType.value(key(pair));
        retval.pair = pair;
        return retval;
    }
}
