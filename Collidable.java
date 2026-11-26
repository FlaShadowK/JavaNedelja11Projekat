
public interface Collidable {

	int getWidth();

	int getHeight();

	default boolean intersects(int x, int y, Collidable other, int otherX, int otherY) {
		return x < otherX + other.getWidth()
				&& x + getWidth() > otherX
				&& y < otherY + other.getHeight()
				&& y + getHeight() > otherY;
	}

}