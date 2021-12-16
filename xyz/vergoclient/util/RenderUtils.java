package xyz.vergoclient.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import xyz.vergoclient.assets.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderUtils {

	// Made by lavaflowglow 11/19/2020 3:39 AM

	public static boolean shouldSetCustomYaw = false;
	public static float customYaw = 0;

	public static void setCustomYaw(float customYaw) {
		RenderUtils.customYaw = customYaw;
		shouldSetCustomYaw = true;
		mc.thePlayer.rotationYawHead = customYaw;
	}

	public static void resetPlayerYaw() {
		shouldSetCustomYaw = false;
	}

	public static float getCustomYaw() {
		return customYaw;
	}

	public static boolean shouldSetCustomPitch = false;
	public static float customPitch = 0;

	public static void setCustomPitch(float customPitch) {
		RenderUtils.customPitch = customPitch;
		shouldSetCustomPitch = true;
	}

	public static void resetPlayerPitch() {
		shouldSetCustomPitch = false;
	}

	public static float getCustomPitch() {
		return customPitch;
	}

	// Made by lavaflowglow 11/19/2020 3:39 AM

	public static Minecraft mc = Minecraft.getMinecraft();
	public static WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
	public static Tessellator tessellator = Tessellator.getInstance();

	// Someone gave me this code
	public static void drawPlayerBox(Double posX, Double posY, Double posZ) {
		double x = posX - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y = posY - Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z = posZ - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		// drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(1, 1, 1, 0.5F);
		RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z, x + 1.0, y + 2.0, z + 1.0));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void drawImg(ResourceLocation loc, double posX, double posY, double width, double height) {
		mc.getTextureManager().bindTexture(loc);
		float f = 1.0F / (float) width;
		float f1 = 1.0F / (float) height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(posX, (posY + height), 0.0D).tex(0 * f, ((0 + (float) height) * f1)).endVertex();
		worldrenderer.pos((posX + width), (posY + height), 0.0D).tex((0 + (float) width) * f, (0 + (float) height) * f1).endVertex();
		worldrenderer.pos((posX + width), posY, 0.0D).tex((0 + (float) width) * f, 0 * f1).endVertex();
		worldrenderer.pos(posX, posY, 0.0D).tex(0 * f, 0 * f1).endVertex();
		tessellator.draw();
	}

	public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		minX -= Minecraft.getMinecraft().getRenderManager().renderPosX;
		minY -= Minecraft.getMinecraft().getRenderManager().renderPosY;
		minZ -= Minecraft.getMinecraft().getRenderManager().renderPosZ;

		maxX -= Minecraft.getMinecraft().getRenderManager().renderPosX;
		maxY -= Minecraft.getMinecraft().getRenderManager().renderPosY;
		maxZ -= Minecraft.getMinecraft().getRenderManager().renderPosZ;

		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(3.0F);
		GL11.glColor4d(0, 1, 0, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		// drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(1, 1, 1, 1);

		worldrenderer.begin(1, DefaultVertexFormats.POSITION);
		worldrenderer.pos(minX, minY, minZ).endVertex();
		worldrenderer.pos(maxX, maxY, maxZ).endVertex();
		tessellator.draw();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);

	}

	public static void drawColoredBox(double corner1X, double corner1Y, double corner1Z, double corner2X,
			double corner2Y, double corner2Z, int color) {

		corner1X -= Minecraft.getMinecraft().getRenderManager().renderPosX;
		corner1Y -= Minecraft.getMinecraft().getRenderManager().renderPosY;
		corner1Z -= Minecraft.getMinecraft().getRenderManager().renderPosZ;

		corner2X -= Minecraft.getMinecraft().getRenderManager().renderPosX;
		corner2Y -= Minecraft.getMinecraft().getRenderManager().renderPosY;
		corner2Z -= Minecraft.getMinecraft().getRenderManager().renderPosZ;

		float var11 = (float) (color >> 24 & 255) / 255.0f;
		float var6 = (float) (color >> 16 & 255) / 255.0f;
		float var7 = (float) (color >> 8 & 255) / 255.0f;
		float var8 = (float) (color & 255) / 255.0f;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(var6, var7, var8, var11).endVertex();
		tessellator.draw();
	}

	public static void setColorForIcon(Color color) {
		GlStateManager.enableBlend();
		GlStateManager.color(((float) color.getRed()) / 255, ((float) color.getGreen()) / 255,
				((float) color.getBlue()) / 255);
	}

	public static void resetColor() {
		GlStateManager.color(1, 1, 1, 1);
	}

	public static Vec3 getRgbFromColor(int color) {
		float r = (float) (color >> 16 & 255) / 255.0f;
		float g = (float) (color >> 8 & 255) / 255.0f;
		float b = (float) (color & 255) / 255.0f;
		return new Vec3(r, g, b);
	}

	public static int getRainbow(double offset, float saturation, float brightness, long speedTime, int speedInt) {
		float hue = (float) ((System.currentTimeMillis() + offset) % (int) (speedInt * speedTime)
				/ (float) (speedInt * speedTime));
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}

	public static int getRainbow(double offset, float saturation, float brightness) {
		return getRainbow(offset, saturation, brightness, 1000, 4);
	}

	public static int getRainbow(double offset) {
		return getRainbow(offset, 1, 1);
	}

	public static int getRainbow() {
		return getRainbow(0, 1, 1);
	}

	public static void drawPointWithBlur(double startX, double startY, int color, int blurSize) {
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		for (double x = startX - blurSize; x < startX + blurSize; x++) {
			for (double y = startY - blurSize; y < startY + blurSize; y++) {
				Vec3 rgb = RenderUtils.getRgbFromColor(color);
				double opacity = MiscellaneousUtils.get2dDistance(x, y, startX, startY);
				if (opacity > blurSize)
					opacity = blurSize;
				opacity = 1 - (opacity / blurSize);
				int newColor = new Color(((float) rgb.xCoord), ((float) rgb.yCoord), ((float) rgb.zCoord),
						((float) opacity)).getRGB();
				Gui.drawRect(x, y, x + 1, y + 1, newColor);
			}
		}
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

	public static void draw2dCircle(double centerX, double centerY, double circleSize, double circleCompilation) {
		draw2dCircle(centerX, centerY, circleSize, circleCompilation, 4);
	}

	public static void draw2dCircle(double centerX, double centerY, double circleSize, double circleCompilation,
			float lineWidth) {

		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GL11.glLineWidth(lineWidth);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (int rotate = 360 + 90; rotate >= (360 - circleCompilation) + 90; rotate--) {
			double x = centerX + ((Math.cos(Math.toRadians(rotate)) * circleSize));
			double y = centerY - ((Math.sin(Math.toRadians(rotate)) * circleSize));
			GL11.glVertex2d(x, y);
		}
		GL11.glEnd();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();

	}

	public static void glColorWithInt(int color) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		GlStateManager.color(f, f1, f2, f3);
	}

	public static void setResourceLocationFromUrl(ResourceLocation resourceLocationIn, String url) {
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		Object object = texturemanager.getTexture(resourceLocationIn);

		if (object == null) {
			ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData((File) null, url,
					Icons.MISSING_TEXTURE.iconLocation, new ImageBufferDownload());
			new Thread(() -> {
				try {
					URLConnection connection = new URL(url).openConnection();
					connection.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
					threadDownloadImageData.setBufferedImage(ImageIO.read(connection.getInputStream()));
					texturemanager.loadTexture(resourceLocationIn, threadDownloadImageData);
					connection.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
			texturemanager.loadTexture(resourceLocationIn, threadDownloadImageData);
		}
	}

	// Funny github code
	public static void enableScissor(int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		int factor = scaledResolution.getScaleFactor();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor(x * factor, (scaledResolution.getScaledHeight() - (y + height)) * factor,
				((x + width) - x) * factor, ((y + height) - y) * factor);

		// disable GL_SCISSOR_TEST after bounding
	}

	public static void disableScissor() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public static void drawHorizontalGradient(double x, double y, double width, double height, int leftColor,
			int rightColor) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glPushMatrix();
		GL11.glBegin(7);
		glColorWithInt(leftColor);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y + height);
		glColorWithInt(rightColor);
		GL11.glVertex2d(x + width, y + height);
		GL11.glVertex2d(x + width, y);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
	}

	public static void drawVerticalGradient(double x, double y, double width, double height, int topColor,
			int bottomColor) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glPushMatrix();
		GL11.glBegin(7);
		glColorWithInt(topColor);
		GL11.glVertex2d(x + width, y);
		GL11.glVertex2d(x, y);
		glColorWithInt(bottomColor);
		GL11.glVertex2d(x, y + height);
		GL11.glVertex2d(x + width, y + height);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
	}

}
