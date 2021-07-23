package creativeeditor.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import creativeeditor.data.DataItem;
import creativeeditor.screen.widgets.StyledButton;
import creativeeditor.styles.StyleManager;
import creativeeditor.util.ColorUtils.Color;
import creativeeditor.util.GuiUtil;
import creativeeditor.util.ItemRendererUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

public class ParentItemScreen extends ParentScreen {
    protected DataItem item;

    // Back, reset, drop, save button (has essential buttons)
    protected boolean hasEssButtons = true;
    protected StyledButton backButton, resetButton, saveButton, dropButton;

    // render item
    protected boolean renderItem = true;
    protected float itemScale = 2.0f;
    protected float itemRotX = 0.0f;

    // render tooltip top left
    protected boolean renderToolTip = false;


    public ParentItemScreen(ITextComponent title, Screen lastScreen, DataItem editing) {
        super( title, lastScreen );
        this.item = editing;
    }


    @Override
    protected void init() {
        super.init();
        buttons.clear();
        children().clear();
        renderWidgets.clear();

        if (hasEssButtons) {
            int bwidth = 50;
            int posX = width / 2 - (bwidth / 2);
            int posY = height - 42;
            boolean hasLastscreen = lastScreen != null;
            String butCloseBack = hasLastscreen ? "gui.main.back" : "gui.main.close";

            backButton = addButton( new StyledButton( posX - bwidth - 1, posY, bwidth, 20, I18n.get( butCloseBack ), this::back ) );

            resetButton = addButton( new StyledButton( posX, (hasLastscreen ? posY : posY - 11), bwidth, 20, I18n.get( "gui.main.reset" ), this::reset ) );

            saveButton = hasLastscreen ? null : addButton( new StyledButton( posX, posY + 10, bwidth, 20, I18n.get( "gui.main.save" ), this::save ) );

            dropButton = addButton( new StyledButton( posX + bwidth + 1, posY, bwidth, 20, I18n.get( "gui.main.drop" ), this::drop ) );
        }
    }


    public void back( Widget w ) {
        minecraft.setScreen( lastScreen );
    }


    public void reset( Widget w ) {
        DataItem dItem = new DataItem( item.getItem().getItem(), 1, new CompoundNBT(), item.getSlot().get() );
        item = dItem;
        Screen last = lastScreen;
        while (last instanceof ParentItemScreen) {
            ParentItemScreen lastItemScreen = (ParentItemScreen) last;
            lastItemScreen.item = dItem;
            last = lastItemScreen.lastScreen;
        }
        init();
    }


    public void save( Widget w ) {
        if (item.getItem().getItem() != Items.AIR);
//            minecraft.gameMode.sendSlotPacket( item.getItemStack(), 36 + minecraft.player.inventory.selected );
    }


    public void drop( Widget w ) {
        if (item.getItem().getItem() != Items.AIR);
//            minecraft.gameMode.sendPacketDropItem( item.getItemStack() );
        // Shift for /give
    }


    public DataItem getItem() {
        return this.item;
    }


    public void setRenderItem( boolean shouldRender, float scale ) {
        this.renderItem = shouldRender;
        if (scale > 0)
            this.itemScale = scale;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (super.mouseClicked(mouseX, mouseY, mouseButton)){
            return true;
        }
        if (GuiUtil.isMouseIn( (int) mouseX, (int) mouseY, width / 2 - 17, 43, 36, 36 )) {
            minecraft.setScreen(new ItemInspectorScreen(this, item));
            return true;
        }
        return false;
    }

    @Override
    public void mainRender(MatrixStack matrix, int mouseX, int mouseY, float p3, Color color ) {
        super.mainRender( matrix, mouseX, mouseY, p3, color );
    }


    @Override
    public void overlayRender(MatrixStack matrix, int mouseX, int mouseY, float p3, Color color ) {
        super.overlayRender(matrix, mouseX, mouseY, p3, color );
        // Item (Tooltip must render last or colors will be messed up)
        if (renderItem) {
            ItemStack stack = item.getItemStack();

            Item ite = item.getItem().getItem();
            int x = width / 2;
            int y = 60;
            int xFrameStart = x - 19;
            int xFrameEnd = x + 19;
            int yFrameStart = 41;
            if (ite == Items.AIR) {
                drawCenteredString( matrix, font, ite.getName( stack ).getString(), x, y-3, color.getInt() );
            }
            else {
                RenderSystem.pushMatrix();
                RenderSystem.translatef( itemScale, itemScale, 1f );
                RenderSystem.scalef( itemScale, itemScale, 1f );
                drawItemStack( item.getItemStack(), (int) (xFrameStart / itemScale + 1), (int) (yFrameStart / itemScale + 1), itemRotX, 0, null );
                RenderSystem.popMatrix();
            }


            // TODO Item scale support
            if (GuiUtil.isMouseIn( mouseX, mouseY, width / 2 - 17, 43, 36, 36 )){
                itemRotX += 0.25;
                if (itemScale == 2f)
                    GuiUtil.drawFrame( matrix, xFrameStart, yFrameStart, xFrameEnd, 79, 1, StyleManager.getCurrentStyle().getFGColor(true, true) );

                renderTooltip(matrix, stack, mouseX, mouseY );
            }
            else{
                itemRotX = 0f;
                if (itemScale == 2f)
                    GuiUtil.drawFrame( matrix, xFrameStart, yFrameStart, xFrameEnd, 79, 1, color);
            }
        }
    }
}
