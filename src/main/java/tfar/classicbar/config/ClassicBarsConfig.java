package tfar.classicbar.config;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.io.IOUtils;
import tfar.classicbar.ClassicBar;
import tfar.classicbar.EventHandler;
import tfar.classicbar.api.BarOverlay;
import tfar.classicbar.api.BarSettings;
import tfar.classicbar.impl.BarOverlayImpl;
import tfar.classicbar.impl.overlays.mod.StaminaB;
import tfar.classicbar.util.ModUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ClassicBar.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClassicBarsConfig {

  static ForgeConfigSpec.BooleanValue displayIcons;
  public static ForgeConfigSpec.BooleanValue displayToughnessBar;
  public static ForgeConfigSpec.BooleanValue fullAbsorptionBar;
  public static ForgeConfigSpec.BooleanValue fullArmorBar;
  public static ForgeConfigSpec.BooleanValue fullToughnessBar;
  public static ForgeConfigSpec.BooleanValue lowArmorWarning;
  public static ForgeConfigSpec.BooleanValue showSaturationBar;
  public static ForgeConfigSpec.BooleanValue showHydrationBar;
  public static ForgeConfigSpec.BooleanValue showHeldFoodOverlay;
  public static ForgeConfigSpec.BooleanValue showHeldDrinkOverlay;
  public static ForgeConfigSpec.BooleanValue showExhaustionOverlay;
  public static ForgeConfigSpec.BooleanValue showThirstExhaustionOverlay;

  public static ForgeConfigSpec.DoubleValue transitionSpeed;
  static ForgeConfigSpec.ConfigValue<String> hungerBarColor;
  static ForgeConfigSpec.ConfigValue<String> hungerBarDebuffColor;
  static ForgeConfigSpec.ConfigValue<String> saturationBarColor;
  static ForgeConfigSpec.ConfigValue<String> saturationBarDebuffColor;
  static ForgeConfigSpec.ConfigValue<String> thirstBarColor;
  static ForgeConfigSpec.ConfigValue<String> thirstBarDebuffColor;
  static ForgeConfigSpec.ConfigValue<String> hydrationBarColor;
  static ForgeConfigSpec.ConfigValue<String> hydrationBarDebuffColor;
  static ForgeConfigSpec.ConfigValue<String> airBarColor;
  static ForgeConfigSpec.ConfigValue<List<? extends String>> armorColors;
  static ForgeConfigSpec.ConfigValue<List<? extends String>> armorToughnessColors;
  static ForgeConfigSpec.ConfigValue<List<? extends String>> absorptionColors;
  static ForgeConfigSpec.ConfigValue<List<? extends String>> absorptionPoisonColors;
  static ForgeConfigSpec.ConfigValue<List<? extends String>> absorptionWitherColors;
  public static ForgeConfigSpec.ConfigValue<List<? extends Double>> normalFractions;
  static ForgeConfigSpec.ConfigValue<List<? extends String>> normalColors;
  public static ForgeConfigSpec.ConfigValue<List<? extends Double>> poisonedFractions;
  static ForgeConfigSpec.ConfigValue<List<? extends String>> poisonedColors;
  public static ForgeConfigSpec.ConfigValue<List<? extends Double>> witheredFractions;
  static ForgeConfigSpec.ConfigValue<List<? extends String>> witheredColors;
  public static ForgeConfigSpec.ConfigValue<String> lavaBarColor;
  public static ForgeConfigSpec.ConfigValue<String> flightBarColor;

  public static ForgeConfigSpec.ConfigValue<List<? extends String>> leftorder;
  public static ForgeConfigSpec.ConfigValue<List<? extends String>> rightorder;

  public ClassicBarsConfig(ForgeConfigSpec.Builder builder) {
    builder.push("general");
    displayIcons = builder.define("display_icons", true);

    displayToughnessBar = builder.comment("Whether to show icons next to the bars").define("display_icons", true);
    fullAbsorptionBar = builder.define("full_absorption_bar", false);
    fullArmorBar = builder.define("full_armor_bar", false);
    fullToughnessBar = builder.define("full_toughness_bar", false);
    lowArmorWarning = builder.define("display_low_armor_warning", true);

    showSaturationBar = builder.define("show_saturation_bar", true);
    showHydrationBar = builder.define("show_hydration_bar", true);
    showHeldFoodOverlay = builder.define("show_held_food_overlay", true);
    showHeldDrinkOverlay = builder.define("show_held_drink_overlay", true);
    showExhaustionOverlay = builder.define("show_exhaustion_overlay", true);
    showThirstExhaustionOverlay = builder.define("show_thirst_exhaustion_overlay", true);
    transitionSpeed = builder.defineInRange("transition_speed", 3, 0, Double.MAX_VALUE);

    hungerBarColor = builder.define("hunger_bar_color","#B34D00",String.class::isInstance);
    hungerBarDebuffColor = builder.define("hunger_bar_debuff_color","#249016",String.class::isInstance);
    thirstBarColor = builder.define("thirstr_bar_color","#1C5EE4",String.class::isInstance);
    thirstBarDebuffColor = builder.define("thirst_bar_debuff_color","#5A891C",String.class::isInstance);
    airBarColor = builder.define("air_bar_color","#00E6E6",String.class::isInstance);
    saturationBarColor = builder.define("saturation_bar_color","#FFCC00",String.class::isInstance);
    saturationBarDebuffColor = builder.define("saturation_bar_debuff_color","#87BC00",String.class::isInstance);
    hydrationBarColor = builder.define("hydration_bar_color","#00A3E2",String.class::isInstance);
    hydrationBarDebuffColor = builder.define("hydration_bar_debuff_color","#85CF25",String.class::isInstance);
    lavaBarColor = builder.define("lava_bar_color","#FF8000",String.class::isInstance);
    flightBarColor = builder.define("flight_bar_color","#FFFFFF",String.class::isInstance);

    armorColors = builder.defineList("armor_color_values", Lists.newArrayList("#AAAAAA", "#FF5500", "#FFC747", "#27FFE3", "#00FF00", "#7F00FF"),String.class::isInstance);
    armorToughnessColors = builder.defineList("armor_toughness_color_values", Lists.newArrayList("#AAAAAA", "#FF5500", "#FFC747", "#27FFE3", "#00FF00", "#7F00FF"),String.class::isInstance);
    absorptionColors = builder.defineList("absorption_color_values", Lists.newArrayList("#D4AF37", "#C2C73B", "#8DC337", "#36BA77", "#4A5BC4", "#D89AE2", "#DF9DC7", "#DFA99D", "#D4DF9D", "#3E84C6", "#B8C1E8", "#DFDFDF"),String.class::isInstance);
    absorptionPoisonColors = builder.defineList("absorption_poison_color_values", Lists.newArrayList("#D4AF37", "#C2C73B", "#8DC337", "#36BA77", "#4A5BC4", "#D89AE2", "#DF9DC7", "#DFA99D", "#D4DF9D", "#3E84C6", "#B8C1E8", "#DFDFDF"),String.class::isInstance);
    absorptionWitherColors = builder.defineList("absorption_wither_color_values", Lists.newArrayList("#D4AF37", "#C2C73B", "#8DC337", "#36BA77", "#4A5BC4", "#D89AE2", "#DF9DC7", "#DFA99D", "#D4DF9D", "#3E84C6", "#B8C1E8", "#DFDFDF"),String.class::isInstance);

    normalColors = builder.defineList("normal_colors", Lists.newArrayList("#FF0000", "#FFFF00", "#00FF00"),String.class::isInstance);
    normalFractions = builder.defineList("normal_fractions", Lists.newArrayList(.25, .5, .75),Double.class::isInstance);
    poisonedColors = builder.defineList("poisoned_colors", Lists.newArrayList("#00FF00", "#55FF55", "#00FF00"),String.class::isInstance);
    poisonedFractions = builder.defineList("poisoned_fractions", Lists.newArrayList(.25, .5, .75),Double.class::isInstance);
    witheredColors = builder.defineList("withered_colors", Lists.newArrayList("#555555", "#AAAAAA", "#555555"),String.class::isInstance);
    witheredFractions = builder.defineList("withered_fractions", Lists.newArrayList(.25, .5, .75),Double.class::isInstance);

    leftorder = builder.defineList("left_order", Lists.newArrayList("health","armor","absorption","lavacharm","lavacharm2"),String.class::isInstance);
    rightorder = builder.defineList("right_order", Lists.newArrayList("blood","health_mount","food","thirst_level", StaminaB.name,"feathers","armor_toughness","air","flighttiara","decay"),String.class::isInstance);
  }

  @SubscribeEvent
  public static void onConfigChanged(ModConfigEvent event) {
      EventHandler.cacheConfigs();
      readBarSettings();
      ClassicBar.logger.info("Syncing Classic Bar Configs");
  }

  static File settingsPath = new File("config/" + ClassicBar.MODID + "/");


  public static void readBarSettings() {

    if (settingsPath.exists()) {
    } else {
      settingsPath.mkdir();
      writeDefault();
    }

    File[] files = settingsPath.listFiles();

    for (File file : files) {

      Reader reader = null;
      try {
        reader = new FileReader(file);

        JsonReader jsonReader = new JsonReader(reader);

        Gson gson = new GsonBuilder().registerTypeAdapter(ResourceLocation.class,new ResourceLocation.Serializer()).create();

        BarSettings barSettings = gson.fromJson(jsonReader, BarSettings.class);
        String fileName = file.getName();
        String name = fileName.substring(0,fileName.length() - ".json".length());
        BarOverlay barOverlay = EventHandler.registry.get(name);
        barOverlay.setBarSettings(barSettings);

      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      } finally {
        IOUtils.closeQuietly(reader);
      }
    }
  }

  /*public static class BarSettingsDeserializer implements JsonDeserializer<BarSettings> {

    @Override
    public BarSettings deserialize
            (JsonElement jElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
      JsonObject jObject = jElement.getAsJsonObject();
      int intValue = jObject.get("valueInt").getAsInt();
      String stringValue = jObject.get("valueString").getAsString();
      return new BarSettings(intValue, stringValue);
    }
  }*/

  public static void writeDefault() {
    Gson gson = new Gson();
    makeDefaultBarSettings();
    for (BarOverlay barOverlay : EventHandler.registry.values()) {
      File file = new File("config/" + ClassicBar.MODID + "/"+ barOverlay.name()+".json");
      JsonWriter writer = null;
      try {
        writer = gson.newJsonWriter(new FileWriter(file));
        writer.setIndent("    ");

        BarSettings barSettings = defaults.getOrDefault(barOverlay.name(),nullSettings);
        gson.toJson(barSettings.toJson(), writer);

      } catch (Exception e) {
        ClassicBar.logger.error("Couldn't save config");
        e.printStackTrace();
        throw new RuntimeException(e);
      } finally {
        IOUtils.closeQuietly(writer);
      }
    }
  }

  private static final BarSettings nullSettings = new BarSettings();

  static void makeDefaultBarSettings() {

    nullSettings.show_text = true;
    nullSettings.icon = BarOverlayImpl.GUI_ICONS_LOCATION;

    BarSettings absorbSettings = nullSettings.copy();
    defaults.put("absorption",absorbSettings);

    BarSettings airSettings = nullSettings.copy();
    defaults.put("air",airSettings);

    BarSettings armorSettings = nullSettings.copy();
    defaults.put("armor",armorSettings);

    BarSettings armorToughnessSettings = nullSettings.copy();
    armorToughnessSettings.icon = BarOverlayImpl.ICON_BAR;
    defaults.put("armor_toughness",armorToughnessSettings);

    BarSettings bloodSettings = nullSettings.copy();
    bloodSettings.icon = ModUtils.VAMPIRISM_ICONS;
    defaults.put("blood",bloodSettings);

    BarSettings foodSettings = nullSettings.copy();
    defaults.put("food",foodSettings);

    BarSettings healthSettings = nullSettings.copy();
    defaults.put("health",healthSettings);

    BarSettings healthMountSettings = nullSettings.copy();
    defaults.put("health_mount",healthMountSettings);
  }



  private static final Map<String,BarSettings> defaults = new HashMap<>();

}