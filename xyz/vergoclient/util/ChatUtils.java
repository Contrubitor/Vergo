package xyz.vergoclient.util;

import net.minecraft.network.Packet;
import xyz.vergoclient.Vergo;
import xyz.vergoclient.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ChatUtils {

	// More convenient?
	private static String prefix = "§4Vergo §f>> ";

	public static void addChatMessage(Object message, boolean displayRawMessage) {
		
		if (ModuleManager.currentlyLoadingConfig)
			return;
		
		if (displayRawMessage) {
			addChatMessage(new ChatComponentText(message + ""));
		}else {
			addChatMessage(new ChatComponentText(prefix + message));
		}
	}

	public static void addDevMessage(Object message) {

		if (ModuleManager.currentlyLoadingConfig)
			return;


		if(Vergo.isDev) {
			addChatMessage(new ChatComponentText("§4V§7Dev §8§o>> §r" + message));
		} else {
			// do nothing
		}
	}

	public static void addProtMsg(Object message) {

		if (ModuleManager.currentlyLoadingConfig)
			return;


		addChatMessage(new ChatComponentText("[§2Vergo Protection§r] " + message));
	}
	
	// For objects
	public static void addChatMessage(Object message) {
		
		if (ModuleManager.currentlyLoadingConfig)
			return;
		
		addChatMessage(message, false);
	}
	
	// Allows you to pass though a style along with a message
	public static void addChatMessage(Object message, ChatStyle style) {
		
		if (ModuleManager.currentlyLoadingConfig)
			return;
		
		ChatComponentText component = new ChatComponentText(prefix + message);
		component.setChatStyle(style);
		addChatMessage(component);
	}
	
	// Allows you to pass though the raw chat component
	public static void addChatMessage(IChatComponent component) {
		
		if (ModuleManager.currentlyLoadingConfig)
			return;
		
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(component);
	}
	
}
