package knexer.betterPaintings.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import knexer.ItemStackWrapper;
import knexer.betterPaintings.ItemBetterPainting;
import knexer.betterPaintings.PaintingMetadataUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "knexer-BetterPaintings", name = "BetterPaintings", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModBetterPaintings {

	public static final ItemBetterPainting paintingItem = new ItemBetterPainting(9001);
	public static final int MAX_WIDTH = 4;
	public static final int MAX_HEIGHT = 4;
	
	static
	{

	}
	
	// The instance of your mod that Forge uses.
	@Instance("knexer-BetterPaintings")
	public static ModBetterPaintings instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "knexer.betterPaintings.client.ClientProxy", serverSide = "knexer.betterPaintings.common.CommonProxy")
	public static CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
	}

	@Init
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		LanguageRegistry.addName(paintingItem, "Fucking Awesome Painting Item");
		GameRegistry.addShapelessRecipe(painting(1, 1, 4), new ItemStack(Item.painting), new ItemStack(Item.slimeBall));
		
		addPaintingCompositionRecipes(painting(1, 1, 1), MAX_WIDTH, MAX_HEIGHT);
		
		for (int width = 1; width <= MAX_WIDTH; width++) {
			for (int height = 1; height <= MAX_HEIGHT; height++) {
				LanguageRegistry.addName(painting(width, height), "Painting " + PaintingMetadataUtil.getNameSuffix(width, height));
				GameRegistry.addShapelessRecipe(painting(1, 1, width * height), painting(width, height));
			}
		}
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
	
	/**
	 * Generates and adds every recipe for paintings larger than 1x1.
	 * 
	 * @param atomicPainting
	 * The 1x1 painting item.
	 * @param maxX
	 * The largest possible painting width.
	 * @param maxY
	 * The largest possible painting height.
	 */
	private void addPaintingCompositionRecipes(ItemStack atomicPainting, int maxX, int maxY)
	{
		ItemStackWrapper wrappedAtomicPainting = new ItemStackWrapper(atomicPainting);
		//The list of all paintings.  Not updated during the inner loop.
		List<ItemStackWrapper> allPaintings = new ArrayList<ItemStackWrapper>();
		allPaintings.add(wrappedAtomicPainting);
		
		//The paintings that have not yet been a 'left' painting.  Note that left- and right-ness has
		//nothing whatsoever to do with geometry.
		Queue<ItemStackWrapper> leftPaintings = new LinkedList<ItemStackWrapper>();
		leftPaintings.offer(wrappedAtomicPainting);
		
		//this queue will grow over time; this for loop will hit every painting exactly once.
		while(!leftPaintings.isEmpty())
		{
			ItemStackWrapper leftPainting = leftPaintings.poll();
			
			//compute 'left' painting's dimensions
			int leftDamage = leftPainting.getWrappedItemStack().getItemDamage();
			int leftX = PaintingMetadataUtil.getWidth(leftDamage);
			int leftY = PaintingMetadataUtil.getHeight(leftDamage);
			
			//the paintings newly generated this time around.
			List<ItemStackWrapper> newPaintings = new ArrayList<ItemStackWrapper>();
			for(ItemStackWrapper rightPainting : allPaintings)
			{
				//compute 'right' painting's dimensions
				int rightDamage = rightPainting.getWrappedItemStack().getItemDamage();
				int rightX = PaintingMetadataUtil.getWidth(rightDamage);
				int rightY = PaintingMetadataUtil.getHeight(rightDamage);
				
				//if the paintings are compatible on the vertical axis
				if(leftX == rightX && leftY + rightY <= maxY)
				{
					//add the recipes to the game, return the products (there might be new paintings)
					newPaintings.add(addVerticalRecipes(leftPainting, rightPainting, leftX, rightX, leftY, rightY));
				}
				
				//if the paintings are compatible on the horizontal axis
				if(leftY == rightY && leftX + rightX <= maxX)
				{
					//add the recipes to the game, return the products (there might be new paintings)
					newPaintings.add(addHorizontalRecipes(leftPainting, rightPainting, leftX, rightX, leftY, rightY));
				}
			}
			
			//figure out which paintings are actually new
			newPaintings.removeAll(allPaintings);
			
			//add those to the list of all paintings and to the queue of paintings to inspect
			allPaintings.addAll(newPaintings);
			leftPaintings.addAll(newPaintings);
		}
	}
	
	/**
	 * Adds both recipes that combine the given paintings in the y direction.
	 * The x dimensions of the paintings are assumed to be equal.
	 * @param leftPainting
	 * @param rightPainting
	 * @param leftX
	 * @param rightX
	 * @param leftY
	 * @param rightY
	 * @return
	 */
	private ItemStackWrapper addVerticalRecipes(ItemStackWrapper leftPainting,
			ItemStackWrapper rightPainting, int leftX, int rightX, int leftY,
			int rightY) {
		//The painting that will be produced by both of these recipes
		ItemStack product = painting(leftX, leftY + rightY);
		
		//left on the top first
		GameRegistry.addRecipe(product, "#", "!", '#', leftPainting.getWrappedItemStack(), '!', rightPainting.getWrappedItemStack());

		//then left on the bottom!
		GameRegistry.addRecipe(product, "!", "#", '#', leftPainting.getWrappedItemStack(), '!', rightPainting.getWrappedItemStack());
		
		return new ItemStackWrapper(product);
	}

	/**
	 * Adds both recipes that combine the given paintings in the x direction.
	 * The y dimensions of the paintings are assumed to be equal.
	 * @param leftPainting
	 * @param rightPainting
	 * @param leftX
	 * @param rightX
	 * @param leftY
	 * @param rightY
	 * @return
	 */
	private ItemStackWrapper addHorizontalRecipes(
			ItemStackWrapper leftPainting, ItemStackWrapper rightPainting,
			int leftX, int rightX, int leftY, int rightY) {
		//The painting that will be produced by both of these recipes
		ItemStack product = painting(leftX + rightX, leftY);
		
		//left on the left first
		GameRegistry.addRecipe(product, "#!", '#', leftPainting.getWrappedItemStack(), '!', rightPainting.getWrappedItemStack());

		//then left on the right!
		GameRegistry.addRecipe(product, "!#", '#', leftPainting.getWrappedItemStack(), '!', rightPainting.getWrappedItemStack());
		
		return new ItemStackWrapper(product);
	}

	/**
	 * Get a stack of <code>width</code>-by-<code>height</code> better paintings
	 * @param width
	 * @param height
	 * @param size The number of paintings in the stack
	 * @return
	 */
	private ItemStack painting(int width, int height, int size) {
		return new ItemStack(paintingItem, size, PaintingMetadataUtil.getDamageForSize(width, height));
	}
	
	/**
	 * Get a stack of <code>width</code>-by-<code>height</code> better paintings
	 * @param width
	 * @param height
	 * @return
	 */
	private ItemStack painting(int width, int height) {
		return painting(width, height, 1);
	}
}
