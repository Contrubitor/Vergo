package xyz.vergoclient.assets;

import xyz.vergoclient.Vergo;
import xyz.vergoclient.ui.hud.Hud;
import xyz.vergoclient.util.RenderUtils;

public enum Colors {
	
	COLOR(0xffcbb277),
	BLACK(0xff000000),
	CREAM(0xffFFFDD0),
	WHITE(0xffffffff),
	GRAY(0xff333333),
	RED(0xffff3d3d),
	GREEN(0xff43b581),
	BLUE(0xff3396FF),
	YELLOW(0xfffff12e),
	PINK(0xfff86f64),
	CLICK_GUI_OFF(0xff13182c),
	CLICK_GUI_ON(0xfff86f64),
	CLICK_GUI_CAT(0xffCC625A),
	CLICK_GUI_SETTING(0xff0c0f1c),
	ARRAY_LIST_MODULE_NAMES(0xff9f70d5),
	ALT_MANAGER_BACKGROUND(0xff333333),
	ALT_MANAGER_BUTTONS(0xff282828),
	ALT_MANAGER_PURPLE(0xff9f70d5),
	MAIN_MENU_BACKGROUND(0xff292929),
	MAIN_MENU_BUTTON_WINDOW(0xff404040),
	MAIN_MENU_TITLE_BACKGROUND_COLOR(0xff353535),
	START_GUI_BACKGROUND(0xff292929),
	START_GUI_PROGRESS_BAR_BACKGROUND(0xff505050),
	START_GUI_PROGRESS_BAR_PROGRESS(0xff3396FF),
	NEW_CLICK_GUI_CATEGORY(0xff434343),
	NEW_CLICK_GUI_PURPLE(0xff9f73ff),
	NEW_CLICK_GUI_GREY(0xff5d5d5d);
	
	private int color;
	private int getColorNoRainbowOverride() {
		return color;
	}
	
	private Colors(int color) {
		this.color = color;
	}
	
}
