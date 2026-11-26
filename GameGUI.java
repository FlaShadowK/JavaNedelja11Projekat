
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameGUI extends JFrame {

	private JTextField txtIme = new JTextField(12);
	private JTextField txtHealth = new JTextField(12);
	private JTextField txtX = new JTextField(12);
	private JTextField txtY = new JTextField(12);
	private JComboBox<String> cmbKolajder = new JComboBox<>(
			new String[] { "Pravougaonik 32x32", "Pravougaonik 40x24", "Kvadrat 32x32" });
	private JTextArea txtRezultat = new JTextArea(20, 55);
	private Game game;

	public GameGUI() {
		setTitle("Game Setup");

		JPanel forma = new JPanel(new GridLayout(5, 2, 5, 10));
		forma.setBorder(BorderFactory.createTitledBorder("Igrač"));
		forma.add(new JLabel("Ime:"));
		forma.add(txtIme);
		forma.add(new JLabel("Health (0-100):"));
		forma.add(txtHealth);
		forma.add(new JLabel("X pozicija:"));
		forma.add(txtX);
		forma.add(new JLabel("Y pozicija:"));
		forma.add(txtY);
		forma.add(new JLabel("Kolajder:"));
		forma.add(cmbKolajder);

		JButton btnPokreni = new JButton("Pokreni igru");
		JButton btnSacuvaj = new JButton("Sačuvaj");
		btnPokreni.addActionListener(e -> pokreniIgru());
		btnSacuvaj.addActionListener(e -> sacuvaj());

		JPanel dugmad = new JPanel(new GridLayout(2, 1, 5, 5));
		dugmad.add(btnPokreni);
		dugmad.add(btnSacuvaj);

		JPanel lijevo = new JPanel(new BorderLayout());
		lijevo.add(forma, BorderLayout.NORTH);
		lijevo.add(dugmad, BorderLayout.SOUTH);

		txtRezultat.setEditable(false);
		txtRezultat.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		JScrollPane skrol = new JScrollPane(txtRezultat);
		skrol.setBorder(BorderFactory.createTitledBorder("Rezultat"));

		add(lijevo, BorderLayout.WEST);
		add(skrol, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}

	private Collidable izabraniKolajder() {
		if (cmbKolajder.getSelectedIndex() == 1) {
			return new RectangleCollider(40, 24);
		}
		if (cmbKolajder.getSelectedIndex() == 2) {
			return new SquareCollider(32);
		}
		return new RectangleCollider(32, 32);
	}

	private void pokreniIgru() {
		int health, x, y;
		try {
			health = Integer.parseInt(txtHealth.getText().trim());
			x = Integer.parseInt(txtX.getText().trim());
			y = Integer.parseInt(txtY.getText().trim());
		} catch (NumberFormatException ex) {
			prikaziGresku("Health, X i Y moraju biti cijeli brojevi.");
			return;
		}

		try {
			Player player = new Player(txtIme.getText(), x, y, izabraniKolajder(), health);
			Game novaIgra = new Game(player);
			for (Enemy e : Game.loadEnemiesFromFile("enemies.txt")) {
				novaIgra.addEnemy(e);
			}
			novaIgra.resolveCollisions();
			game = novaIgra;

			txtRezultat.setText(game.getReport());
			txtRezultat.setCaretPosition(0);
			JOptionPane.showMessageDialog(this, game.getEndMessage(), "Kraj igre", JOptionPane.INFORMATION_MESSAGE);
		} catch (IllegalArgumentException ex) {
			prikaziGresku(ex.getMessage());
		} catch (IOException ex) {
			prikaziGresku("Greska pri ucitavanju: " + ex.getMessage());
		}
	}

	private void sacuvaj() {
		if (game == null) {
			prikaziGresku("Prvo pokrenite igru.");
			return;
		}
		try {
			game.saveToFile("rezultat.txt");
			JOptionPane.showMessageDialog(this, "Rezultat je snimljen u rezultat.txt");
		} catch (IOException ex) {
			prikaziGresku("Greska pri snimanju: " + ex.getMessage());
		}
	}

	private void prikaziGresku(String poruka) {
		JOptionPane.showMessageDialog(this, poruka, "Greska", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		new GameGUI().setVisible(true);
	}

}