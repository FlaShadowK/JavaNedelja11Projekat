
public class RectangleCollider implements Collidable {

	private int width;
	private int height;

	public RectangleCollider(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	@Override
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width <= 0) {
			throw new IllegalArgumentException("Sirina mora biti veca od 0: " + width);
		}
		this.width = width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height <= 0) {
			throw new IllegalArgumentException("Visina mora biti veca od 0: " + height);
		}
		this.height = height;
	}

}