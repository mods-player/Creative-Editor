package creativeeditor.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import creativeeditor.data.DataItem;
import creativeeditor.util.ColorUtils.Color;
import creativeeditor.util.ItemRendererUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class ItemInspectorScreen extends ParentScreen {
    private DataItem item;
    private boolean isScrolling;
    private float rotX = 0f, rotY = 0f;
    private float itemScale = 10.0f;


    public ItemInspectorScreen(Screen lastScreen, DataItem item) {
        super( new TranslationTextComponent( "gui.iteminspector" ), lastScreen );
        this.item = item;
        this.isScrolling = false;
    }


    @Override
    public boolean mouseDragged( double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_ ) {
        if (p_mouseDragged_5_ != 0) {
            this.isScrolling = false;
            return false;
        }
        else if (!this.isScrolling) {
            this.isScrolling = true;
        }
        else {
            rotX += p_mouseDragged_6_;
            rotY += p_mouseDragged_8_;
            System.out.println(p_mouseDragged_6_);
        }
        return true;
    }


    @Override
    public void overlayRender( int mouseX, int mouseY, float p3, Color color ) {
        super.overlayRender( mouseX, mouseY, p3, color );
        ItemRendererUtils itemRenderUtils = new ItemRendererUtils( this.itemRenderer );

        ItemStack stack = item.getItemStack();
        RenderSystem.pushMatrix();
        RenderSystem.scalef( itemScale, itemScale, 1f );
        int x = (int) (width / (2 * itemScale) - 8);
        int y = (int) (30 / itemScale + height / (2 * itemScale) - 8);
        itemRenderUtils.renderItemIntoGUI( stack, x, y, rotX, rotY );
        RenderSystem.popMatrix();
    }
}
