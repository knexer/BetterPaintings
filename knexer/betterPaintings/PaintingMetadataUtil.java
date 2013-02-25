package knexer.betterPaintings;

public class PaintingMetadataUtil {
	
	public static int getDamageForSize(int width, int height) {
		return (width - 1) + ((height - 1) * 4);
	}
	
	public static int getWidth(int damage) {
		return (damage % 4) + 1;
	}
	
	public static int getHeight(int damage) {
		return (damage / 4) + 1;
	}
	
	public static String getNameSuffix(int width, int height) {
		return "(Width: " + width + ", Height: " + height + ")";
	}
	
	public static String getNameSuffix(int damage) {
		return getNameSuffix(getWidth(damage), getHeight(damage));
	}
}
