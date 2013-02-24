package knexer.betterPaintings;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import knexer.betterPaintings.common.ModBetterPaintings;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumArt;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityBetterPainting extends EntityPainting {

	private int width;
	private int height;
	
	public EntityBetterPainting(World world) {
		super(world);
	}

	public EntityBetterPainting(World par1World, int xPos, int yPos, int zPos, int par5, int width, int height) {
		super(par1World, xPos, yPos, zPos, par5);
		this.width = width;
		this.height = height;
		
		EnumArt[] all = EnumArt.values();
		List<EnumArt> correctSize = new LinkedList<EnumArt>();
		
		for (EnumArt a : all) {
			if (a.sizeX / 16 == width && a.sizeY / 16 == height) {
				correctSize.add(a);
			}
		}
		
		if (correctSize.size() == 0) {
			//TODO: nothing of this size - what do
			art = EnumArt.Aztec;
		} else {
			art = correctSize.get(new Random().nextInt(correctSize.size()));
		}
		
	}

	@SideOnly(Side.CLIENT)
	public EntityBetterPainting(World par1World, int par2, int par3, int par4,
			int par5, String par6Str) {
		super(par1World, par2, par3, par4, par5, par6Str);
	}

	@Override
	public void dropItemStack()
	{
		this.entityDropItem(new ItemStack(ModBetterPaintings.paintingItem, 1, PaintingMetadataUtil.getDamageForSize(width, height)), 0.0F);
	}
}
