package tfar.classicbar.config;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.regex.Matcher;

import static tfar.classicbar.ClassicBar.logger;
import static tfar.classicbar.ColorUtilities.p1;
import static tfar.classicbar.config.ModConfig.*;

public class IdiotHandler {


    public IdiotHandler() {
    }

    public void idiotsTryingToParseBadHexColorsDOTJpeg() {
        colors.hungerBarColor = isSomeoneAttemptingToAddABadHexCodeToTheConfigQuestionMark(colors.hungerBarColor);
        colors.lavaBarColor = isSomeoneAttemptingToAddABadHexCodeToTheConfigQuestionMark(colors.lavaBarColor);
        colors.oxygenBarColor = isSomeoneAttemptingToAddABadHexCodeToTheConfigQuestionMark(colors.oxygenBarColor);
        colors.saturationBarColor = isSomeoneAttemptingToAddABadHexCodeToTheConfigQuestionMark(colors.saturationBarColor);
        colors.thirstBarColor = isSomeoneAttemptingToAddABadHexCodeToTheConfigQuestionMark(colors.thirstBarColor);
    }

    public String isSomeoneAttemptingToAddABadHexCodeToTheConfigQuestionMark(String s) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer p = mc.player;
        Matcher m = p1.matcher(s);
        if (!m.matches()) return s;
        logger.error("USER PUT IN A BAD HEX CODE");
        p.sendMessage(new TextComponentString(TextFormatting.RED + "USER PUT IN A BAD HEX CODE"));
        return "#000000";
    }

    public void emptyArrayFixer() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer p = mc.player;
        if (colors.advancedColors.healthFractions.length == 0) {
            p.sendMessage(new TextComponentString(TextFormatting.RED + "USER PUT IN AN EMPTY ARRAY FOR HEALTH SCALING"));
            if (colors.advancedColors.hexColors.length > 0) {
                colors.advancedColors.healthFractions = new Float[colors.advancedColors.hexColors.length];
                for (double i1 = 0; i1 < colors.advancedColors.hexColors.length; i1++) {
                    colors.advancedColors.healthFractions[(int) i1] = (float) (i1 / colors.advancedColors.hexColors.length);
                }
                return;
            } else {
                p.sendMessage(new TextComponentString(TextFormatting.RED + "USER PUT IN AN EMPTY ARRAY FOR HEALTH COLOR SCALING"));
                colors.advancedColors.healthFractions = new Float[]{.25f, .5f, 1f};
                colors.advancedColors.hexColors = new String[]{"#FF0000", "#FFFF00", "#00FF00"};
                return;
            }
        }
        if (colors.advancedColors.hexColors.length == 0) {
            p.sendMessage(new TextComponentString(TextFormatting.RED + "USER PUT IN AN EMPTY ARRAY FOR HEALTH COLOR SCALING"));
            colors.advancedColors.hexColors = new String[colors.advancedColors.hexColors.length];
            for (double i1 = 0; i1 < colors.advancedColors.healthFractions.length; i1++)
                colors.advancedColors.hexColors[(int) i1] = "#000000";
        }
    }
}