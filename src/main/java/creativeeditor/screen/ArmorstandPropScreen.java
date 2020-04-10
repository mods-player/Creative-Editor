package creativeeditor.screen;

import creativeeditor.data.DataItem;
import creativeeditor.util.ColorUtils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class ArmorstandPropScreen extends ParentItemScreen {

	
	
	
	public ArmorstandPropScreen(Screen lastScreen, DataItem item) {
		super( new TranslationTextComponent( "gui.armorstandeditor.properties" ), lastScreen, item );
        this.renderItem = false;
    }
	
	
	@Override
	public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
		super.init();
	}
	
	@Override
	public void overlayRender(int mouseX, int mouseY, float p3, Color color) {
		
		super.overlayRender(mouseX, mouseY, p3, color);
	}
	
	@Override
	public void mainRender(int mouseX, int mouseY, float p3, Color color) {
		
		super.mainRender(mouseX, mouseY, p3, color);
	}

	
	@Override
	public void backRender(int mouseX, int mouseY, float p3, Color color) {

		super.backRender(mouseX, mouseY, p3, color);
	}
	
	
	
}