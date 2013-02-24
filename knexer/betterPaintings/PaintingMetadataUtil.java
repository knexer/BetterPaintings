package knexer.betterPaintings;

public class PaintingMetadataUtil {
	
	public static int getDamageForSize(int width, int height) {
		return width + (height * 4);
	}
	
	public static int getWidth(int damage) {
		return damage % 4;
	}
	
	public static int getHeight(int damage) {
		return damage / 4;
	}
	
	public static String getNameSuffix(int damage) {
		return "(" + PaintingMetadataUtil.getWidth(damage) + "x" + PaintingMetadataUtil.getHeight(damage) + ")";
	}
}
