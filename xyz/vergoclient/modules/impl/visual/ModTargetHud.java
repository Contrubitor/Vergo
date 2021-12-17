package xyz.vergoclient.modules.impl.visual;

import java.awt.Color;
import java.text.DecimalFormat;

import javafx.animation.Animation;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import optifine.MathUtils;
import org.lwjgl.opengl.GL11;
import sun.font.FontManager;
import xyz.vergoclient.Vergo;
import xyz.vergoclient.assets.Colors;
import xyz.vergoclient.event.Event;
import xyz.vergoclient.event.impl.EventRenderGUI;
import xyz.vergoclient.event.impl.EventTick;
import xyz.vergoclient.modules.Module;
import xyz.vergoclient.modules.OnEventInterface;
import xyz.vergoclient.modules.impl.combat.ModKillAura;
import xyz.vergoclient.modules.impl.combat.ModTPAura;
import xyz.vergoclient.settings.ModeSetting;
import xyz.vergoclient.settings.NumberSetting;
import xyz.vergoclient.ui.fonts.FontUtil;
import xyz.vergoclient.ui.guis.GuiClickGui;
import xyz.vergoclient.ui.guis.GuiNewClickGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import xyz.vergoclient.util.*;

public class ModTargetHud extends Module implements OnEventInterface {

	public ModTargetHud() {
		super("TargetHud", Category.VISUAL);
	}

	public float animation = 0;

	public ModeSetting mode = new ModeSetting("Mode", "Complex", "Complex", "Paper", "Coinchan");
	public NumberSetting xOffset = new NumberSetting("X position", (new ScaledResolution(mc).getScaledWidth() / 2) - 110, 0, new ScaledResolution(mc).getScaledWidth() - 220, 1),
			yOffset = new NumberSetting("Y position", ((new ScaledResolution(mc).getScaledHeight() / 8) * 6) - 32.5, 0, new ScaledResolution(mc).getScaledHeight() - 65, 1);
		    /*heartSliderX = new NumberSetting("Heart SliderX", 45, 0, 200, 1 ), heartSliderY = new NumberSetting("Heart SliderY", 45, 0, 200, 1 ),
			healthSliderX = new NumberSetting("Health SliderX", 45, 0, 200, 1 ), healthSliderY = new NumberSetting("Health SliderY", 45, 0, 200, 1 ),
			healthWidth = new NumberSetting("Health Width", 140, 0, 250, 1),
		    nameSliderX = new NumberSetting("NameSliderX", 0, 0, 200, 1), nameSliderY = new NumberSetting("NameSliderY", 0, 0, 200, 1 ),
			characterX = new NumberSetting("CharX", 0, 0, 200, 1), characterY = new NumberSetting("CharY", 0, 0, 200, 1),
	        characterScale = new NumberSetting("CharScale", 0, 0, 1000, 1);*/

	@Override
	public void loadSettings() {

		xOffset.maximum = new ScaledResolution(mc).getScaledWidth() - 220;
		yOffset.maximum = new ScaledResolution(mc).getScaledHeight() - 65;

		if (xOffset.getValueAsDouble() < 0) {
			xOffset.setValue(0);
		}

		if (xOffset.getValueAsDouble() > xOffset.getMaximum()) {
			xOffset.setValue(xOffset.getMaximum());
		}

		if (yOffset.getValueAsDouble() < 0) {
			yOffset.setValue(0);
		}

		if (yOffset.getValueAsDouble() > xOffset.getMaximum()) {
			yOffset.setValue(yOffset.getMaximum());
		}

		addSettings(mode, xOffset, yOffset/*, heartSliderX, heartSliderY, healthSliderX, healthSliderY, healthWidth, nameSliderX, nameSliderY, characterX, characterY, characterScale*/);
	}

	public static transient double healthBarTarget = 0, healthBar = 0, hurtTime = 0, hurtTimeTarget = 0;

