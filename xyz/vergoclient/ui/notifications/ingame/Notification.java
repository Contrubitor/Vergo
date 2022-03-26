package xyz.vergoclient.ui.notifications.ingame;

import xyz.vergoclient.ui.fonts.FontUtil;
import xyz.vergoclient.ui.fonts.JelloFontRenderer;
import xyz.vergoclient.util.TimerUtil;
import xyz.vergoclient.util.animations.Animation;

public class Notification {

    private final NotificationType notificationType;
    private final String title, description;
    private final float height = 28, time;
    public float notificationY;
    public JelloFontRenderer descriptionFont = FontUtil.comfortaaSmall;
    public JelloFontRenderer titleFont = FontUtil.comfortaaNormal;
    public final TimerUtil timerUtil;
    private Animation animation;

    public Notification(NotificationType type, String title, String description) {
        this.title = title;
        this.description = description;
        this.time = 1500; // 1.5 seconds
        timerUtil = new TimerUtil();
        this.notificationType = type;
    }

    public Notification(NotificationType type, String title, String description, float time) {
        this.title = title;
        this.description = description;
        this.time = (long) (time * 1000);
        timerUtil = new TimerUtil();
        this.notificationType = type;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public float getWidth() {
        return 17 + (float) Math.max(descriptionFont.getStringWidth(description), titleFont.getStringWidth(title));
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getHeight() {
        return height;
    }

    public float getMaxTime() {
        return time;
    }

    public void startAnimation(Animation animation) {
        this.animation = animation;
    }

    public void stopAnimation() {
        this.animation = null;
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isAnimating() {
        return animation != null && !animation.isDone();
    }

}