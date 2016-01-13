package coded.lens.amaral;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.*;

import java.lang.reflect.Field;
import java.util.Random;

public class FoodWeb {
    private final int layers;
    private final int connectedness;
    private List<Species> allSpecies;
    private Array<List<Species>> trophicLayers;
    private Set<Tuple2<Species, Species>> predators;
    private Set<Tuple2<Species, Species>> prey;
    private Map<Species, Integer> trophicLevels;

    private FoodWeb(final int layers, final int connectedness) {
        this.layers = layers;
        this.connectedness = connectedness;

        if (layers < 3) {
            throw new IllegalArgumentException("Trophic web must have at least three levels");
        }
        trophicLayers = Array.empty();
        for (int i = 0; i < layers; i++) {
            trophicLayers = trophicLayers.append(List.empty());
        }

        predators = HashSet.empty();
        prey = HashSet.empty();

        trophicLevels = HashMap.empty();
        allSpecies = List.empty();
    }

    private FoodWeb(List<Species> allSpecies,
                   Array<List<Species>> trophicLayers,
                   Set<Tuple2<Species, Species>> predators,
                   Set<Tuple2<Species, Species>> prey,
                   Map<Species, Integer> trophicLevels,
                   final int layers,
                   final int connectedness)
    {
        this.allSpecies = allSpecies;
        this.trophicLayers = trophicLayers;
        this.predators = predators;
        this.prey = prey;
        this.trophicLevels = trophicLevels;
        this.layers = layers;
        this.connectedness = connectedness;
    }

    public static FoodWeb primedFoodWeb(int layers, int connectedness, int... speciesAtEachLevel) {
        FoodWeb web = new FoodWeb(layers, connectedness);

        for (int level = 0; level < speciesAtEachLevel.length; level++) {
            for (int s = 0; s < speciesAtEachLevel[level]; s++) {
                web = web.newSpeciesAtTrophicLevel(level);
            }
        }

        return web;
    }

    private FoodWeb newSpeciesAtTrophicLevel(int trophicLevel) {
        Species species = new Species();

        Map<Species, Integer> trophicLevels = this.trophicLevels.put(species, trophicLevel);
        List<Species> allSpecies = this.allSpecies.append(species);
        List<Species> trophicLayer = this.trophicLayers.get(trophicLevel).append(species);
        Array<List<Species>> trophicLayers = this.trophicLayers.update(trophicLevel, trophicLayer);
        Set<Tuple2<Species, Species>> predators = this.predators;
        Set<Tuple2<Species, Species>> prey = this.prey;

        if (trophicLevel > 0) {
            List<Species> potentialPrey = this.trophicLayers.get(trophicLevel - 1);
            for (int i = 0; i < connectedness; i++) {
                Species preySpecies = potentialPrey.get((int) (Math.random() * potentialPrey.length()));
                prey = prey.add(Tuple.of(species, preySpecies));
                predators = predators.add(Tuple.of(preySpecies, species));
            }
        }

        if (trophicLevel < layers - 1) {
            List<Species> potentialPredators = this.trophicLayers.get(trophicLevel + 1);
            if (potentialPredators.length() > 0) {
                for (int i = 0; i < connectedness; i++) {
                    Species predatorSpecies = potentialPredators.get((int) (Math.random() * potentialPredators.length()));
                    prey = prey.add(Tuple.of(predatorSpecies, species));
                    predators = predators.add(Tuple.of(species, predatorSpecies));
                }
            }
        }

        return new FoodWeb(allSpecies, trophicLayers, predators, prey, trophicLevels, layers, connectedness);
    }

    public FoodWeb speciationEvent() {
        int speciesIndex = (int)(Math.random() * allSpecies.length());
        int trophicLevel = trophicLevels.get(allSpecies.get(speciesIndex)).orElseThrow(() -> new IllegalStateException());

        int randomTrophicLevel;

        if (trophicLevel == 0) {
            randomTrophicLevel = (int)(Math.random() * 2);
        } else if (trophicLevel == layers - 1) {
            randomTrophicLevel = (int)(Math.random() * 3) - 2 + trophicLevel;
        } else {
            randomTrophicLevel = (int)(Math.random() * 3) - 1 + trophicLevel;
        }

        return newSpeciesAtTrophicLevel(randomTrophicLevel);
    }


    @Override
    public String toString() {
        return "FoodWeb {"  + "\n" +
                "  trophicLayers = " + trophicLayers  + "\n" +
                "  predators = " + predators  + "\n" +
                "  prey = " + prey  + "\n" +
                '}';
    }

    public String prettyString() {
        StringBuilder web = new StringBuilder();

        for (int i = layers - 1; i >= 0; i--) {
            List<Species> layer = trophicLayers.get(i);

            web.append("layer " + i + ": ");

            layer.forEach(s -> web.append(s + ", "));
            web.append("\n");
        }

        return web.toString();
    }
}
