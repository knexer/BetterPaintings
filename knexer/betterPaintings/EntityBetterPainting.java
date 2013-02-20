package knexer.betterPaintings;

import knexer.betterPaintings.common.ModBetterPaintings;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumArt;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityBetterPainting extends EntityPainting {

	public EntityBetterPainting(World world) {
		super(world);
	}

	public EntityBetterPainting(World par1World, int par2, int par3, int par4, int par5) {
		super(par1World, par2, par3, par4, par5);
		// TODO
		art = EnumArt.Aztec;
	}

	@SideOnly(Side.CLIENT)
	public EntityBetterPainting(World par1World, int par2, int par3, int par4,
			int par5, String par6Str) {
		super(par1World, par2, par3, par4, par5, par6Str);
	}

	@Override
	public void dropItemStack()
	{
		this.entityDropItem(new ItemStack(ModBetterPaintings.paintingItem), 0.0F);
	}
}
