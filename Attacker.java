
public interface Attacker {

	int getAttackDamage();

	boolean canAttack(Player target);

	boolean attack(Player target);

}