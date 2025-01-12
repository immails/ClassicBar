package tfar.classicbar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import tfar.classicbar.api.BarOverlay;
import tfar.classicbar.compat.ModCompat;
import tfar.classicbar.config.ClassicBarsConfig;
import tfar.classicbar.config.ConfigCache;
import tfar.classicbar.impl.overlays.mod.Blood;
import tfar.classicbar.impl.overlays.mod.StaminaB;
import tfar.classicbar.impl.overlays.mod.Thirst;
import tfar.classicbar.impl.overlays.vanilla.*;
import tfar.classicbar.util.ModUtils;

import java.util.*;

public class EventHandler implements IGuiOverlay {

  private static final List<BarOverlay> all = new ArrayList<>();
  public static final Map<String, BarOverlay> registry = new HashMap<>();

  private static final List<BarOverlay> errored = new ArrayList<>();

  public static void register(BarOverlay iBarOverlay) {
    registry.put(iBarOverlay.name(), iBarOverlay);
  }

  public static void registerAll(BarOverlay... iBarOverlay) {
    Arrays.stream(iBarOverlay).forEach(overlay -> {
      if (overlay != null) {
      registry.put(overlay.name(), overlay);
      }
    });
  }

  public void render(ForgeGui gui, GuiGraphics matrices, float partialTick, int screenWidth, int screenHeight) {

    Entity entity = ModUtils.mc.getCameraEntity();
    if (!(entity instanceof Player player)) return;
    if (player.getAbilities().instabuild || player.isSpectator()) return;
    ModUtils.mc.getProfiler().push("classicbars_hud");

    for (BarOverlay overlay : all) {
      boolean rightHand = overlay.rightHandSide();
      try {
        overlay.render(gui, matrices, player, screenWidth, screenHeight, getOffset(gui, rightHand));
      } catch (Error e) {
        ClassicBar.logger.error("Removing broken overlay "+overlay.name());
        e.printStackTrace();
        errored.add(overlay);
      }
    }
    if (!errored.isEmpty()) all.removeAll(errored);

    // mc.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
    ModUtils.mc.getProfiler().pop();
  }

  public static void increment(ForgeGui gui,boolean side ,int amount){
    if (side)gui.rightHeight+=amount;
    else gui.leftHeight+=amount;
  }

  public static int getOffset(ForgeGui gui,boolean right) {
    return right ? gui.rightHeight : gui.leftHeight;
  }

  public static void cacheConfigs() {
    all.clear();
    ClassicBarsConfig.leftorder.get().stream().filter(s -> registry.get(s) != null).forEach(e -> all.add(registry.get(e).setSide(false)));
    ClassicBarsConfig.rightorder.get().stream().filter(s -> registry.get(s) != null).forEach(e -> all.add(registry.get(e).setSide(true)));
    all.removeAll(errored);
    ConfigCache.bake();
  }

  public static void sendModMessage(InterModEnqueueEvent e) {
    InterModComms.sendTo("vampirism", "disable-blood-bar", () -> true);
  }

  public static void setupOverlays(RegisterGuiOverlaysEvent e) {
    MinecraftForge.EVENT_BUS.addListener(EventHandler::disableOtherOverlays);
    e.registerBelow(VanillaGuiOverlay.ITEM_NAME.id(),ClassicBar.MODID,new EventHandler());

    //Register renderers for events
    ClassicBar.logger.info("Registering Vanilla Overlays");

    EventHandler.registerAll(new Absorption(), new Air(), new Armor(), new ArmorToughness(),
            new Health(), new Hunger(), new MountHealth());

    //mod renderers
    ClassicBar.logger.info("Registering Mod Overlays");
    if (ModCompat.vampirism.loaded)EventHandler.register(new Blood());
  //  if (ModCompat.feathers.loaded)EventHandler.register(new Feathers());
    if (ModCompat.parcool.loaded)EventHandler.register(new StaminaB());
    if (ModCompat.toughasnails.loaded)EventHandler.register(new Thirst());
    // if (ModList.get().isLoaded("randomthings")) MinecraftForge.EVENT_BUS.register(new LavaCharmRenderer());
    // if (ModList.get().isLoaded("lavawaderbauble")) {
    //    MinecraftForge.EVENT_BUS.register(new LavaWaderBaubleRenderer());
    // }

    //if (ModList.get().isLoaded("superiorshields"))
    //  MinecraftForge.EVENT_BUS.register(new SuperiorShieldRenderer());

    //MinecraftForge.EVENT_BUS.register(new BetterDivingRenderer());
    //  if (ModList.get().isLoaded("botania")) MinecraftForge.EVENT_BUS.register(new TiaraBarRenderer());
  }

  private static final List<ResourceLocation> vanilla_overlays = List.of(VanillaGuiOverlay.AIR_LEVEL.id(),VanillaGuiOverlay.ARMOR_LEVEL.id(),
          VanillaGuiOverlay.PLAYER_HEALTH.id(),VanillaGuiOverlay.MOUNT_HEALTH.id(),VanillaGuiOverlay.FOOD_LEVEL.id());
  public static void disableOtherOverlays(RenderGuiOverlayEvent.Pre e) {
    NamedGuiOverlay overlay = e.getOverlay();
    if (vanilla_overlays.contains(overlay.id())) e.setCanceled(true);
    else if (overlay.id().getNamespace().equals("parcool") && StaminaB.checkConfigs()) e.setCanceled(true);
    else if (ModCompat.toughasnails.loaded && Thirst.isEnabled() && Thirst.OVERLAY_ID.equals(overlay.id())) e.setCanceled(true);
  }
}