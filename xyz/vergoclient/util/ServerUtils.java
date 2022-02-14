package xyz.vergoclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class ServerUtils {

	public static Timer timer;
	
	public static boolean isOnHypixel() {
		
		try {
			Minecraft mc = Minecraft.getMinecraft();
			
			if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net")) {
				return true;
			}
		} catch (Exception e) {
			
		}
		
		return false;
		
	}
	
	public static boolean isOnBrokenlens() {
		
		try {
			Minecraft mc = Minecraft.getMinecraft();
			
			if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("brlns.net")) {
				return true;
			}
		} catch (Exception e) {
			
		}
		
		return false;
		
	}
	
	public static boolean isOnMineplex() {
		
		try {
			Minecraft mc = Minecraft.getMinecraft();
			
			if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("mineplex.com")) {
				return true;
			}
		} catch (Exception e) {
			
		}
		
		return false;
		
	}
	
	public static boolean isOnInvaded() {
		
		try {
			Minecraft mc = Minecraft.getMinecraft();
			
			if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("invadedlands.net")) {
				return true;
			}
		} catch (Exception e) {
			
		}
		
		return false;
		
	}
	
	public static boolean isOnMineMen() {
		
		try {
			Minecraft mc = Minecraft.getMinecraft();
			
			if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("minemen.club")) {
				return true;
			}
		} catch (Exception e) {
			
		}
		
		return false;
		
	}

	public static NetworkPlayerInfo networkPlayerInfo;

	public static int getHypixelNetworkPing() {
		NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID());
		return networkPlayerInfo == null ? 0 : networkPlayerInfo.getResponseTime();
	}

}
