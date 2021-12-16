package xyz.vergoclient.modules.impl.combat;

import java.awt.AWTException;
import java.text.DecimalFormat;

import org.apache.commons.lang3.RandomUtils;

import com.google.common.util.concurrent.AtomicDouble;

import xyz.vergoclient.Vergo;
import xyz.vergoclient.modules.Module;
import xyz.vergoclient.modules.OnSettingChangeInterface;
import xyz.vergoclient.settings.BooleanSetting;
import xyz.vergoclient.settings.NumberSetting;
import xyz.vergoclient.settings.SettingChangeEvent;
import xyz.vergoclient.util.MiscellaneousUtils;
import xyz.vergoclient.util.TimerUtil;

public class ModAutoClicker extends Module implements OnSettingChangeInterface {

	public ModAutoClicker() {
		super("AutoClicker", "Clicks for you", Category.COMBAT);
	}
	
	public NumberSetting minCps = new NumberSetting("Min CPS", 10, 1, 100, 0.5),
			maxCps = new NumberSetting("Max CPS", 14, 1, 100, 0.5);
	public BooleanSetting dragClick = new BooleanSetting("Drag Clicker", false),
			overrideHitDelay = new BooleanSetting("Override hit delay", true);
	
	@Override
	public void loadSettings() {
		addSettings(minCps, maxCps, overrideHitDelay, dragClick);
	}
	
	@Override
	public void onSettingChange(SettingChangeEvent e) {
		if (e.setting == minCps || e.setting == maxCps) {
			if (cps.get() < minCps.getValueAsDouble() || cps.get() > maxCps.getValueAsDouble()) {
				setCps();
			}
			if (e.setting == minCps && minCps.getValueAsDouble() > maxCps.getValueAsDouble()) {
				maxCps.setValue(minCps.getValueAsDouble());
			}
			if (e.setting == maxCps && maxCps.getValueAsDouble() < minCps.getValueAsDouble()) {
				minCps.setValue(maxCps.getValueAsDouble());
			}
		}
	}
	
	public static transient AtomicDouble cps = new AtomicDouble(1);
	public static transient TimerUtil cpsTimer = new TimerUtil(), dragClickTimer = new TimerUtil();
	public static transient Thread autoClickerThread = new Thread(() -> {
		while (true) {
			
			try {
				update();
			} catch (Exception e) {
				
			}
			
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				
			}
		}
	}, "AutoClicker Thread");
	
	static {
		autoClickerThread.start();
	}
	
	public static synchronized void setCps() {
		try {
			if (Vergo.config.modAutoClicker.minCps.getValueAsDouble() == Vergo.config.modAutoClicker.maxCps.getValueAsDouble()){
				cps.set(Vergo.config.modAutoClicker.minCps.getValueAsDouble());
			}else {
				cps.set(RandomUtils.nextDouble(Vergo.config.modAutoClicker.minCps.getValueAsDouble(), Vergo.config.modAutoClicker.maxCps.getValueAsDouble()));
			}
		} catch (Exception e) {
			cps.set(RandomUtils.nextDouble(10, 14));
		}
	}
	
	@Override
	public String getInfo() {
		if (Vergo.config.modAutoClicker.dragClick.isEnabled() && (dragClickTimer.hasTimeElapsed(600, false))) {
			return "00.00 CPS";
		}
		return new DecimalFormat("#00.00").format(cps.get()) + " CPS";
	}
	
	public static synchronized void update() {
		if (Vergo.config.modAutoClicker.dragClick.isEnabled() && (dragClickTimer.hasTimeElapsed(700, true) || dragClickTimer.hasTimeElapsed(400, false))) {
			
		}
		else if (Vergo.config.modAutoClicker.isEnabled() && mc.gameSettings.keyBindAttack.isKeyDown() && !mc.gameSettings.keyBindUseItem.isKeyDown() && cpsTimer.hasTimeElapsed((long) (1000 / cps.get()), true)) {
			setCps();
			try {
				if (Vergo.config.modAutoClicker.overrideHitDelay.isEnabled()) {
					mc.leftClickCounter = 0;
				}
				MiscellaneousUtils.simulateMouseClick();
			} catch (AWTException e) {
				
			}
		}
	}
	
}