	@Override
	public void onEvent(Event e) {
		if (e instanceof EventRenderGUI && e.isPre()) {

			if (mode.is("Complex")) {

				EntityLivingBase target = null;

				if (Vergo.config.modKillAura.isEnabled() && ModKillAura.target != null) {
					target = ModKillAura.target;
				} else {
					if (Vergo.config.modTPAura.isEnabled() && ModTPAura.target != null) {
						target = ModTPAura.target;
					}
				}

				if (target == null) {
					if (mc.currentScreen instanceof GuiClickGui || mc.currentScreen instanceof GuiNewClickGui) {
						target = mc.thePlayer;
					} else {
						healthBar = 0;
						return;
					}
				}

				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();
				GlStateManager.translate(xOffset.getValueAsDouble(), yOffset.getValueAsDouble(), 0);

				// Lower is faster, higher is slower
				double barSpeed = 5;
				if (healthBar > healthBarTarget) {
					healthBar = ((healthBar) - ((healthBar - healthBarTarget) / barSpeed));
				} else if (healthBar < healthBarTarget) {
					healthBar = ((healthBar) + ((healthBarTarget - healthBar) / barSpeed));
				}

				if (hurtTime > hurtTimeTarget) {
					hurtTime = ((hurtTime) - ((hurtTime - hurtTimeTarget) / barSpeed));
				} else if (hurtTime < healthBarTarget) {
					hurtTime = ((hurtTime) + ((hurtTimeTarget - hurtTime) / barSpeed));
				}

				ScaledResolution sr = new ScaledResolution(mc);
				FontRenderer fr = mc.fontRendererObj;
				DecimalFormat dec = new DecimalFormat("#");

				healthBarTarget = sr.getScaledWidth() / 2 - 41 + (((140) / (target.getMaxHealth())) * (target.getHealth()));

				int color = 0xff3396FF;

				if (Vergo.config.modRainbow.isEnabled()) {
					float hue1 = System.currentTimeMillis() % (int) ((4) * 1000) / (float) ((4) * 1000);
					color = Color.HSBtoRGB(hue1, 0.65f, 1);
				}

				// Main box
				RenderUtils.drawRoundedRect(0, 0, 220, 65, 3, new Color(37, 38, 54));

				// Health bar
				fr.drawString("❤", 52, 46, new Color(48, 194, 124).getRGB(), false);
				healthBarTarget = (140 * (target.getHealth() / target.getMaxHealth()));
				if (healthBar > 140) {
					healthBar = 140;
				}

				RenderUtils.drawRoundedRect(65, 45, 140, 10f, 2, new Color(23, 23, 33));
				RenderUtils.drawRoundedRect(65, 45, healthBar, 10f, 2, new Color(48, 194, 124));

				// Name
				if(target.getDisplayName().getFormattedText().length() < 9) {
					int length = target.getDisplayName().getFormattedText().length();
					int nine = 9;
					int newLength = length - nine;
					FontUtil.bakakakBig.drawString(target.getDisplayName().getFormattedText(), 99 + newLength, 16, color);
				}
				if (target.getDisplayName().getFormattedText().length() == 9) {
					FontUtil.bakakakBig.drawString(target.getDisplayName().getFormattedText(), 99, 16, color);
				} else {
					if (target.getDisplayName().getFormattedText().length() >= 10) {
						int length = target.getDisplayName().getFormattedText().length();
						int nine = 9;
						int newLength = length - nine;
						FontUtil.bakakakBig.drawString(target.getDisplayName().getFormattedText(), 99 - newLength, 16, color);
					}
				}

				// 3D model of the target
				GlStateManager.disableBlend();
				GlStateManager.color(1, 1, 1, 1);
				GuiInventory.drawEntityOnScreen(27, 58, (int) (45 / target.height), 0, 0, target);
			}

		else if(mode.is("Paper")) {

				EntityLivingBase target = null;

				if (Vergo.config.modKillAura.isEnabled() && ModKillAura.target != null) {
					target = ModKillAura.target;
				} else {
					if (Vergo.config.modTPAura.isEnabled() && ModTPAura.target != null) {
						target = ModTPAura.target;
					}
				}

				if (target == null) {
					if (mc.currentScreen instanceof GuiClickGui || mc.currentScreen instanceof GuiNewClickGui) {
						target = mc.thePlayer;
					} else {
						healthBar = 0;
						return;
					}
				}

				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();
				GlStateManager.translate(xOffset.getValueAsDouble(), yOffset.getValueAsDouble(), 0);

				// Lower is faster, higher is slower
				double barSpeed = 5;
				if (healthBar > healthBarTarget) {
					healthBar = ((healthBar) - ((healthBar - healthBarTarget) / barSpeed));
				} else if (healthBar < healthBarTarget) {
					healthBar = ((healthBar) + ((healthBarTarget - healthBar) / barSpeed));
				}

				if (hurtTime > hurtTimeTarget) {
					hurtTime = ((hurtTime) - ((hurtTime - hurtTimeTarget) / barSpeed));
				} else if (hurtTime < healthBarTarget) {
					hurtTime = ((hurtTime) + ((hurtTimeTarget - hurtTime) / barSpeed));
				}

				ScaledResolution sr = new ScaledResolution(mc);
				FontRenderer fr = mc.fontRendererObj;
				DecimalFormat dec = new DecimalFormat("#");

				healthBarTarget = sr.getScaledWidth() / 2 - 41 + (((140) / (target.getMaxHealth())) * (target.getHealth()));

				int color = 0xff3396FF;

				if (Vergo.config.modRainbow.isEnabled()) {
					float hue1 = System.currentTimeMillis() % (int) ((4) * 1000) / (float) ((4) * 1000);
					color = Color.HSBtoRGB(hue1, 0.65f, 1);
				}

				// Main box
				RenderUtils.drawRoundedRect(0, 0, 220, 65, 3, new Color(37, 38, 54));

				// Health bar
				healthBarTarget = (140 * (target.getHealth() / target.getMaxHealth()));
				if (healthBar > 140) {
					healthBar = 140;
				}

				RenderUtils.drawRoundedRect(16, 43, 188, 12f, 2, new Color(23, 23, 33));
				RenderUtils.drawRoundedRect(16, 43, healthBar + 48, 12f, 2, new Color(48, 194, 124));

				// Name
				if(target.getDisplayName().getFormattedText().length() < 9) {
					int length = target.getDisplayName().getFormattedText().length();
					int nine = 9;
					int newLength = length - nine;
					FontUtil.bakakakBig.drawString(target.getDisplayName().getFormattedText(), 85 + newLength, 13, color);
				}
				if (target.getDisplayName().getFormattedText().length() == 9) {
					FontUtil.bakakakBig.drawString(target.getDisplayName().getFormattedText(), 85, 13, color);
				} else {
					if (target.getDisplayName().getFormattedText().length() >= 10) {
						int length = target.getDisplayName().getFormattedText().length();
						int nine = 9;
						int newLength = length - nine;
						FontUtil.bakakakBig.drawString(target.getDisplayName().getFormattedText(), 85 - newLength, 13, color);
					}
				}
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
		} else if(mode.is("Coinchan")) {

				EntityLivingBase target = null;

				if (Vergo.config.modKillAura.isEnabled() && ModKillAura.target != null) {
					target = ModKillAura.target;
				} else {
					if (Vergo.config.modTPAura.isEnabled() && ModTPAura.target != null) {
						target = ModTPAura.target;
					}
				}

				if (target == null) {
					if (mc.currentScreen instanceof GuiClickGui || mc.currentScreen instanceof GuiNewClickGui) {
						target = mc.thePlayer;
					} else {
						return;
					}
				}

				// EXTREMELY SECRET. DO NOT FUCKING RE-USE. SERIOUSLY, I WILL GET FUCKING SUED.

				Color color;

				GL11.glPushMatrix();
				String playerName = target.getName();

				String clientTag = "";

				/*IRCUser user = IRCUser.getIRCUserByIGN(playerName);

				if (user != null) {
					clientTag = "\247" + user.rank.charAt(0) + "[" + user.rank.substring(1) + "|" + user.username + "] \247f";
				}*/

				String healthStr = Math.round(target.getHealth() * 10) / 10d + " hp";
				float width = (float) Math.max(75, FontUtil.arialBig.getStringWidth(clientTag + playerName) + 25);

				/*if (BlurBuffer.blurEnabled()) {
					BlurBuffer.blurRoundArea(x + .5f, y + .5f, 28 + width - 1f, 30 - 1f, 2f, true);
				}*/

				//更改TargetHUD在屏幕坐标的初始位置
				GL11.glTranslatef(300, 250, 0);
				RenderUtils.drawBorderedRect(0, 0, 40 + width, 40, 1, new Color(0, 0, 0, 255), new Color(70, 70, 70, 255));

				FontUtil.arialBig.drawString(clientTag + playerName, 30f, 3f, Colors.WHITE.getColor());
				FontUtil.arialBig.drawString(healthStr, 37 + width - FontUtil.arialBig.getStringWidth(healthStr) - 2, 4f, 0xffcccccc);

				boolean isNaN = Float.isNaN(target.getHealth());
				float health = isNaN ? 20 : target.getHealth();
				float maxHealth = isNaN ? 20 : target.getMaxHealth();
				float healthPercent = MiscellaneousUtils.clampValue(health / maxHealth, 0, 1);

				RenderUtils.drawRoundedRect(25, 31.5f, 26 + width - 2, 34.5f, 3, new Color(15, 15, 15));

				float barWidth = (26 + width - 2) - 37;
				float drawPercent = 47 + (barWidth / 100) * (healthPercent * 100);

				if (this.animation <= 0) {
					this.animation = drawPercent;
				}

				if (target.hurtTime <= 6) {
					this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float) Math.max(10, (Math.abs(this.animation - drawPercent) * 30) * 0.4));
				}


				RenderUtils.drawRoundedRect(30, 31.5f, this.animation,8f, 2, new Color(77, 255, 91));
				RenderUtils.drawRoundedRect(30, 31.5f, drawPercent, 8f, 2, new Color(10, 38, 11));

				float f3 = 37 + (barWidth / 100f) * (target.getTotalArmorValue() * 5);
				this.renderArmor((EntityPlayer) target);

				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();

				GlStateManager.resetColor();
				// 3D model of the target

				for (NetworkPlayerInfo info : GuiPlayerTabOverlay.field_175252_a.sortedCopy(mc.getNetHandler().getPlayerInfoMap())) {
					if (mc.theWorld.getPlayerEntityByUUID(info.getGameProfile().getId()) == target) {
						mc.getTextureManager().bindTexture(info.getLocationSkin());
						GlStateManager.disableBlend();
						GlStateManager.color(1, 1, 1, 1);
						GuiInventory.drawEntityOnScreen(15, 34, (int) (28 / target.height), 0, 0, target);
						GL11.glPopMatrix();
						GlStateManager.bindTexture(0);
						break;
					}
				}
			}

		}

		else if (e instanceof EventTick && e.isPre()) {
			if (xOffset.maximum > new ScaledResolution(mc).getScaledWidth() - 220)
				xOffset.maximum = new ScaledResolution(mc).getScaledWidth() - 220;
			if (yOffset.maximum > new ScaledResolution(mc).getScaledHeight() - 65)
				yOffset.maximum = new ScaledResolution(mc).getScaledHeight() - 65;
		}

	}

	public void renderArmor(EntityPlayer player) {
		int xOffset = 60;

		int index;
		ItemStack stack;
		for (index = 3; index >= 0; --index) {
			stack = player.inventory.armorInventory[index];
			if (stack != null) {
				xOffset -= 8;
			}
		}

		for (index = 3; index >= 0; --index) {
			stack = player.inventory.armorInventory[index];
			if (stack != null) {
				ItemStack armourStack = stack.copy();
				if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
					armourStack.stackSize = 1;
				}

				renderItemStack(armourStack, xOffset, 12);
				xOffset += 16;
			}
		}
	}

	private void renderItemStack(ItemStack stack, int x, int y) {
		GlStateManager.pushMatrix();

		GlStateManager.disableAlpha();
		this.mc.getRenderItem().zLevel = -150.0F;

		GlStateManager.disableCull();

		this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);

		GlStateManager.enableCull();

		this.mc.getRenderItem().zLevel = 0;

		GlStateManager.disableBlend();

		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		GlStateManager.disableDepth();
		GlStateManager.disableLighting();

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();

		GlStateManager.scale(2.0F, 2.0F, 2.0F);

		GlStateManager.enableAlpha();

		GlStateManager.popMatrix();
	}

	private void renderPlayer2d(final double n, final double n2, final float n3, final float n4, final int n5, final int n6, final int n7, final int n8, final float n9, final float n10, final AbstractClientPlayer abstractClientPlayer) {
		mc.getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
		GL11.glEnable(3042);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Gui.drawScaledCustomSizeModalRect((int)n, (int)n2, n3, n4, n5, n6, n7, n8, n9, n10);
		GL11.glDisable(3042);
	}

}