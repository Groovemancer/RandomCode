import java.util.ArrayList;
import java.util.List;


public class Weapon {
	
	public String name;
	public int damage;
	public int speed;
	
	private static List<Weapon> weapons = new ArrayList<Weapon>();
	
	public Weapon(String name, int damage, int speed) {
		this.name = name;
		this.damage = damage;
		this.speed = speed;
	}
	
	public static void init() {
		weapons.add(new Weapon("Sword", 10, 12));
		weapons.add(new Weapon("Axe", 15, 7));
		weapons.add(new Weapon("Dagger", 7, 15));
		weapons.add(new Weapon("Warhammer", 20, 5));
	}
	
	public static int weaponCount() {
		return weapons.size();
	}
	
	public static Weapon get(int id) {
		if (id >= 0 && id < weapons.size())
			return weapons.get(id);
		return null;
	}
}
