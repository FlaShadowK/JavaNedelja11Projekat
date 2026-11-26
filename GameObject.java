
public abstract class GameObject {

	private int x;
	private int y;
	private Collidable collider;

	public GameObject() {
		this(0, 0, 1, 1);
	}

	public GameObject(int x, int y, int width, int height) {
		this(x, y, new RectangleCollider(width, height));
	}

	public GameObject(int x, int y, Collidable collider) {
		super();
		this.x = x;
		this.y = y;
		setCollider(collider);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Collidable getCollider() {
		return collider;
	}

	public void setCollider(Collidable collider) {
		if (collider == null) {
			throw new IllegalArgumentException("Kolajder ne smije biti null.");
		}
		this.collider = collider;
	}

	public int getWidth() {
		return collider.getWidth();
	}

	public int getHeight() {
		return collider.getHeight();
	}

	public abstract String getDisplayName();

	public boolean intersects(GameObject other) {
		return collider.intersects(x, y, other.collider, other.x, other.y);
	}

	@Override
	public String toString() {
		return "GameObject @ (" + x + "," + y + ") " + getWidth() + "x" + getHeight();
	}

	public static void main(String[] args) {
		GameObject a = new Enemy("Goblin", 0, 0, 10, 10, 5);
		GameObject b = new Enemy("Orc", 5, 5, 10, 10, 15);
		GameObject c = new Player("Luka", 20, 20, 5, 5, 100);
		c.setCollider(new SquareCollider(5));

		System.out.println(a);                 
		System.out.println(a.intersects(b));   
		System.out.println(a.intersects(c));   
	}

}