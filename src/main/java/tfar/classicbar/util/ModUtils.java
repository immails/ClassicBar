package tfar.classicbar.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import tfar.classicbar.ClassicBar;

public class ModUtils {
  public static final int rightTextOffset = 82;

  public static final int leftTextOffset = -5;

  //maximum width the bar can be
  public static final int WIDTH = 77;

  public static ResourceLocation ICON_BAR = new ResourceLocation(ClassicBar.MODID, "textures/gui/health.png");
  public static final Minecraft mc = Minecraft.getInstance();
  private static final Font fontRenderer = mc.font;

  public static void drawTexturedModalRect(PoseStack stack, double x, int y, int textureX, int textureY, double width, int height) {
    mc.gui.blit(stack, (int) x, y, textureX, textureY, (int) width, height);
  }

  public static double getWidth(double d1, double d2) {
    double ratio = WIDTH * d1 / d2;
    return Math.ceil(ratio);
  }

  public static int getStringLength(String s) {
    return fontRenderer.width(s);
  }

  public static void drawScaledBar(PoseStack stack,double absorb, double maxHealth, int x, int y, boolean left) {
    int i = (int) getWidth(absorb, maxHealth);

    if (left) {
      drawTexturedModalRect(stack,x, y - 1, 0, 0, i + 1, 9);
      drawTexturedModalRect(stack,x + i + 1, y - 1, 79, 0, 2, 9);
    } else {
      drawTexturedModalRect(stack,x + 2, y -1, 80 - i, 0, i + 1, 9);
      drawTexturedModalRect(stack,x, y-1, 0, 0, 2, 9);
    }
  }

  public static void drawScaledBarBackground1(PoseStack stack, double barWidth, int x, int y, boolean right) {
    if (right) {
      drawTexturedModalRect(stack,x, y - 1, 0, 0, barWidth + 2, 9);
       drawTexturedModalRect(stack,x + barWidth + 2, y-1, WIDTH + 2, 0, 2, 9);
    } else {
      drawTexturedModalRect(stack, (int) (x + barWidth + 1), y - 1, 79, 0, 2, 9);
      drawTexturedModalRect(stack,x, y - 1, 0, 0, (int) (barWidth + 1), 9);
    }
  }

  public static void drawStringOnHUD(PoseStack stack,String string, int xOffset, int yOffset, int color) {
   /* double scale = numbers.numberScale;
    GlStateManager.pushMatrix();
    GlStateManager.scale(scale, scale, 1);
    xOffset /= scale;
    yOffset /= scale;
    int l = fontRenderer.getStringWidth(string);
    xOffset += (left) ? .4*l * (1 - scale) / scale : 0;
    GlStateManager.translate(16 * (1 - scale) / scale, 14 * (1 - scale) / scale, 0);*/

    xOffset += 2;
    yOffset += 2;

    fontRenderer.drawShadow(stack,string, xOffset, yOffset, color);
  }
}
