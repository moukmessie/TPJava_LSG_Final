package lsg.characters;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import lsg.consumables.Consumable;
import lsg.consumables.drinks.Drink;
import lsg.consumables.food.Food;
import lsg.consumables.repair.RepairKit;
import lsg.exceptions.*;
import lsg.helper.Dice;
import lsg.bags.Bag;
import lsg.bags.Collectible;
import lsg.bags.SmallBag;
import lsg.weapons.Weapon;

import java.beans.PropertyChangeSupport;
import java.util.Locale;

public abstract class Character {

	public static final String LIFE_STAT_STRING = "life" ;
	public static final String STAM_STAT_STRING = "stamina" ;
	public static final String PROTECTION_STAT_STRING = "protection" ;
	public static final String BUFF_STAT_STRING = "buff" ;

	private static String MSG_ALIVE = "(ALIVE)" ;
	private static String MSG_DEAD = "(DEAD)" ;
	
	private String name ; // Nom du personnage
	
	private int maxLife, life ; 		// Nombre de points de vie restants
	private int maxStamina, stamina ;	// Nombre de points d'action restants

	private int lifeRegen, staminaRegen ; // Nombre de points rendus lors d'une recuperation
	
	private Weapon weapon ;

	private Consumable consumable ;

	protected Bag bag ;

	private Dice dice101 = new Dice(101) ;

	private final DoubleProperty
			lifeRate = new SimpleDoubleProperty(),
			staminaRate = new SimpleDoubleProperty();

	public DoubleProperty lifeRateProperty() {
		return lifeRate;
	}

	public DoubleProperty staminaRateProperty() {
		return staminaRate;
	}

	public Character(String name) {
		this.name = name ;
		bag = new SmallBag() ;
	}

	public void setLifeRegen(int lifeRegen) {
		this.lifeRegen = lifeRegen;
	}

	public int getLifeRegen() {
		return lifeRegen;
	}

	public void setStaminaRegen(int staminaRegen) {
		this.staminaRegen = staminaRegen;
	}

	public int getStaminaRegen() {
		return staminaRegen;
	}

	/**
	 * Permet au personnage de récupérer un peu de vie et de stamina :
	 * calcul basé sur lifeRegen et staminaRegen
	 */
	public void recuperate(){
		int lifeRecup = ((life+lifeRegen) <= maxLife) ? lifeRegen : maxLife - life ;
		setLife(life+lifeRecup);
		int stamRecup = ((stamina+staminaRegen) <= maxStamina) ? staminaRegen : maxStamina - stamina ;
		setStamina(stamina+stamRecup);
//		System.out.println(getName() + " recuperates " + lifeRecup + "Life Point(s) and " + stamRecup + "Stamina Point(s)");
	}

	/**
	 * @return la capacite du sac équipé
	 * @throws BagNullException
	 */
	public int getBagCapacity() throws BagNullException {
		if(bag == null) throw new BagNullException() ;
		return bag.getCapacity() ;
	}

	/**
	 * @return le poids total courant du sac equipe
	 * @throws BagNullException
	 */
	public int getBagWeight() throws BagNullException {
		if(bag == null) throw new BagNullException() ;
		return bag.getWeight() ;
	}

	/**
	 * @return un tableau contenant les Collectible du sac équipé
	 * @throws BagNullException
	 */
	public Collectible[] getBagItems() throws BagNullException {
		if(bag == null) throw new BagNullException() ;
		return bag.getItems() ;
	}

	/**
	 * Ajoute un item dans le sac s'il n'est pas plein
	 * @param item
	 * @throws BagNullException
	 * @throws BagFullException
	 */
	public void pickUp(Collectible item) throws BagNullException, BagFullException {
		if(bag == null) throw new BagNullException() ;
		System.out.print(name + " picks up " + item);
		bag.push(item);
	}

	/**
	 * Retire un item du sac s'il y était
	 * @param item : item à retirer
	 * @return l'item retiré ; null si l'item n'était pas dans le sac
	 * @throws BagNullException
	 */
	public Collectible pullOut(Collectible item) throws BagNullException {
		if(bag == null) throw new BagNullException() ;
		System.out.print(name + " pulls out " + item);
		return bag.pop(item) ;
	}

	/**
	 * Change le sac du personnage, en transférant (ce qu'il est possible) dans le nouveau sac.
	 * @param newBag : le nouveau sac contenant les items transférés
	 * @return l'ancien sac avec les items qui n'ont pas pu être transféré
	 */
	public Bag setBag(Bag newBag){
		String oldBagName, newBagName ;
		String nullBagName = "null" ;
		try{
			oldBagName = bag.getClass().getSimpleName() ;
		}catch (NullPointerException e){ oldBagName = nullBagName ; }
		try {
			newBagName = newBag.getClass().getSimpleName();
		}catch (NullPointerException e){ newBagName = nullBagName ; }
		System.out.println(name + " changes " + oldBagName + " for " + newBagName);
		Bag oldBag = bag ;
		Bag.transfer(oldBag, newBag);
		bag = newBag ;
		return oldBag ;
	}

