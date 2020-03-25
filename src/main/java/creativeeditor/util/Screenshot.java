package creativeeditor.util;

import creativeeditor.eventhandlers.GameOverlayHandler;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ScreenshotEvent;

public class Screenshot {

	boolean enableAnimation;
	int opacity = 254;
	int hold = 20;
	double speed = 0.99;
	public static ResourceLocation screenshotLocation = new ResourceLocation("creativeeditor", "screenshot");
	
	
	public Screenshot(ScreenshotEvent e) {
		GameOverlayHandler.hold = hold;
		GameOverlayHandler.opacity = opacity;
		GameOverlayHandler.speed = speed;
		GameOverlayHandler.firstRun = true;
		GameOverlayHandler.dyntex = new DynamicTexture(e.getImage());
		GameOverlayHandler.event = e;
	}

	public void animationEnabled(boolean bool) {
		this.enableAnimation = bool;
	}

	public void setHold(int hold) {
		this.hold = hold;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
