package tfar.classicbar.impl.overlays.vanilla;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import tfar.classicbar.config.ClassicBarsConfig;
import tfar.classicbar.config.ConfigCache;
import tfar.classicbar.impl.BarOverlayImpl;
import tfar.classicbar.util.Color;
import tfar.classicbar.util.ModUtils;

public class Armor extends BarOverlayImpl {

    private static final EquipmentSlot[] armorList = new EquipmentSlot[]{EquipmentSlot.HEAD,
            EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public Armor() {
        super("armor");
    }

    @Override
    public boolean shouldRender(Player player) {
        return calculateArmorValue(player) >= 1;
    }

    @Override
    public void renderBar(ForgeGui gui, GuiGraphics graphics, Player player, int screenWidth, int screenHeight, int vOffset) {
        double armor = calculateArmorValue(player);
        double barWidth = getBarWidth(player);

        boolean warn = shouldFlash(player);
        float armorAlpha = warn ? (System.currentTimeMillis() / 500) % 2 : 1;
        int xStart = screenWidth / 2 + getHOffset();

        if (rightHandSide()) {
            xStart += WIDTH - barWidth;
        }

        int yStart = screenHeight - vOffset;
        //bar background
        renderBarBackground(graphics, player, screenWidth, screenHeight, vOffset);
        //how many layers are there? remember to start at 0
        int index = (int) Math.min(Math.ceil(armor / 20), ConfigCache.armor.size()) - 1;
        Color primary = getPrimaryBarColor(index, player);

        if (index == 0) {
            //calculate bar color
            primary.color2Gla(armorAlpha);
            //draw portion of bar based on armor
            renderPartialBar(graphics, xStart + 2, yStart + 2, barWidth);
        } else {
            //we have wrapped, draw 2 bars
            //draw first bar
            //case 1: bar is not capped and is partially filled
            if (armor % 20 != 0) {
                Color secondary = getSecondaryBarColor(index - 1, player);
                //draw complete first bar
                secondary.color2Gla(armorAlpha);
                renderFullBar(graphics, xStart + 2, yStart + 2);
                //draw partial second bar
                primary.color2Gl();
                double w = ModUtils.getWidth(armor % 20, 20);
                double f = xStart + (rightHandSide() ? WIDTH - w : 0);
                renderPartialBar(graphics, f + 2, yStart + 2, w);
            }
            //case 2, bar is a multiple of 20, or it is capped
            else {
                //draw complete second bar
                primary.color2Gla(armorAlpha);
                renderFullBar(graphics, xStart + 2, yStart + 2);
            }
            // now handle the low armor warning
            if (warn) {
                //draw one bar
                primary.color2Gla(armorAlpha);
                renderPartialBar(graphics, xStart + 2, yStart + 2, ModUtils.getWidth(armor - index * 20, 20));
            }
        }
    }

    @Override
    protected boolean shouldFlash(Player player) {
        return ClassicBarsConfig.lowArmorWarning.get() && getDamagedAmount(player) > 0;
    }

    @Override
    public double getBarWidth(Player player) {
        int armor = calculateArmorValue(player);
        return Math.ceil(WIDTH * Math.min(20, armor) / 20d);//armor can go above 20 in modded contexts!
    }

    @Override
    public Color getPrimaryBarColor(int index, Player player) {
        return ConfigCache.armor.get(index);
    }

    @Override
    public Color getSecondaryBarColor(int index, Player player) {
        return ConfigCache.armor.get(index);
    }

    @Override
    public boolean isFitted() {
        return !ClassicBarsConfig.fullArmorBar.get();
    }


    public static int getDamagedAmount(Player player) {
        int warningAmount = 0;
        for (EquipmentSlot slot : armorList) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof ArmorItem)) continue;
            int max = stack.getMaxDamage();
            int current = stack.getDamageValue();
            int percentage = 100;
            if (max != 0) percentage = 100 * (max - current) / (max);
            if (percentage < 5) {
                warningAmount += ((ArmorItem) stack.getItem()).getDefense();
            }
        }
        return warningAmount;
    }

    @Override
    public void renderText(GuiGraphics graphics, Player player, int width, int height, int vOffset) {
        int xStart = width / 2 + getIconOffset();
        int yStart = height - vOffset;
        double armor = calculateArmorValue(player);
        //draw armor amount
        int index = (int) Math.min(Math.ceil(armor / 20), ConfigCache.armor.size()) - 1;
        int c = getPrimaryBarColor(index, player).colorToText();
        textHelper(graphics, xStart, yStart, armor, c);
    }

    @Override
    public void renderIcon(GuiGraphics graphics, Player player, int width, int height, int vOffset) {
        int xStart = width / 2 + getIconOffset();
        int yStart = height - vOffset;
        //Draw armor icon
        ModUtils.drawTexturedModalRect(graphics, xStart, yStart, 43, 9, 9, 9);
    }

    private static int calculateArmorValue(Player player) {
        return player.getArmorValue();
    }
}