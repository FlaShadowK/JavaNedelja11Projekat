
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Game {

	private Player player;
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private List<String> eventLog = new ArrayList<>();

	public Game(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("Igrac ne smije biti null.");
		}
		this.player = player;
	}

	public boolean checkCollision(Player p, Enemy e) {
		return p.intersects(e);
	}

	public void decreaseHealth(Player p, Enemy e) {
		int oldHp = p.getHealth();
		int dmg = e.getAttackDamage();
		int newHp = Math.max(0, oldHp - dmg);
		p.setHealth(newHp);
		eventLog.add("HIT: " + p.getName() + " by " + e.getType() + " for " + dmg
				+ " -> HP " + oldHp + " -> " + newHp);
	}

	public void addEnemy(Enemy e) {
		if (e == null) {
			throw new IllegalArgumentException("Neprijatelj ne smije biti null.");
		}
		enemies.add(e);
		eventLog.add("ADD: " + e);
	}

	public List<Enemy> findByType(String query) {
		List<Enemy> result = new ArrayList<>();
		String q = query.trim().toLowerCase();
		for (Enemy e : enemies) {
			if (e.getType().toLowerCase().contains(q)) {
				result.add(e);
			}
		}
		return result;
	}

	public List<Enemy> collidingWithPlayer() {
		List<Enemy> result = new ArrayList<>();
		for (Enemy e : enemies) {
			if (checkCollision(player, e)) {
				result.add(e);
			}
		}
		return result;
	}

	public void resolveCollisions() {
		for (Enemy e : collidingWithPlayer()) {
			decreaseHealth(player, e);
		}
	}


	public static Enemy parseEnemy(String s) {
		String[] parts = s.split(",", -1);
		if (parts.length != 9) {
			throw new IllegalArgumentException("Neispravan format: " + s);
		}
		String type = parts[0].trim();
		String enemyClass = parts[1].trim();
		String shape = parts[6].trim();
		int damage, health, x, y, w, h;
		try {
			damage = Integer.parseInt(parts[2].trim());
			health = Integer.parseInt(parts[3].trim());
			x = Integer.parseInt(parts[4].trim());
			y = Integer.parseInt(parts[5].trim());
			w = Integer.parseInt(parts[7].trim());
			h = Integer.parseInt(parts[8].trim());
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Neispravan broj u liniji: " + s);
		}

		Collidable collider;
		if (shape.equalsIgnoreCase("square") && w == h) {
			collider = new SquareCollider(w);
		} else if (shape.equalsIgnoreCase("rectangle")) {
			collider = new RectangleCollider(w, h);
		} else {
			throw new IllegalArgumentException("Neispravan oblik ili dimenzije: " + s);
		}

		if (enemyClass.equalsIgnoreCase("melee")) {
			return new MeleeEnemy(type, x, y, collider, damage, health);
		}
		if (enemyClass.equalsIgnoreCase("boss")) {
			return new BossEnemy(type, x, y, collider, damage, health);
		}
		throw new IllegalArgumentException("Nepoznata klasa neprijatelja: " + s);
	}

	public static ArrayList<Enemy> loadEnemiesFromFile(String path) throws IOException {
		ArrayList<Enemy> result = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					result.add(parseEnemy(line));
				}
			}
		}
		return result;
	}

	public void saveToFile(String path) throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
			writer.println(getStatus());
			writer.println();
			writer.println(getCollisionsReport());
			writer.print(getLogReport());
		}
	}

	public String getStatus() {
		return "STATUS: Player[" + player.getName() + "] @ (" + player.getX() + "," + player.getY() + ") HP="
				+ player.getHealth();
	}

	public String getEnemiesReport() {
		StringBuilder sb = new StringBuilder("NEPRIJATELJI (" + enemies.size() + "):\n");
		for (Enemy e : enemies) {
			sb.append("  " + e + "\n");
		}
		return sb.toString();
	}

	public String getCollisionsReport() {
		List<Enemy> colliding = collidingWithPlayer();
		StringBuilder sb = new StringBuilder("U KOLIZIJI SA IGRAČEM (" + colliding.size() + "):\n");
		for (Enemy e : colliding) {
			sb.append("  " + e.getDisplayName() + "\n");
		}
		return sb.toString();
	}

	public String getLogReport() {
		StringBuilder sb = new StringBuilder("LOG:\n");
		for (String line : eventLog) {
			sb.append("  " + line + "\n");
		}
		return sb.toString();
	}

	public String getReport() {
		return getStatus() + "\n\n" + getEnemiesReport() + "\n" + getCollisionsReport() + "\n" + getLogReport();
	}

	public String getEndMessage() {
		if (player.getHealth() == 0) {
			return "Igrač " + player.getName() + " je poražen!";
		}
		return "Igrač " + player.getName() + " je preživio sa " + player.getHealth() + " HP!";
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public List<String> getEventLog() {
		return eventLog;
	}

	public static void main(String[] args) {
		Game game = new Game(new Player("Karim Dzankovic", 10, 5, 32, 32, 85));
		ArrayList <Enemy> neprijatelji = new ArrayList <Enemy>();

		neprijatelji.add(new Enemy("Goblin", 43, 77, 16, 16, 20));
		neprijatelji.add(new Enemy("Goblin", 22, 11, 3, 5, 19));
		neprijatelji.add(new Enemy("Goblin", 22, 54, 14, 29, 48));
		neprijatelji.add(new Enemy("Goblin", 19, 60, 75, 88, 21));

		game.addEnemy(new Enemy("Hobgoblin", 30, 5, 16, 16, 10));
		game.addEnemy(parseEnemy("Goblin,melee,20,60,12,5,square,16,16"));

		
		game.addEnemy(new MeleeEnemy(30, 5, 16, 16, 10, 50));    
		game.addEnemy(new BossEnemy(35, 20, 20, 20, 15, 100));   
		game.addEnemy(new MeleeEnemy(200, 200, 16, 16, 15, 30)); 

		
		game.addEnemy(parseEnemy("Skeleton,melee,5,40,40,30,square,10,10"));      
		game.addEnemy(parseEnemy("Orc,melee,25,80,100,100,square,16,16"));     

		try {
			for (Enemy e : loadEnemiesFromFile("enemies.txt")) {
				game.addEnemy(e);
			}
		} catch (IOException | IllegalArgumentException ex) {
			System.out.println("Greska pri ucitavanju: " + ex.getMessage());
		}

		
		System.out.println("=== Svi neprijatelji ===");
		for (Enemy e : game.getEnemies()) {
			System.out.println(e);
		}

		
		System.out.println("\n=== Neprijatelji koji sadrže \"gob\" ===");
		for (Enemy e : game.findByType("gob")) {
			System.out.println(e);
		}

		System.out.println("\n=== Stanje prije ===");
		System.out.println(game.getPlayer());

		List<Enemy> sudari = game.collidingWithPlayer();
		System.out.println("\n=== Provjera kolizija ===");
		System.out.println("Sudaraju se: " + sudari.size() + " neprijatelja");
		if (sudari.isEmpty()) {
			System.out.println("Nema kolizija.");
		} else {
			for (Enemy e : sudari) {
				System.out.println("Sudar sa: " + e);
			}
		}

		
		game.resolveCollisions();

		
		System.out.println("\n=== Stanje poslije ===");
		System.out.println(game.getPlayer());

		
		System.out.println("\n=== Event log ===");
		for (String line : game.getEventLog()) {
			System.out.println(line);
		}

		try {
			game.saveToFile("rezultat.txt");
			System.out.println("\nRezultat je snimljen u rezultat.txt");
		} catch (IOException ex) {
			System.out.println("Greska pri snimanju: " + ex.getMessage());
		}
	}
}