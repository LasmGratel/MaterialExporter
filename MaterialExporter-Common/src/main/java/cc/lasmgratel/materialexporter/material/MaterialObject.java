package cc.lasmgratel.materialexporter.material;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class MaterialObject {
    public String chemicalFormula;
    public String name;

    /**
     * Color in RGBA
     */
    public int color;

    public double value;

    public List<JsonElement> extra = new ArrayList<>();
}
