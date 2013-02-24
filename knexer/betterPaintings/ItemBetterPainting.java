package knexer.betterPaintings;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class ItemBetterPainting extends Item {

	public ItemBetterPainting(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setIconCoord(10, 1);
		this.setItemName("itemBetterPainting");
		this.setHasSubtypes(true);
	}

	public boolean onItemUse(ItemStack stack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
        if (par7 == 0)
        {
            return false;
        }
        else if (par7 == 1)
        {
            return false;
        }
        else
        {
            int var11 = Direction.vineGrowth[par7];
            int damage = stack.getItemDamage();
            int width = PaintingMetadataUtil.getWidth(damage);
            int height = PaintingMetadataUtil.getHeight(damage);
            EntityHanging var12 = new EntityBetterPainting(par3World, par4, par5, par6, var11, width, height);

            if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, stack))
            {
                return false;
            }
            else
            {
                if (var12 != null && var12.onValidSurface())
                {
                    if (!par3World.isRemote)
                    {
                        par3World.spawnEntityInWorld(var12);
                    }

                    --stack.stackSize;
                }

                return true;
            }
        }
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		int damage = itemstack.getItemDamage();
		return getItemName() + "." + PaintingMetadataUtil.getNameSuffix(damage);
	}
}
