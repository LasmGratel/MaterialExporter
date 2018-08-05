package cc.lasmgratel.materialexporter.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vein {
    public String name;

    /**
     * Chance to spawn
     */
    public double spawnChance;

    public int minY, maxY;

    /**
     * Default to generate in all dimensions
     */
    public List<String> dimensions = new ArrayList<>();

    public Map<String, Integer> materialChances = new HashMap<>();

    public double density;

    public int[] size = {};
}
