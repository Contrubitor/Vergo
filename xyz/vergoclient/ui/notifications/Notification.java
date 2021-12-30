package xyz.vergoclient.ui.notifications;

import xyz.vergoclient.Vergo;
import xyz.vergoclient.util.Animation;
import xyz.vergoclient.util.AnimationUtils;

public class Notification {
    private final NotificationType type;
    public float y;
    public boolean vanishing;
    Timer timer = new Timer();
    private float width, height, roundness, time;
    private String title, description;
    private boolean heightAdjusted;
    private Animation animation;
    private AnimationUtils.Direction direction;

    public Notification(NotificationType type, String title, String description) {
        this.title = title;
        this.description = description;
        width = 145;
        height = 45;
        roundness = 4;
        time = (long) (timer.getTime() * 1000);
        timer = new Timer();
        this.type = type;
    }

    public static void post(NotificationType type, String title, String description) {
        Vergo.notificationManager.add(new Notification(type, title, description));
    }

    public void post() {
        Vergo.notificationManager.add(this);
    }

    public boolean isHeightAdjusted() {
        return heightAdjusted;
    }

    public void setHeightAdjusted(boolean bool) {
        heightAdjusted = bool;
    }

    public NotificationType getType() {
        return type;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getRoundness() {
        return roundness;
    }

    public void setRoundness(float roundness) {
        this.roundness = roundness;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public
    AnimationUtils.Direction getDirection() {
        return direction;
    }

    public void setDirection(AnimationUtils.Direction direction) {
        this.direction = direction;
    }

    public boolean animationInProgress() {
        return animation != null && !animation.isDone();
    }

}