	/**
	 * Recherche le consommable passé en paramètre dans le sac, et l'équipe (donc le retire du sac)
	 * Ne fait rien si le consommable n'est pas dans le sac
	 * @param consumable
	 * @throws BagNullException
	 */
	public void equip(Consumable consumable) throws BagNullException {
		if(bag == null) throw new BagNullException() ;
		if(consumable == null) return ;
		if(bag.contains(consumable)){
			pullOut(consumable) ;
			System.out.println(" and equips it !");
			setConsumable(consumable);
		}
	}

	/**
	 * Recherche l'arme passée en paramètre dans le sac, et l'équipe (donc la retire du sac)
	 * Ne fait rien si l'arme n'est pas dans le sac
	 * @param weapon
	 */
	public void equip(Weapon weapon) throws BagNullException {
		if(bag == null) throw new BagNullException() ;
		if(weapon == null) return ;
		if(bag.contains(weapon)){
			pullOut(weapon) ;
			System.out.println(" and equips it !");
			setWeapon(weapon);
		}
	}

	public Consumable getConsumable() {
		return consumable;
	}

	/**
	 * Methode qui utilise le consommable equipé par le personnage
	 * @throws ConsumeNullException
	 * @throws ConsumeEmptyException
	 * @throws ConsumeRepairNullWeaponException
	 */
	public void consume() throws ConsumeNullException, ConsumeEmptyException, ConsumeRepairNullWeaponException {
		use(consumable);
	}

	public void setConsumable(Consumable consumable) {
		this.consumable = consumable;
	}

	private void eat(Food food) throws ConsumeNullException, ConsumeEmptyException {
		if(food == null) throw new ConsumeNullException(food);
		System.out.println(getName() + " eats " + food);
		int newLife = getLife() + food.use() ;
		newLife = (newLife < maxLife) ? newLife : maxLife ;
		setLife(newLife);
	}

	private void drink(Drink drink) throws ConsumeNullException, ConsumeEmptyException {
		if(drink == null) throw new ConsumeNullException(drink);
		System.out.println(getName() + " drinks " + drink);
		int newStam = getStamina() + drink.use() ;
		newStam = (newStam < maxStamina) ? newStam : maxStamina ;
		setStamina(newStam);
	}

	private void repairWeaponWith(RepairKit kit) throws WeaponNullException, ConsumeNullException {
		if(weapon ==null) throw new WeaponNullException() ;
		System.out.println(getName() + " repairs " + weapon + " with " + kit);
		weapon.repairWith(kit);
	}

	public void use(Consumable consumable) throws ConsumeNullException, ConsumeEmptyException, ConsumeRepairNullWeaponException {
		if(consumable == null) throw new ConsumeNullException(consumable) ;
		if(consumable instanceof Drink){
			drink((Drink)consumable);
		}else if(consumable instanceof Food){
			eat((Food)consumable) ;
		}else if(consumable instanceof RepairKit){
			try {
				repairWeaponWith((RepairKit)consumable);
			} catch (WeaponNullException e) {
				throw new ConsumeRepairNullWeaponException(consumable) ;
			}
		}
	}

	/**
	 * Methode qui consomme le premier item du sac qui est instance de la classe passée en paramètre.
	 * Consommer signifie utiliser puis retirer du sac s'il est vide (getCapacity() vaut 0)
	 * @param type : la classe de l'objet recherché
	 * @return : le premier objet de la classe type trouvé ; null si le sac ne contient pas d'objet de cette classe
	 * @throws ConsumeNullException
	 * @throws ConsumeEmptyException
	 * @throws ConsumeRepairNullWeaponException
	 * @throws BagNullException
	 */
	private Consumable fastUseFirst(Class<? extends Consumable> type) throws ConsumeNullException, ConsumeEmptyException, ConsumeRepairNullWeaponException, BagNullException {
		if(type == null) return null ;
		Consumable found = null ;
		Collectible[] items = getBagItems() ;
		for(Collectible item: items){
			if(type.isInstance(item)){
				found = (Consumable)item ;
				break;
			}
		}
		use(found) ;
		if(found.getCapacity() == 0) pullOut(found) ;
		return found ;
	}

	/**
	 * Le personnage boit la première boisson trouvée dans le sac, puis jette l'objet
	 * Ne fait rien si le sac ne contient pas de boisson
	 * @return la boisson
	 * @throws ConsumeNullException
	 * @throws ConsumeEmptyException
	 * @throws BagNullException
	 */
	public Drink fastDrink() throws ConsumeNullException, ConsumeEmptyException, BagNullException {
		System.out.println(name + " drinks FAST :");
		Drink drink = null ;
		try {
			drink = (Drink) fastUseFirst(Drink.class) ;
		} catch (ConsumeRepairNullWeaponException e) {
			e.printStackTrace();
		}
		return drink ;
	}

	/**
	 * Le personnage mange la première nourriture trouvée dans le sac, puis jette l'objet
	 * Ne fait rien si le sac ne contient pas de nourriture
	 * @return la nourriture
	 * @throws ConsumeNullException
	 * @throws ConsumeEmptyException
	 * @throws BagNullException
	 */
	public Food fastEat() throws ConsumeNullException, ConsumeEmptyException, BagNullException {
		System.out.println(name + " eats FAST :");
		Food food = null ;
		try {
			food = (Food) fastUseFirst(Food.class);
		} catch (ConsumeRepairNullWeaponException e) {
			e.printStackTrace();
		}
		return food ;
	}

