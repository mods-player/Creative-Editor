package creativeeditor.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public class PlayerHead {

	private ItemStack skull;

	public PlayerHead(ItemStack is) {
		if (is.getItem() == Items.PLAYER_HEAD) {
			this.skull = is;
		}

	}

	public void setTexture(String texture) {
		//TODO null check
		if (!skull.getTag().contains("Properties")) {
			skull.getTag().put("Properties", new CompoundNBT());
		}
		skull.getTag().putString("Properties", texture);
		//System.out.println(skull.getTag().toString());

	}

	public ItemStack getLatest() {
		return skull;
	}

	public void setUUID(String uuid) {

	}

	public void setName(String name) {

	}
}