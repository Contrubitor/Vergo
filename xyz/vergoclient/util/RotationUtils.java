package xyz.vergoclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
	
	public static float updateRotation(float current, float intended, float factor) {
		float var4 = MathHelper.wrapAngleTo180_float(intended - current);

		if (var4 > factor) {
			var4 = factor;
		}

		if (var4 < -factor) {
			var4 = -factor;
		}

		return current + var4;
	}
	
	public static float getRotationChange(float current, float intended) {
		float var4 = MathHelper.wrapAngleTo180_float(intended - current);
		
		return var4;
	}
	
	public static float getRotationPercent(float starting, float current, float intended) {
		float currentToIntended = MathHelper.wrapAngleTo180_float(intended - current);
		float startingToIntended = MathHelper.wrapAngleTo180_float(intended - starting);
		
		if (currentToIntended == 0 || startingToIntended == 0) {
			return 1.0f;
		}
		
		if (currentToIntended < 0) {
			currentToIntended *= -1;
		}
		
		if (startingToIntended < 0) {
			startingToIntended *= -1;
		}
		
		if (currentToIntended > startingToIntended) {
			float temp = startingToIntended;
			startingToIntended = currentToIntended;
			currentToIntended = temp;
		}
		
		return 1.0f - (currentToIntended / startingToIntended);
	}
	
	public static float updateRotationWithPercent(float current, float intended, float percent) {
		float factor = (intended - current) * percent;
		return current + factor;
	}
	
    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY -1.2;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }
    
    public static float[] getRotationFromPosition(double x1, double z1, double y1, double x2, double z2, double y2) {
        double xDiff = x2 - x1;
        double zDiff = z2 - z1;
        double yDiff = y2 - y1;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }
    
    public static float[] getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (ent.getEyeHeight() / 4.5F);
        return getRotationFromPosition(x, z, y);
    }
    
    public static Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
    
    public static float getBowVelocity() {
    	try {
            int i = Minecraft.getMinecraft().thePlayer.getItemInUse().getMaxItemUseDuration() - Minecraft.getMinecraft().thePlayer.itemInUseCount;
            float f = (float)i / 20.0F;
            return f * 2.0F;
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return 0;
    }
    
}
