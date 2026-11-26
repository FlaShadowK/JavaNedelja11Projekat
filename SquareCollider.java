
public class SquareCollider implements Collidable {

	private int width;

	public SquareCollider(int width) {
		setWidth(width);
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
		return width;
	}

}