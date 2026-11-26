
public class Player extends GameObject {
	
	private String name;
	private int health;
	
	public Player() {
		super();
		this.name = "Default";
		this.health = 1;
	}
	
	public Player(String name, int x, int y, int width, int height, int health) {
		super(x, y, width, height);
		setName(name);
		setHealth(health);
	}

	public Player(String name, int x, int y, Collidable collider, int health) {
		super(x, y, collider);
		setName(name);
		setHealth(health);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Ime ne smije biti prazno.");
		}else if(!Character.isLetter(name.trim().charAt(0))) {
			throw new IllegalArgumentException("Ime mora poceti slovom: " + name);
		}else {
			this.name = normalizeName(name);
		}
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if(health < 0 || health > 100) {
			throw new IllegalArgumentException("Health mora biti u opsegu od 0 do 100: " + health);
		}
		this.health = health;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + "] @  (" + getX() + ", " + getY() + ") " + getWidth() + " X " + getHeight()
				+ ", health=" + health + "]";
	}
	
		private static String normalizeName(String raw) {
			String[] words = raw.trim().split("\\s+");
			StringBuilder sb = new StringBuilder();
			for (String w : words) {
				if (sb.length() > 0) {
					sb.append(" ");
				}
				sb.append(Character.toUpperCase(w.charAt(0)));
				sb.append(w.substring(1).toLowerCase());
			}
			return sb.toString();
		}
	
	
	
	
	
	
	

}
