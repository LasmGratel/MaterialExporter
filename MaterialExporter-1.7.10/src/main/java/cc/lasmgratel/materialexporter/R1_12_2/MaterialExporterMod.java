package cc.lasmgratel.materialexporter.R1_12_2;

import cc.lasmgratel.materialexporter.material.MaterialObject;
import cc.lasmgratel.materialexporter.material.Vein;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.Materials;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;

import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(modid = "material-exporter")
public class MaterialExporterMod {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Path configPath;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configPath = event.getModConfigurationDirectory().toPath().resolve("MaterialExporter");
        if (Files.notExists(configPath)) {
            try {
                Files.createDirectories(configPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        if (Loader.isModLoaded("gregtech_addon"))
            processGregTech();
    }

    private void processGregTech() {
        for (Materials material : Materials.getAll()) {
            MaterialObject object = new MaterialObject();
            object.name = material.mName;
            object.chemicalFormula = material.mChemicalFormula;
            object.color = new Color(material.mRGBa[0], material.mRGBa[1], material.mRGBa[2], material.mRGBa[3]).getRGB();
        }
        for (GT_Worldgen_GT_Ore_Layer layerGen : GT_Worldgen_GT_Ore_Layer.sList) {
            Vein vein = new Vein();
            vein.density = layerGen.mDensity / 10d;
            vein.name = layerGen.mWorldGenName;
            vein.maxY = layerGen.mMaxY;
            vein.minY = layerGen.mMinY;
            vein.spawnChance = layerGen.mWeight / 1850d;
            if (layerGen.mOverworld)
                vein.dimensions.add("Overworld");
            else if (layerGen.mAsteroid)
                vein.dimensions.add("Asteroid");
            else if (layerGen.mNether)
                vein.dimensions.add("Nether");
            else if (layerGen.mEnd)
                vein.dimensions.add("The End");
            else if (layerGen.mEndAsteroid)
                vein.dimensions.add("End Asteroid");
            else if (layerGen.mMars)
                vein.dimensions.add("Mars");
            else if (layerGen.mMoon)
                vein.dimensions.add("Moon");


            vein.size = new int[]{16 + layerGen.mSize / GTConstants.LAYER_SIZE_STEP, 6 /* TODO Hardcoded */, 16 + layerGen.mSize / GTConstants.LAYER_SIZE_STEP};
        }
    }

    private static Materials getMaterialFromMeta(int meta) {
        return Materials.getAll().stream().filter(material -> material.mMetaItemSubID == meta).findAny().orElse(null);
    }

    public void writeJson(JsonElement element, String path) {
        try (Writer writer = Files.newBufferedWriter(configPath.resolve(path))) {
            JsonWriter jsonWriter = new JsonWriter(writer);
            gson.toJson(element, jsonWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
