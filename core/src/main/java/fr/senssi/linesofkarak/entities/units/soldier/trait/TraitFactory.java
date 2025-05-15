package fr.senssi.linesofkarak.entities.units.soldier.trait;

import fr.senssi.linesofkarak.entities.units.soldier.Soldier;

import java.util.*;

public class TraitFactory {
    private static final Map<String, Trait> registeredTraits = new HashMap<>();

    static {
        register(new Trait() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void applyEffects( Soldier soldier) {

            }
        });

        register(new Trait() {
            @Override
            public String getName() {
                return "Nerfs d'acier";
            }

            @Override
            public String getDescription() {
                return "Ne subit pas la pression du combat";
            }

            @Override
            public void applyEffects(Soldier soldier) {
                soldier.setMoraleLossMultiplier(0.5f); // Suppose que tu as ce champ ou méthode
            }
        });

        register(new Trait() {
            @Override
            public String getName() {
                return "Leader né";
            }

            @Override
            public String getDescription() {
                return "Prend la place du commandant s'il meurt";
            }

            @Override
            public void applyEffects(Soldier soldier) {
                soldier.canReplaceCommander = true;
            }
        });

        register(new GenericTrait(
            "Survivant",
            "A survécu à de grandes épreuves qui l'ont rendu plus fort",
            10, 0, 0 // +10 HP
        ));

        register(new Trait() {
            @Override
            public String getName() {
                return "Guérillero";
            }

            @Override
            public String getDescription() {
                return "Adepte des combats en terrain difficile";
            }

            @Override
            public void applyEffects(Soldier soldier) {
                soldier.setTerrainBonus("forest", 1.2f); // Exemple pour forêt
                soldier.setTerrainBonus("mountain", 1.2f);
            }
        });

        register(new Trait() {
            @Override
            public String getName() {
                return "Fourbe";
            }

            @Override
            public String getDescription() {
                return "On ne sait jamais ce qu'il va faire";
            }

            @Override
            public void applyEffects( Soldier soldier) {
                soldier.setFlankingDamageBonus(1.5f); // Suppose une méthode dédiée
            }
        });

        // Trait neutre (pas d'effet)
        register(new Trait() {
            @Override
            public String getName() {
                return "Aucun";
            }

            @Override
            public String getDescription() {
                return "Aucun trait particulier.";
            }

            @Override
            public void applyEffects(Soldier soldier) {
                // Rien
            }
        });
    }

    private static void register(Trait trait) {
        registeredTraits.put(trait.getName(), trait);
    }

    public static Trait getRandom(){
        List<Trait> values = new ArrayList<>(registeredTraits.values());

        // Choisir un index au hasard
        Random random = new Random();
        return values.get(random.nextInt(values.size()));
    }

    public static Trait get( String name) {
        return registeredTraits.getOrDefault(name, registeredTraits.get("Aucun"));
    }

    public static Map<String, Trait> getAll() {
        return registeredTraits;
    }
}
