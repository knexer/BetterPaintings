package knexer;

import net.minecraft.item.ItemStack;

public class ItemStackWrapper {
	private ItemStack wrapped;
	
	public ItemStackWrapper(ItemStack toWrap)
	{
		wrapped = toWrap;
	}
	
	public ItemStack getWrappedItemStack()
	{
		return wrapped;
	}
	
	/**
	 * Checks this item for equality with other.
	 * 
	 * Compares itemID, stack size, and item damage.
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other == null) return false;
		if(!(other instanceof ItemStackWrapper)) return false;
		ItemStack o = ((ItemStackWrapper) other).getWrappedItemStack();
		
		if(wrapped.itemID != o.itemID) return false;
		if(wrapped.stackSize != o.stackSize) return false;
		if(wrapped.getItemDamage() != o.getItemDamage()) return false;
		
		return true;
	}
}
