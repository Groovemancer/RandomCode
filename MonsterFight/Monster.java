import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Monster {

	public String name;
	public int health;
	public int tier;
	public int damage;
	public int speed;
	public boolean defending = false;
	
	private static List<Monster> monsters = new ArrayList<Monster>();
	
	public Monster(String name, int health, int tier, int damage, int speed) {
		this.name = name;
		this.health = health;
		this.tier = tier;
		this.damage = damage;
		this.speed = speed;
	}
	
	public static Monster newInstance(Monster monster) {
		Monster m = new Monster(monster.name, monster.health, monster.tier, monster.damage, monster.speed);
		return m;
	}
	
	public static void init() {
		monsters.add(new Monster("Slime", 10, 1, 5, 10));
		monsters.add(new Monster("Rat", 7, 1, 3, 15));
		monsters.add(new Monster("Goblin", 15, 2, 10, 11));
		monsters.add(new Monster("Wolf", 13, 2, 7, 16));
		monsters.add(new Monster("Ogre", 25, 3, 13, 7));
		monsters.add(new Monster("Vampire", 21, 3, 10, 14));
		monsters.add(new Monster("Dragon", 40, 4, 20, 13));
		monsters.add(new Monster("Chimera", 35, 4, 25, 8));
	}
	
	public static int monsterCount() {
		return monsters.size();
	}
	
	public static Monster get(int id) {
		if (id >= 0 && id < monsters.size())
			return monsters.get(id);
		return null;
	}
	
	public static Monster getRandomByTier(int tier) {
		List<Monster> temp = new ArrayList<Monster>();
		for (Monster m : monsters) {
			if (m.tier == tier)
				temp.add(m);
		}
		
		// If there are no monsters at the desired tier, will check the previous tier
		if (temp.isEmpty())
			getRandomByTier(tier - 1);
		Random r = new Random();
		return temp.get(r.nextInt(temp.size()));
	}
}