	/**
	 * Le personnage le premier kit trouvée dans le sac, puis jette l'objet
	 * Ne fait rien si le sac ne contient pas de nourriture
	 * @return le kit
	 * @throws ConsumeNullException
	 * @throws ConsumeEmptyException
	 * @throws ConsumeRepairNullWeaponException
	 * @throws BagNullException
	 */
	public RepairKit fastRepair() throws ConsumeNullException, ConsumeEmptyException, ConsumeRepairNullWeaponException, BagNullException {
		System.out.println(name + " repairs FAST :");
		return (RepairKit) fastUseFirst(RepairKit.class);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMaxLife() {
		return maxLife;
	}
	
	protected void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}
	
	public int getLife() {
		return life;
	}
	
	protected void setLife(int life) {
		this.life = life;
		lifeRate.set((double)life/maxLife);
	}
	
	public int getMaxStamina() {
		return maxStamina;
	}
	
	protected void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}
	
	public int getStamina() {
		return stamina;
	}
	
	protected void setStamina(int stamina) {
		this.stamina = stamina;
		staminaRate.set((double)stamina/maxStamina);
	}
	
	public boolean isAlive(){
		return life > 0 ;
	}
	
	public void printStats(){
		System.out.println(this);
	}

	public void printBag(){
		System.out.print("BAG : " + bag);
	}

	public void printConsumable(){
		System.out.println("CONSUMABLE : " + consumable);
	}

	public void printWeapon(){
		System.out.println("WEAPON : " + weapon);
	}

	@Override
	public String toString() {
		
		String classe = getClass().getSimpleName() ;
		String life = String.format("%5d", getLife()) ; 
		String stam = String.format("%5d", getStamina()) ; 
		String protection = String.format(Locale.US, "%6.2f", computeProtection()) ;
		String buff = String.format(Locale.US, "%6.2f", computeBuff()) ;
		
		String msg = String.format("%-20s %-20s %s:%-10s %s:%-10s %s:%-10s %s:%-10s", "[ " + classe + " ]",
				getName(),
				LIFE_STAT_STRING.toUpperCase(),
				life,
				STAM_STAT_STRING.toUpperCase(),
				stam,
				PROTECTION_STAT_STRING.toUpperCase(),
				protection,
				BUFF_STAT_STRING.toUpperCase(),
				buff) ;
		
		String status ;
		if(isAlive()){
			status = MSG_ALIVE ;
		}else{
			status = MSG_DEAD ;
		}
		
		return msg + status ;
	}
	
	public int attack() throws WeaponNullException, WeaponBrokenException, StaminaEmptyException {
		return attackWith(this.getWeapon()) ;
	}
	
	/**
	 * Calcule une attaque en fonction d'une arme.
	 * Le calcul dépend des statistiques de l'arme, de la stamina (restante) du personnage et des buffs eventuels
	 * 
	 * @param weapon : l'arme utilisée.
	 * @return la valeur de l'attaque eventuellement buffée ; 0 si l'arme est cassée.
	 * @throws WeaponNullException
	 * @throws WeaponBrokenException
	 */
	protected int attackWith(Weapon weapon) throws WeaponNullException, WeaponBrokenException, StaminaEmptyException {

		if(weapon == null) throw new WeaponNullException() ;
		if(weapon.isBroken()) throw new WeaponBrokenException(weapon) ;
		if(stamina == 0) throw new StaminaEmptyException() ;

		int min = weapon.getMinDamage() ;
		int max = weapon.getMaxDamage() ;
		int cost = weapon.getStamCost() ;

		int attack = 0 ;

		attack = min + Math.round((max-min) * dice101.roll() / 100.f) ;
		int stam = getStamina() ;
		if(cost <= stam){ // il y a assez de stam pour lancer l'attaque
			setStamina(getStamina()-cost);
		}else{
			attack = Math.round(attack * ((float)stam / cost)) ;
			setStamina(0);
		}

		weapon.use();

		return attack + Math.round(attack*computeBuff()/100);
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	/**
	 * @return le nombre de points de buff du personnage
	 */
	protected abstract float computeBuff() ; 
	
	/**
	 * @return le nombre de points de protection du personnage
	 */
	protected abstract float computeProtection() ;
	
	/**
	 * Calcule le nombre de PV retires en tenant compte de la protection
	 * @param value : le montant des degats reçus
	 * @return le nombre de PV effectivement retires (value reduite par la protecion si assez de vie ; le reste de la vie sinon)
	 */
	public int getHitWith(int value){
		
		int life = getLife() ;
		int dmg ;
		float protection = computeProtection() ;
		if(protection > 100) protection = 100 ; // si la protection depasse 100, elle absorbera 100% de l'attaque
		value = Math.round(value - (value * protection / 100)) ;
		dmg = (life > value) ? value : life ; 
		setLife(life-dmg);
		return dmg ;
				
	}
	
}
