import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class Main {
	
	private BufferedReader br;
	private int monsterCount = 0;
	private final int monstersPerTier = 3;
	private Weapon equippedWeapon = null;
	private int maxHealth = 15;
	private int health = maxHealth;
	private boolean defending = false;
	private boolean quit = false;
	
	private Random random = new Random();
	
	public void init() {
		br = new BufferedReader(new InputStreamReader(System.in));
		
		Weapon.init();
		Monster.init();
		
		intro();
		
		do {
			armory();
		} while (yesOrNo("Ready?") != 'y');
		fight();
	}
	
	public void intro() {
		System.out.println("Welcome to 'Monster Fight'\nYour goal is to fight increasingly stronger monsters in combat.");
		pressToContinue();
	}
	
	public void armory() {
		System.out.println("\nChoose a weapon:");
		
		for (int i = 0; i < Weapon.weaponCount(); i++) {
			Weapon tmp = Weapon.get(i);
			System.out.println(String.format("[%1$d]: %2$s\tAttack: %3$d\tSpeed: %4$d", i, tmp.name, tmp.damage, tmp.speed));
		}
		
		boolean tryAgain = true;
		int choice = -1;
		while (tryAgain) {
			System.out.print("Enter id of Weapon you want: ");
			try {
				choice = Integer.parseInt(readLine().trim());
			} catch (NumberFormatException nfe) {
			}
			if (Weapon.get(choice) != null)
				tryAgain = false;
		}
		equippedWeapon = Weapon.get(choice);
		
		System.out.println("\nEquipped: " + equippedWeapon.name);
		pressToContinue();
	}
	
	public char yesOrNo(String message) {
		char c;
		do {
			System.out.print(message + " (Y)es/(N)o: ");
			c = Character.toLowerCase(readLine().trim().charAt(0));
		} while (c != 'y' && c != 'n');
		return c;
	}
	
	public void fight() {
		int tier = monsterCount / monstersPerTier + 1;
		
		Monster monster = new Monster(Monster.getRandomByTier(tier));
		
		System.out.println("\nNow fighting: " + monster.name);
		
		while (monster.health > 0 && health > 0 && !quit) {
			// Having a higher speed than the opponent provides a higher chance of going first, though isn't guaranteed.
			int attackFirstChance = (int)(50f * (float)equippedWeapon.speed / (float)monster.speed);
			boolean yourTurn = random.nextInt(100) < attackFirstChance;
			
			if (yourTurn)
				System.out.println("You attack first!");
			else
				System.out.println(monster.name + " attacks first!");
			
			if (!combatPhase(monster, yourTurn))
				break;
		}
		
		// If you haven't quit yet and are still alive, check to see if you've gone up a tier
		// Upon moving to the next tier, increase health and visit the armory
		if (!quit && health > 0) {
			int newTier = monsterCount / monstersPerTier + 1;
			if (newTier > tier) {
				maxHealth += 10;
				health = maxHealth;
				
				do {
					armory();
				} while (yesOrNo("Ready?") != 'y');
			}
		}
		
		// If you did not quit or die, fight again!
		fight();
	}
	
	// If returns true, continues to the next round
	public boolean combatPhase(Monster monster, boolean yourTurn) {
		
		int choice;
		
		// Loops twice, to give each combatant a turn.
		// Keeps track of who's turn it is with the yourTurn variable, which is swapped at the end of the loop
		for (int i = 0; i < 2; i++) {
			printHealth(monster);
			if (yourTurn) {
				choice = fightChoices();
				
				defending = false;
				if (choice == 0) {
					int damage = equippedWeapon.damage;
					if (monster.defending)
						damage /= 2;
					System.out.println("You attack " + monster.name);
					System.out.println(monster.name + " takes " + damage + " damage!");
					monster.health -= damage;
				} else if (choice == 1) {
					System.out.println("You are now defending!");
					defending = true;
				} else if (choice == 2) {
					quit = true;
				}
			} else {
				choice = random.nextInt(100);
				monster.defending = false;
				
				// 75% chance for monster to attack, otherwise defend
				if (choice < 75) {
					int damage = monster.damage;
					if (defending)
						damage /= 2;
					System.out.println(monster.name + " attacks you for " + damage + " damage!");
					health -= damage;
				} else {
					System.out.println(monster.name + " is now defending!");
					monster.defending = true;
				}
			}
			// Potential results from turn
			if (quit) {
				System.out.println("You quit!");
				gameOver();
				return false;
			} else if (monster.health <= 0) {
				System.out.println("\nYou have slain: " + monster.name);
				monsterCount++;
				char cont = yesOrNo("Continue?");
				if (cont == 'n')
					gameOver();
				return false;
			} else if (health <= 0) {
				System.out.println("\nYou have died to: " + monster.name);
				System.out.println("Number of monsters slain: " + monsterCount);
				gameOver();
				return false;
			}
			// Swaps who's turn it is.
			yourTurn = !yourTurn;
		}
		return true;
	}
	
	public void printHealth(Monster monster) {
		System.out.println("\nYour Health: " + health);
		System.out.println(monster.name + "'s Health: " + monster.health); 
	}
	
	public int fightChoices() {
		int choice = -1;
		boolean tryAgain;
		System.out.println("\n[0] Attack\n[1] Defend\n[2] Quit");
		System.out.print("Enter choice: ");
		
		do {
			try {
				choice = Integer.parseInt(readLine().trim());
				tryAgain = false;
			} catch (NumberFormatException e) {
				tryAgain = true;
			}
			if (choice < 0 && choice > 2)
				tryAgain = true;
		} while (tryAgain);
		return choice;
	}
	
	public void gameOver() {
		System.out.println("\n\nGame Over!");
		pressToContinue();
		System.exit(0);
	}
	
	public String readLine() {
		String result = "";
		try {
			result = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void pressToContinue() {
		System.out.print("Press the 'Enter' key to continue...");
		readLine();
	}
	
    public static void main(String[] args) {
    	Main m = new Main();
    	m.init();
    }
}