
public class Enemy extends GameObject implements Attacker {

	private String type;
	private int damage;
	private int health;

	public Enemy() {
		super();
		this.type = "default";
		this.damage = 0;
		this.health = 1;
	}

	public Enemy(String type, int x, int y, int width, int height, int damage) {
		this(type, x, y, width, height, damage, 1);
	}

	public Enemy(String type, int x, int y, int width, int height, int damage, int health) {
		super(x, y, width, height);
		setType(type);
		setDamage(damage);
		setHealth(health);
	}

	public Enemy(String type, int x, int y, Collidable collider, int damage, int health) {
		super(x, y, collider);
		setType(type);
		setDamage(damage);
		setHealth(health);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type == null || type.trim().isEmpty()) {
			throw new IllegalArgumentException("Tip ne smije biti prazan.");
		} else {
			this.type = type;
		}
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		if (damage < 0 || damage > 100) {
			throw new IllegalArgumentException("Damage mora biti u opsegu od 1 do 100: " + damage);
		}
		this.damage = damage;
	}

	public int getAttackDamage() {
		return damage;
	}

	public String getDamageText() {
		return String.valueOf(damage);
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if (health < 0 || health > 100) {
			throw new IllegalArgumentException("Health mora biti u opsegu [0, 100]: " + health);
		}
		this.health = health;
	}

	@Override
	public String getDisplayName() {
		return type;
	}

	@Override
	public boolean canAttack(Player target) {
		return health > 0 && intersects(target);
	}

	@Override
	public boolean attack(Player target) {
		if (!canAttack(target)) {
			return false;
		}
		int newHealth = Math.max(0, target.getHealth() - getAttackDamage());
		target.setHealth(newHealth);
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + type + "] @ (" + getX() + "," + getY() + ") DMG="
				+ getDamageText() + " HP=" + health;
	}

}