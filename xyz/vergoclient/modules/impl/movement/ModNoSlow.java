package xyz.vergoclient.modules.impl.movement;

import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import xyz.vergoclient.Vergo;
import xyz.vergoclient.event.Event;
import xyz.vergoclient.event.impl.EventSendPacket;
import xyz.vergoclient.event.impl.EventSlowdown;
import xyz.vergoclient.modules.Module;
import xyz.vergoclient.modules.OnEventInterface;
import xyz.vergoclient.util.MovementUtils;

public class ModNoSlow extends Module implements OnEventInterface {

	public ModNoSlow() {
		super("NoSlow", Category.MOVEMENT);
	}

	@Override
	public void onEvent(Event e) {

		if (e instanceof EventSlowdown && e.isPre()) {
			e.setCanceled(true);
		}

		if(MovementUtils.isMoving()) {
			if (mc.gameSettings.keyBindSprint.isPressed()) {
				mc.thePlayer.setSprinting(true);
			}
		}

		if(Vergo.config.modScaffold.isEnabled()) {
			return;
		}

		if (e instanceof EventSendPacket && e.isPre()) {

			if(mc.thePlayer.isBlocking()) {
				EventSendPacket event = (EventSendPacket) e;
				if(event.packet instanceof C08PacketPlayerBlockPlacement) {
					mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
				}
			}

		}

	}

}