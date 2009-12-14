import de.enough.polish.ui.Color;
import de.enough.polish.ui.RgbFilter;

/**
 * ColorComponentScaler -- filters an image by multiplier its red, green and
 * blue color components by their given scale factors
 */
public class Image2CardTypeImage{

	/**
	 * rm = red multiplier gm = green multiplier bm = blue multiplier
	 */
	public Image2CardTypeImage(double rm, double gm, double bm) {
		canFilterIndexColorModel = true;
		redMultiplier = rm;
		greenMultiplier = gm;
		blueMultiplier = bm;
	}

	private int multColor(int colorComponent, double multiplier) {
		colorComponent = (int) (colorComponent * multiplier);
		if (colorComponent < 0)
			colorComponent = 0;
		else if (colorComponent > 255)
			colorComponent = 255;

		return colorComponent;
	}

	/**
	 * split the argb value into its color components, multiply each color
	 * component by its corresponding scaler factor and pack the components back
	 * into a single pixel
	 */
	public int filterRGB(int x, int y, int argb) {
		color = new Color(argb);
		newBlue = multColor(color.getBlue(), blueMultiplier);
		newGreen = multColor(color.getGreen(), greenMultiplier);
		newRed = multColor(color.getRed(), redMultiplier);
		newColor = new Color(newRed, newGreen, newBlue);
		return (newColor.getRGB());
	}