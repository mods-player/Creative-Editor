package creativeeditor.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import creativeeditor.config.Config;
import creativeeditor.data.DataItem;
import creativeeditor.data.NumberRangeInt;
import creativeeditor.screen.widgets.*;
import creativeeditor.screen.widgets.base.AdvancedWidgets;
import creativeeditor.screen.widgets.base.ItemWidgets;
import creativeeditor.styles.StyleManager;
import creativeeditor.util.ColorUtils.Color;
import creativeeditor.util.ItemRendererUtils;
import creativeeditor.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class MainScreen extends ParentItemScreen {

    private final ArrayList<Widget> toolsWidgets = new ArrayList<>();
    private final ArrayList<Widget> displayWidgets = new ArrayList<>();
    private final ArrayList<Widget> editWidgets = new ArrayList<>();
    private final ArrayList<Widget> advancedWidgets = new ArrayList<>();
    private StyledTextButton nbtButton, tooltipButton, toolsButton, displayButton, editButton, advancedButton, loreButton;
    private NumberField countField, damageField;
    private StyledDataTextField idField;
    private SliderTag countSlider, damageSlider;
    private StyledToggle unbreakable;
    // Tools
    private StyledTextButton styleButton;

    // Lore
    private StyledDataTextField nameField;
    private StyledTextButton clearButton;


    public MainScreen(Screen lastScreen, DataItem editing) {
        super(new TranslationTextComponent("gui.main"), lastScreen, editing);
    }


    @Override
    protected void init() {
        renderColorHelper = true;
        super.init();
        //if (item.getItem().getItem() == Items.AIR)
        //    item.getItem().setItem( Items.STICK );
        if (item.getCount().get() < 1) {
            item.getCount().set(1);
        }



        assert minecraft != null;
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
        String nbtLocal = I18n.get("gui.main.nbt");
        String tooltipLocal = I18n.get("gui.main.tooltip");
        String toolsLocal = I18n.get("gui.main.tools");
        String displayLocal = I18n.get("gui.main.display");
        String editLocal = I18n.get("gui.main.data");
        String advancedLocal = I18n.get("gui.main.other");

        int nbtWidth = font.width(nbtLocal);
        int tooltipWidth = font.width(tooltipLocal);
        int toolsWidth = font.width(toolsLocal);
        int displayWidth = font.width(displayLocal);
        int editWidth = font.width(editLocal);
        int advancedWidth = font.width(advancedLocal);

        int nbtX = 21 + nbtWidth / 2;
        int toolsX = width / 3 - 16 - toolsWidth / 2;
        int tooltipX = (nbtX + toolsX) / 2;

        int advancedX = width - 21 - advancedWidth / 2;
        int loreX = 2 * width / 3 + 17 + displayWidth / 2;
        int editX = ((loreX + displayWidth / 2) + (advancedX - advancedWidth / 2)) / 2;

        minecraft.keyboardHandler.setSendRepeatsToGui(true);

        toolsWidgets.clear();
        displayWidgets.clear();
        editWidgets.clear();
        advancedWidgets.clear();

        nbtButton = addButton(new StyledTextButton(nbtX, 35, nbtWidth, nbtLocal, b -> {
            setLeftTab(0, true);
        }));

        tooltipButton = addButton(new StyledTextButton(tooltipX, 35, tooltipWidth, tooltipLocal, b -> {
            setLeftTab(1, true);
        }));

        toolsButton = addButton(new StyledTextButton(toolsX, 35, toolsWidth, toolsLocal, b -> {
            setLeftTab(2, true);

            for (Widget tool : toolsWidgets) {
                tool.visible = true;
            }
        }));

        displayButton = addButton(new StyledTextButton(loreX, 35, displayWidth, displayLocal, b -> {
            setRightTab(0, true);

            for (Widget lore : displayWidgets) {
                lore.visible = true;
            }
        }));

        editButton = addButton(new StyledTextButton(editX, 35, editWidth, editLocal, b -> {
            setRightTab(1, true);

            for (Widget lore : displayWidgets) {
                lore.visible = false;
            }
        }));

        advancedButton = addButton(new StyledTextButton(advancedX, 35, advancedWidth, advancedLocal, b -> {
            setRightTab(2, true);

            for (Widget lore : displayWidgets) {
                lore.visible = false;
            }
        }));

        // Widgets
        loadWidgets(new ItemWidgets(), editWidgets);
        loadWidgets(new AdvancedWidgets(), advancedWidgets);


        // Tools
        String styleLocal = I18n.get("gui.main.style");
        styleButton = addButton(new StyledTextButton(width / 6, 55, font.width(styleLocal), styleLocal, b -> StyleManager.setNext()));
        toolsWidgets.add(styleButton);

        String headsLocal = I18n.get("gui.headcollection");
        StyledTextButton headsButton = addButton(new StyledTextButton(width / 6, 75, font.width(headsLocal), headsLocal, b -> minecraft.setScreen(new HeadCollectionScreen(this))));
        toolsWidgets.add(headsButton);

        String spawnerLocal = I18n.get("gui.itemspawner");
        StyledTextButton spawnerButton = addButton(new StyledTextButton(width / 6, 95, font.width(spawnerLocal), spawnerLocal, b -> minecraft.setScreen(new ItemSpawnerScreen(this))));
        toolsWidgets.add(spawnerButton);

        String tagExplorer = I18n.get("gui.tagexplorer");
        StyledTextButton tagExploreButton = addButton(new StyledTextButton(width / 6, 115, font.width(tagExplorer), tagExplorer, b -> minecraft.setScreen(new TagExplorerScreen(this, item))));
        toolsWidgets.add(tagExploreButton);

        // Lore
        String resetLore = I18n.get("gui.main.resetlore");
        int resetWidth = font.width(resetLore);
        int resetX = width - 22 - resetWidth / 2;
        int nameX = 2 * width / 3 + 16;

        String lore = I18n.get("gui.loreeditor");
        int loreWidth = font.width(lore);
        loreButton = addButton(new StyledTextButton(width * 2 / 3 + 16 + (width / 3 / 2 / 2), 95, loreWidth, lore, t -> minecraft.setScreen(new LoreEditorScreen(this, item))));
        nameField = new StyledDataTextField(font, nameX, 55, resetX - nameX - resetWidth / 2 - 7, 20, item.getDisplayNameTag());
        renderWidgets.add(nameField);
        displayWidgets.add(nameField);
        displayWidgets.add(loreButton);
        children.add(nameField);
        clearButton = addButton(new StyledTextButton(resetX, 67, resetWidth, resetLore, b -> {
            nameField.setText(item.getDisplayNameTag().getDefault().getString());
            nameField.setCursorPos(0);
            nameField.setSelectionPos(0);
        }));
        displayWidgets.add(clearButton);

        // General Item
        String id = I18n.get("gui.main.id");
        int idWidth = font.width(id);
        String count = I18n.get("gui.main.count");
        int countWidth = font.width(count);
        String damage = I18n.get("gui.main.damage");
        int damageWidth = font.width(damage);

        int x = width / 3 + 16 + Math.max(idWidth, Math.max(countWidth, damageWidth));

        idField = new StyledDataTextField(font, x, 81, (width - width / 3 - 8) - x, 16, item.getItem());
        idField.setIsResourceLocationField(true);
        renderWidgets.add(idField);

        int countX = x;
        this.countField = new NumberField(font, countX, 101, 16, item.getCount());
        renderWidgets.add(countField);
        countX += this.countField.getWidth() + 8;
        this.countSlider = addButton(new SliderTag(countX, 101, (width - width / 3 - 8) - countX, 16, item.getCount()));

        int dmgX = x;
        this.damageField = new NumberField(font, dmgX, 121, 16, item.getTag().getDamage());
        dmgX += this.damageField.getWidth() + 8;
        renderWidgets.add(damageField);
        this.damageSlider = addButton(new SliderTag(dmgX, 121, (width - width / 3 - 8) - dmgX, 16, item.getTag().getDamage()));

        this.unbreakable = addButton(new StyledToggle(width / 2 - 40, 141, 80, 16, I18n.get("item.tag.unbreakable.true"), I18n.get("item.tag.unbreakable.false"), item.getTag().getUnbreakable()));

        setLeftTab(Config.MAIN_LEFT_TAB.get(), false);
        setRightTab(Config.MAIN_RIGHT_TAB.get(), false);
    }

    private void loadWidgets(Iterable<ClassSpecificWidget> widgets, ArrayList<Widget> widgetList) {
        int y = 55;
        for (ClassSpecificWidget w : widgets) {
            WidgetInfo info = new WidgetInfo(width - width / 6, y, font.width(w.text), 10, w.text, null, this);
            Widget widget = w.get(info, item);
            if (widget != null) {
                addButton(widget);
                widgetList.add(widget);
                y += 20;
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }

    public void setLeftTab(int i, boolean updateConfig) {
        if (updateConfig && i >= 0 && i <= 3)
            Config.MAIN_LEFT_TAB.set(i);

        switch (i) {
            case 0:
                nbtButton.active = false;
                tooltipButton.active = true;
                toolsButton.active = true;

                for (Widget tool : toolsWidgets) {
                    tool.visible = false;
                }
                break;
            case 1:
                nbtButton.active = true;
                tooltipButton.active = false;
                toolsButton.active = true;

                for (Widget tool : toolsWidgets) {
                    tool.visible = false;
                }
                break;
            case 2:
                nbtButton.active = true;
                tooltipButton.active = true;
                toolsButton.active = false;

                for (Widget tool : toolsWidgets) {
                    tool.visible = true;
                }
                break;
        }
    }


    public void setRightTab(int i, boolean updateConfig) {
        if (updateConfig && i >= 0 && i <= 3)
            Config.MAIN_RIGHT_TAB.set(i);

        switch (Config.MAIN_RIGHT_TAB.get()) {
            case 0:
                displayButton.active = false;
                editButton.active = true;
                advancedButton.active = true;

                for (Widget lore : displayWidgets) {
                    lore.visible = true;
                }
                for (Widget edit : editWidgets) {
                    edit.visible = false;
                }
                for (Widget advanced : advancedWidgets) {
                    advanced.visible = false;
                }
                break;
            case 1:
                displayButton.active = true;
                editButton.active = false;
                advancedButton.active = true;

                for (Widget lore : displayWidgets) {
                    lore.visible = false;
                }
                for (Widget edit : editWidgets) {
                    edit.visible = true;
                }
                for (Widget advanced : advancedWidgets) {
                    advanced.visible = false;
                }
                break;
            case 2:
                displayButton.active = true;
                editButton.active = true;
                advancedButton.active = false;

                for (Widget lore : displayWidgets) {
                    lore.visible = false;
                }
                for (Widget edit : editWidgets) {
                    edit.visible = false;
                }
                for (Widget advanced : advancedWidgets) {
                    advanced.visible = true;
                }
                break;
        }
    }


    @Override
    public void resize(Minecraft minecraft, int par2, int par3) {
        this.init(minecraft, par2, par3);
    }


    @Override
    public void removed() {
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }


    @Override
    public boolean keyPressed(int key1, int key2, int key3) {
        if (key3 == 2) {
            if (key2 == 47) {
                String clip = minecraft.keyboardHandler.getClipboard();
                if (!clip.isEmpty()) {
                    try {
                        CompoundNBT nbt = JsonToNBT.parseTag(clip);
                        if (nbt != null) {
                            item = new DataItem(nbt);
                            NumberRangeInt c = item.getCount();
                            if (c.get() > c.getMax()) c.set(c.getMax());
                            init();
                        }
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else if (key2 == 46) {
                if (!item.getNBT().isEmpty()) {
                    minecraft.keyboardHandler.setClipboard(item.getNBT().getAsString());
                }
            }
        }

        return super.keyPressed(key1, key2, key3);
    }


    @Override
    public void backRender(MatrixStack matrix, int mouseX, int mouseY, float partialTicks, Color color) {
        super.backRender(matrix, mouseX, mouseY, partialTicks, color);

        // First vertical line
        fill(matrix, width / 3, 20, width / 3 + 1, height - 20, color.getInt());
        // Second vertical line
        fill(matrix, width * 2 / 3, 20, width * 2 / 3 + 1, height - 20, color.getInt());
        // Left horizontal line
        fill(matrix, 20, 40, width / 3 - 15, 41, color.getInt());
        // Right horizontal line
        fill(matrix, width * 2 / 3 + 16, 40, width - 20, 41, color.getInt());

    }


    @Override
    public void mainRender(MatrixStack matrix, int mouseX, int mouseY, float partialTicks, Color color) {
        super.mainRender(matrix, mouseX, mouseY, partialTicks, color);

        // Item Name
        String itemCount = item.getCount().get() > 1 ? item.getCount().get() + "x " : "";
        String displayName = item.getItemStack().getDisplayName().getString();
        String itemOverview = itemCount + addChar(displayName, "\u00a7r", displayName.length() - 1);
        String overviewTrimmed = font.plainSubstrByWidth(itemOverview, width / 3 - 15);
        drawCenteredString(matrix, font, overviewTrimmed.equals(itemOverview) ? overviewTrimmed : overviewTrimmed + "...", width / 2, 27, color.getInt());


        int x = width / 3 + 10;
        boolean dmgEnabled = getItem().getTag().getDamage().getMax() > 0;
        String count = I18n.get("gui.main.count");
        int countWidth = font.width(count);
        String dmgString = I18n.get("gui.main.damage");
        int damageWidth = font.width(dmgString);
        int idoffset = Math.max(countWidth, damageWidth);

        if (dmgEnabled) {
            damageField.visible = true;
            damageSlider.visible = true;
            drawString(matrix, font, dmgString, x, 125, color.getInt());
            unbreakable.y = 141;
        } else {
            damageField.visible = false;
            damageSlider.visible = false;
            unbreakable.y = 121;
        }

        String id = I18n.get("gui.main.id");
        // int idWidth = font.width( id );
        drawString(matrix, font, id, x, 85, color.getInt());

        //drawString( font, item.getItem().getIDExcludingMC(), x + 6 + idoffset, 85, color.getInt() );

        drawString(matrix, font, count, x, 105, color.getInt());
    }

    private String addChar(String str, String ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }


    @Override
    public void overlayRender(MatrixStack matrix, int mouseX, int mouseY, float partialTicks, Color color) {
        super.overlayRender(matrix, mouseX, mouseY, partialTicks, color);

        RenderUtil.glScissorBox(6, 41, width / 3, height - 6);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        matrix.pushPose();
        if (Config.MAIN_LEFT_TAB.get() == 0) {
            // NBT
            assert minecraft != null;
            matrix.scale(0.75F, 0.75F, 0.75F);
            ItemRendererUtils.renderFormattedItemNBT(matrix, item, 5, 80, width / 3 - 1, height, -1, font );
        } else if (Config.MAIN_LEFT_TAB.get() == 1) {

            matrix.scale(0.9F, 0.9F, 0.9F);
            renderTooltip(matrix, item.getItemStack(), 3, 68);
//          GuiUtil.drawHoveringText(item.getItemStack(), matrix, getTooltipFromItem(item.getItemStack()), 0, 60, width / 3 - 1, height, -1, font);

        }
        matrix.popPose();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
