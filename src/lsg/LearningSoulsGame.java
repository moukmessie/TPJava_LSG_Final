
package lsg;

import lsg.armor.DragonSlayerLeggings;
import lsg.buffs.rings.DragonSlayerRing;
import lsg.buffs.rings.RingOfDeath;
import lsg.buffs.talismans.MoonStone;
import lsg.characters.Character;
import lsg.characters.Hero;
import lsg.characters.Monster;
import lsg.characters.Zombie;
import lsg.consumables.food.Hamburger;
import lsg.exceptions.*;
import lsg.weapons.Sword;
import lsg.weapons.Weapon;

import java.util.Scanner;

public class LearningSoulsGame {

	private static int ACTION_CODE_MIN = 1 ;
	private static int actionNumber = ACTION_CODE_MIN ;
	private static int ACTION_CODE_ATTACK = actionNumber++ ;
	private static int ACTION_CODE_CONSUME = actionNumber++ ;
	private static int ACTION_CODE_RECUPERATE = actionNumber++ ;
	private static int ACTION_CODE_MAX = actionNumber ;

	public static final String BULLET_POINT = "\u2219 " ;

	private Scanner scanner = new Scanner(System.in) ;

	private Hero hero ;
	private Monster monster ;

	private int score ;

	public LearningSoulsGame() {
		init();
	}

	public Hero getHero() {
		return hero;
	}

	public Monster getMonster() {
		return monster;
	}

	public Monster createMonster(){
		monster = new Zombie() ;
		return monster ;
	}

	public void heroAttack() throws StaminaEmptyException, WeaponBrokenException, WeaponNullException {
		int attack = hero.attack();
		monster.getHitWith(attack) ;
	}

	private void createExhaustedHero(){
		System.out.println("Create exhausted hero : ");
		hero = new Hero() ;

		// pour vider la vie
		hero.getHitWith(99) ;

		// pour depenser la stam
		hero.setWeapon(new Weapon("Grosse Arme", 0, 0, 1000, 100));
		try {
			hero.attack() ;
		} catch (WeaponNullException e) {
			e.printStackTrace();
		} catch (WeaponBrokenException e) {
			e.printStackTrace();
		} catch (StaminaEmptyException e) {
			e.printStackTrace();
		}

		System.out.println(hero);

	}

	private void init(){
		hero = new Hero() ;
		hero.setWeapon(new Sword());
		hero.setArmorItem(new DragonSlayerLeggings(), 1);
		hero.setRing(new RingOfDeath(), 1);
		hero.setRing(new DragonSlayerRing(), 2);

		hero.setConsumable(new Hamburger());

		monster = new Zombie(); // plus besoin de donner la skin et l'arme !
		monster.setTalisman(new MoonStone());

		score = 0 ;
	}

	private void play(){
		init();
		fight1v1();
		while (hero.isAlive()){
			score++ ;
			System.out.println("\n*** !!! NEW MONSTER AHEAD !!! ***\n");
			monster = new Zombie() ;
			monster.setTalisman(new MoonStone());
			fight1v1();
		}
		System.out.println("You score : " + score);
	}

	private void fight1v1(){

		refresh();

		Character agressor = hero ;
		Character target = monster ;
		int action ; // TODO sera effectivement utilise dans une autre version
		int attack, hit ;
		Character tmp ;

		while(hero.isAlive() && monster.isAlive()){ // ATTENTION : boucle infinie si 0 stamina...

			action = 1 ; // par defaut on lancera une attaque
			System.out.println();

			if(agressor == hero) {
				do {
					System.out.print("Hero's action for next move : " +
							"(" + ACTION_CODE_ATTACK +") attack | " +
							"(" + ACTION_CODE_CONSUME +") consume | " +
							"(" + ACTION_CODE_RECUPERATE +") recuperate > ");
					action = scanner.nextInt(); // GENERERA UNE ERREUR L'UTILISATEUR ENTRE AUTRE CHOSE QU'UN ENTIER (ON TRAITERA PLUS TARD)
				}while(action < ACTION_CODE_MIN || action > ACTION_CODE_MAX) ;
				System.out.println();
			}else{
				action = ACTION_CODE_ATTACK ;
			}

			if(action == ACTION_CODE_ATTACK){
				try {
					attack = agressor.attack() ;
				} catch (WeaponNullException e) {
					System.out.println("WARNING : no weapon has been equiped !!!\n");
					attack = 0 ;
				} catch (WeaponBrokenException e) {
					System.out.println("WARNING : " + e.getMessage() + "\n");
					attack = 0 ;
				} catch (StaminaEmptyException e) {
					System.out.println("ACTION HAS NO EFFECT: no more stamina !!!\n");
					attack = 0 ;
				}
				hit = target.getHitWith(attack);
				System.out.printf("%s attacks %s with %s (ATTACK:%d | DMG : %d)", agressor.getName(), target.getName(), agressor.getWeapon(), attack, hit);
				System.out.println();
				System.out.println();
			}else if(action == ACTION_CODE_CONSUME){
				try {
					hero.consume();
				} catch (ConsumeNullException e) {
					System.out.println("IMPOSSIBLE ACTION : no consumable has been equiped !");
				} catch (ConsumeEmptyException e) {
					System.out.println("ACTION HAS NO EFFECT: " + e.getConsumable().getName() + " is empty !");
				} catch (ConsumeRepairNullWeaponException e) {
					System.out.println("IMPOSSIBLE ACTION : no weapon has been equiped !");
				}
				System.out.println();
			}else if(action == ACTION_CODE_RECUPERATE){
				hero.recuperate();
				System.out.println();
			}

			refresh();

			tmp = agressor ;
			agressor = target ;
			target = tmp ;

		}

		Character winner = (hero.isAlive()) ? hero : monster ;
		System.out.println();
		System.out.println("--- " + winner.getName() + " WINS !!! ---");

	}

	private void refresh(){
		hero.printStats();
		hero.printArmor();
		hero.printRings();
		hero.printConsumable();
		hero.printWeapon();
		hero.printBag();

		System.out.println();
		System.out.println();

		monster.printStats();
		monster.printWeapon();
	}

	private void title(){
		System.out.println();
		System.out.println("###############################");
		System.out.println("#   THE LEARNING SOULS GAME   #");
		System.out.println("###############################");
		System.out.println();
	}

	public static void main(String[] args) {
		LearningSoulsGame lsg = new LearningSoulsGame() ;
	}

}
