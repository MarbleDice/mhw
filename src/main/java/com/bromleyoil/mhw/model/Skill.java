package com.bromleyoil.mhw.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public enum Skill {

	ADRENALINE("Adrenaline", "", 0,
		"Temporarily reduces stamina depletion when health is at 40% or lower."),
	AFFINITY_SLIDING("Affinity Sliding", "Slider Jewel 2", 2,
		"Increases affinity by 30% for a short time after sliding."),
	AGITATOR("Agitator", "Challenger Jewel 2", 2,
		"While active, grants attack +4 and increases affinity by 3%.",
		"While active, grants attack +8 and increases affinity by 6%.",
		"While active, grants attack +12 and increases affinity by 9%.",
		"While active, grants attack +16 and increases affinity by 12%.",
		"While active, grants attack +20 and increases affinity by 15%."),
	AIRBORNE("Airborne", "Flight Jewel 2", 2,
		"Jumping attack power +10%"),
	AQUATIC_EXPERT("Aquatic Expert", "Mirewalker Jewel 1", 1,
		"Prevents water from slowing movement.",
		"Prevents water from slowing movement and improves evasion while in water.",
		"Prevents water from slowing movement and greatly improves evasion while in water."),
	ARTILLERY("Artillery", "Artillery Jewel 1", 1,
		"Increases power of each attack by 10% and reduces Wyvern's Fire cooldown by 15%.",
		"Increases power of each attack by 20% and reduces Wyvern's Fire cooldown by 30%.",
		"Increases power of each attack by 30% and reduces Wyvern's Fire cooldown by 50%."),
	ATTACK_BOOST("Attack Boost", "Attack Jewel 1", 1,
		"Attack +3",
		"Attack +6",
		"Attack +9",
		"Attack +12 Affinity +5%",
		"Attack +15 Affinity +5%",
		"Attack +18 Affinity +5%",
		"Attack +21 Affinity +5%"),
	BBQ_MASTER("BBQ Master", "", 0,
		"Makes it easier to cook well-done steaks."),
	BLAST_ATTACK("Blast Attack", "Blast Jewel 1", 1,
		"Blast buildup +5% Bonus: +10",
		"Blast buildup +10% Bonus: +10",
		"Blast buildup +20% Bonus: +10"),
	BLAST_FUNCTIONALITY("Blast Functionality", "Blastcoat Jewel 3", 3,
		"Lets you use blast coating."),
	BLAST_RESISTANCE("Blast Resistance", "Antiblast Jewel 1", 1,
		"Delays blastblight and reduces blast damage.",
		"Greatly delays blastblight and greatly reduces blast damage.",
		"Prevents blastblight."),
	BLEEDING_RESISTANCE("Bleeding Resistance", "Suture Jewel 1", 1,
		"Reduces damage while bleeding.",
		"Greatly reduces damage while bleeding.",
		"Prevents bleeding."),
	BLIGHT_RESISTANCE("Blight Resistance", "Resistor Jewel 1", 1,
		"Reduces the duration of all elemental blights by 30%.",
		"Reduces the duration of all elemental blights by 60%.",
		"Nullifies all elemental blights."),
	BLINDSIDER("Blindsider", "", 0,
		"Increases the success rate of flash effects."),
	BLUDGEONER("Bludgeoner", "", 0,
		"Raises attack as your weapon loses sharpness. Also boosts ranged weapon melee attacks and odds of stunning."),
	BOMBARDIER("Bombardier", "Bomber Jewel 1", 1,
		"Explosive power +10%",
		"Explosive power +20%",
		"Explosive power +30%"),
	BOTANIST("Botanist", "Botany Jewel 1", 1,
		"One extra consumable herb item per gather.",
		"One extra consumable herb, fruit, nut, or seed item per gather.",
		"One extra consumable herb, fruit, nut, seed, or insect item per gather.",
		"One extra consumable herb, fruit, nut, seed, insect or mushroom item per gather."),
	BOW_CHARGE_PLUS("Bow Charge Plus", "Mighty Bow Jewel 2", 2,
		"Increases max bow charge level by one."),
	CAPACITY_BOOST("Capacity Boost", "Magazine Jewel 2", 2,
		"Loading capacity +1"),
	CAPTURE_MASTER("Capture Master", "", 0,
		"High chance of increased capture rewards. (No effect when joining mid-quest.)"),
	CARVING_PRO("Carving Pro", "", 0,
		"Prevents knockback while carving."),
	CLIFFHANGER("Cliffhanger", "", 0,
		"Reduces stamina depletion while climbing by 25%."),
	CONSTITUTION("Constitution", "Physique Jewel 2", 2,
		"Reduces fixed stamina depletion by 10%.",
		"Reduces fixed stamina depletion by 20%.",
		"Reduces fixed stamina depletion by 30%.",
		"Reduces fixed stamina depletion by 40%.",
		"Reduces fixed stamina depletion by 50%."),
	CRITICAL_BOOST("Critical Boost", "Critical Jewel 2", 2,
		"Increases damage dealt by critical hits by 30%.",
		"Increases damage dealt by critical hits by 35%.",
		"Increases damage dealt by critical hits by 40%."),
	CRITICAL_DRAW("Critical Draw", "Draw Jewel 2", 2,
		"Draw attack affinity +30%",
		"Draw attack affinity +60%",
		"Draw attack affinity +100%"),
	CRITICAL_ELEMENT("Critical Element", "", 0,
		"Increases elemental damage (fire, water, thunder, ice, dragon) when landing critical hits."),
	CRITICAL_EYE("Critical Eye", "Expert Jewel 1", 1,
		"Affinity +3%",
		"Affinity +6%",
		"Affinity +10%",
		"Affinity +15%",
		"Affinity +20%",
		"Affinity +25%",
		"Affinity +30%"),
	CRITICAL_STATUS("Critical Status", "", 0,
		"Increases abnormal status effect damage (paralysis, poison, sleep, blast) when landing critical hits."),
	DEFENSE_BOOST("Defense Boost", "Defense Jewel 1", 1,
		"Defense +5",
		"Defense +10",
		"Defense +15",
		"Defense +20 All Elemental Resistances +3",
		"Defense +25 All Elemental Resistances +3",
		"Defense +30 All Elemental Resistances +3",
		"Defense +35 All Elemental Resistances +3"),
	DETECTOR("Detector", "", 0,
		"Marks rare sites on the wildlife map."),
	DIVINE_BLESSING("Divine Blessing", "Protection Jewel 1", 1,
		"While active, reduces damage taken by 15%.",
		"While active, reduces damage taken by 30%.",
		"While active, reduces damage taken by 50%."),
	DRAGON_ATTACK("Dragon Attack", "Dragon Jewel 1", 1,
		"Dragon attack +30",
		"Dragon attack +60",
		"Dragon attack +100",
		"Dragon attack +5% Bonus: +100",
		"Dragon attack +10% Bonus: +100"),
	DRAGON_RESISTANCE("Dragon Resistance", "Dragon Res Jewel 1", 1,
		"Dragon resistance +6",
		"Dragon resistance +12",
		"Dragon resistance +20 Defense +10"),
	DUNGMASTER("Dungmaster", "", 0,
		"Makes slinger dung pods more effective at making monsters run away."),
	EARPLUGS("Earplugs", "Earplug Jewel 3", 3,
		"Slightly reduces the effects of weak monster roars.",
		"Reduces the effects of weak monster roars.",
		"Nullifies weak monster roars.",
		"Nullifies weak monster roars and reduces the effects of strong monster roars.",
		"Nullifies weak and strong monster roars."),
	EFFLUVIA_RESISTANCE("Effluvia Resistance", "Miasma Jewel 1", 1,
		"Reduces effluvial buildup.",
		"Greatly reduces effluvial buildup.",
		"Prevents effluvial buildup."),
	EFFLUVIAL_EXPERT("Effluvial Expert", "", 0,
		"Nullifies damage from effluvia and reduces damage from acids."),
	ELDERSEAL_BOOST("Elderseal Boost", "Dragonseal Jewel 3", 3,
		"Boosts Elderseal one level."),
	ENTOMOLOGIST("Entomologist", "Specimen Jewel 1", 1,
		"Increases the chances of a corpse being left behind.",
		"Greatly increases the chances of a corpse being left behind.",
		"Makes it so corpses will always be left behind."),
	EVADE_EXTENDER("Evade Extender", "Jumping Jewel 2", 2,
		"Slightly extends evasion distance.",
		"Extends evasion distance.",
		"Greatly extends evasion distance."),
	EVADE_WINDOW("Evade Window", "Evasion Jewel 2", 2,
		"Very slightly increases invulnerability window.",
		"Slightly increases invulnerability window.",
		"Increases invulnerability window.",
		"Greatly increases invulnerability window.",
		"Massively increases invulnerability window."),
	FIRE_ATTACK("Fire Attack", "Blaze Jewel 1", 1,
		"Fire attack +30",
		"Fire attack +60",
		"Fire attack +100",
		"Fire attack +5% Bonus: +100",
		"Fire attack +10% Bonus: +100"),
	FIRE_RESISTANCE("Fire Resistance", "Fire Res Jewel 1", 1,
		"Fire resistance +6",
		"Fire resistance +12",
		"Fire resistance +20 Defense +10"),
	FLINCH_FREE("Flinch Free", "Brace Jewel 3", 3,
		"Prevents knockbacks.",
		"Prevents standard knockbacks. Tripping is reduced to a knockback instead.",
		"Prevents knockbacks and tripping."),
	FOCUS("Focus", "Charger Jewel 2", 2,
		"Increases gauge fill rate by 5% and reduces charge times by 5%.",
		"Increases gauge fill rate by 10% and reduces charge times by 10%.",
		"Increases gauge fill rate by 20% and reduces charge times by 20%."),
	FORAGERS_LUCK("Forager's Luck", "", 0,
		"Increases the likelihood of rare gathering points respawning."),
	FORTIFY("Fortify", "Fortitude Jewel 1", 1,
		"Increases attack by 10% and defense by 15% with each use."),
	FREE_ELEM_AMMO_UP("Free Elem/Ammo Up", "Release Jewel 3", 3,
		"Draws out 33% of hidden element and expands clip size for some ammo.",
		"Draws out 66% of hidden element and expands clip size for some ammo.",
		"Draws out 100% of hidden element and expands clip size for some ammo."),
	FREE_MEAL("Free Meal", "Satiated Jewel 1", 1,
		"Activates 25% of the time."),
	GEOLOGIST("Geologist", "Geology Jewel 1", 1,
		"Allows you to gather one extra time from bonepiles.",
		"Allows you to gather one extra time from bonepiles and special item gathering points.",
		"Allows you to gather one extra time from bonepiles, gathering points, and mining outcrops."),
	GOOD_LUCK("Good Luck", "", 0,
		"Good chance of increased quest rewards. (No effect when joining mid-quest.)"),
	GREAT_LUCK("Great Luck", "", 0,
		"Great chance of increased quest rewards? (No effect when joining mid-quest.)"),
	GUARD("Guard", "Ironwall Jewel 1", 1,
		"Very slightly decreases the impact of attacks.",
		"Slightly decreases the impact of attacks and reduces stamina depletion by 15%.",
		"Greatly decreases the impact of attacks and redcues stamina depletion by 15%.",
		"Greatly decreases the impact of attacks and redcues stamina depletion by 30%.",
		"Greatly decreases the impact of attacks and redcues stamina depletion by 50%."),
	GUARD_UP("Guard Up", "Shield Jewel 2", 2,
		"Allows you to guard against ordinarily unblockable attacks."),
	GUTS("Guts", "", 0,
		"Above a certain health threshold, you withstand an attack that would normally cart you (once only)."),
	HANDICRAFT("Handicraft", "Handicraft Jewel 3", 3,
		"Weapon sharpness +10",
		"Weapon sharpness +20",
		"Weapon sharpness +30",
		"Weapon sharpness +40",
		"Weapon sharpness +50"),
	HASTEN_RECOVERY("Hasten Recovery", "", 0,
		"Regenerates your health as you continually attack a monster. Recovery varies by weapon."),
	HEALTH_BOOST("Health Boost", "Vitality Jewel 1", 1,
		"Health +15",
		"Health +30",
		"Health +50"),
	HEAT_GUARD("Heat Guard", "", 0,
		"Nullfies heat damage."),
	HEAVY_ARTILLERY("Heavy Artillery", "Heavy Artillery Jewel 1", 1,
		"Firepower +10%",
		"Firepower +20%"),
	HEROICS("Heroics", "Potential Jewel 2", 2,
		"While active, increases attack power by 5% and increases defense by 15 points.",
		"While active, increases attack power by 10% and increases defense by 20 points.",
		"While active, increases attack power by 15% and increases defense by 25 points.",
		"While active, increases attack power by 20% and increases defense by 30 points.",
		"While active, increases attack power by 30% and increases defense by 40 points."),
	HONEY_HUNTER("Honey Hunter", "", 0,
		"One extra honey per gather."),
	HORN_MAESTRO("Horn Maestro", "Sonorous Jewel 1", 1,
		"Extends melody effect duration and increases health recovery."),
	HUNGER_RESISTANCE("Hunger Resistance", "Hungerless Jewel 1", 1,
		"Extends the time until your stamina cap decreases by 30%.",
		"Extends the time until your stamina cap decreases by 60%.",
		"Prevents your stamina cap from decreasing."),
	ICE_ATTACK("Ice Attack", "Frost Jewel 1", 1,
		"Ice attack +30",
		"Ice attack +60",
		"Ice attack +100",
		"Ice attack +5% Bonus: +100",
		"Ice attack +10% Bonus: +100"),
	ICE_RESISTANCE("Ice Resistance", "Ice Res Jewel 1", 1,
		"Ice resistance +6",
		"Ice resistance +12",
		"Ice resistance +20 Defense +10"),
	INTIMIDATOR("Intimidator", "Intimidator Jewel 1", 1,
		"Discourages monsters from engaging you even if you've been spotted.",
		"Highly discourages monsters from engaging you even if you've been spotted.",
		"Prevents monsters from engaging you even if you've been spotted."),
	IRON_SKIN("Iron Skin", "Def Lock Jewel 1", 1,
		"Reduces the duration of Defense Down by 30%./hideout",
		"Reduces the duration of Defense Down by 60%.",
		"Prevents Defense Down."),
	ITEM_PROLONGER("Item Prolonger", "Enduring Jewel 1", 1,
		"Item effect duration +10%",
		"Item effect duration +25%",
		"Item effect duration +50%"),
	JUMP_MASTER("Jump Master", "", 0,
		"Negates knockback during jumps."),
	LATENT_POWER("Latent Power", "Throttle Jewel 2", 2,
		"While active, increases affinity by 10% and reduces stamina depletion by 10%.",
		"While active, increases affinity by 20% and reduces stamina depletion by 10%.",
		"While active, increases affinity by 30% and reduces stamina depletion by 30%.",
		"While active, increases affinity by 40% and reduces stamina depletion by 30%.",
		"While active, increases affinity by 50% and reduces stamina depletion by 50%."),
	LEAP_OF_FAITH("Leap of Faith", "", 0,
		"Enables Leap of Faith."),
	MARATHON_RUNNER("Marathon Runner", "Sprinter Jewel 2", 2,
		"Reduces continuous stamina depletion by 15%.",
		"Reduces continuous stamina depletion by 30%.",
		"Reduces continuous stamina depletion by 50%."),
	MASTER_FISHER("Master Fisher", "", 0,
		"Increases the time window for reeling in fish, and makes it easier to catch large fish."),
	MASTER_GATHERER("Master Gatherer", "", 0,
		"Speeds up gathering and prevents knockbacks while gathering."),
	MASTER_MOUNTER("Master Mounter", "", 0,
		"Makes it easier to mount monsters and down mounted monsters."),
	MASTERS_TOUCH("Master's Touch", "", 0,
		"Prevents your weapon from losing sharpness during critical hits."),
	MAXIMUM_MIGHT("Maximum Might", "Mighty Jewel 2", 2,
		"While active, increases affinity by 10%.",
		"While active, increases affinity by 20%.",
		"While active, increases affinity by 30%."),
	MINDS_EYE_BALLISTICS("Mind's Eye/Ballistics", "Mind's Eye Jewel 2", 2,
		"Prevents attacks from being deflected. Also shortens the distance before ammo and arrows reach maximum power."),
	MUCK_RESISTANCE("Muck Resistance", "", 0,
		"Reduces limits on movement speed and evasion."),
	MUSHROOMANCER("Mushroomancer", "Fungiform Jewel 1", 1,
		"Lets you digest blue mushrooms and toadstools.",
		"Additionally lets you digest nitroshrooms and parashrooms.",
		"Additionally lets you digest mandragoras, devil's blight, and exciteshrooms."),
	NON_ELEMENTAL_BOOST("Non-elemental Boost", "Elementless Jewel 2", 2,
		"Powers up non-elemental weapons you have equipped."),
	NORMAL_SHOTS("Normal Shots", "Forceshot Jewel 3", 3,
		"Increases the power of normal ammo and normal arrows."),
	NULLIFY_WIND_PRESSURE("Nullify Wind Pressure", "", 0,
		"Negates all wind pressure."),
	PALICO_RALLY("Palico Rally", "Meowster Jewel 1", 1,
		"Increases Palico attack power and defense by 5%.",
		"Increases Palico attack power and defense by 10%.",
		"Increases Palico attack power and defense by 15%.",
		"Increases Palico attack power and defense by 20%.",
		"Increases Palico attack power and defense by 25%."),
	PARA_FUNCTIONALITY("Para Functionality", "Paracoat Jewel 3", 3,
		"Lets you use paralysis coating."),
	PARALYSIS_ATTACK("Paralysis Attack", "Paralyzer Jewel 1", 1,
		"Paralysis buildup +5% Bonus: +10",
		"Paralysis buildup +10% Bonus: +10",
		"Paralysis buildup +20% Bonus: +10"),
	PARALYSIS_RESISTANCE("Paralysis Resistance", "Antipara Jewel 1", 1,
		"Reduces the duration of paralysis by 30%.",
		"Reduces the duration of paralysis by 60%.",
		"Prevents paralysis."),
	PARTBREAKER("Partbreaker", "Destroyer Jewel 2", 2,
		"Part damage +10%",
		"Part damage +20%",
		"Part damage +30%"),
	PEAK_PERFORMANCE("Peak Performance", "Flawless Jewel 2", 2,
		"While active, grants attack +5.",
		"While active, grants attack +10.",
		"While active, grants attack +20."),
	PIERCING_SHOTS("Piercing Shots", "Pierce Jewel 3", 3,
		"Increases the power of piercing ammo and Dragon Piercer."),
	POISON_ATTACK("Poison Attack", "Venom Jewel 1", 1,
		"Poison buildup +5% Bonus: +10",
		"Poison buildup +10% Bonus: +10",
		"Poison buildup +20% Bonus: +10"),
	POISON_DURATION_UP("Poison Duration Up", "", 0,
		"Extends the duration of your poison's effect on monsters."),
	POISON_FUNCTIONALITY("Poison Functionality", "Poisoncoat Jewel 3", 3,
		"Lets you use poison coating."),
	POISON_RESISTANCE("Poison Resistance", "Antidote Jewel 1", 1,
		"Reduces the duration of poison by 30%.",
		"Reduces the duration of poison by 60%.",
		"Prevents poison."),
	POWER_PROLONGER("Power Prolonger", "Enhancer Jewel 2", 2,
		"Bonus duration +10%",
		"Bonus duration +20%",
		"Bonus duration +30%"),
	PRO_TRANSPORTER("Pro Transporter", "", 0,
		"Increases movement speed while transporting and reduces downtime on landings."),
	PROTECTIVE_POLISH("Protective Polish", "Sharp Jewel 2", 2,
		"Weapon sharpness does not decrease for a set time after sharpening."),
	PUNISHING_DRAW("Punishing Draw", "", 0,
		"Adds a stun effect to draw attacks and slightly increases attack power."),
	QUICK_SHEATH("Quick Sheath", "Sheath Jewel 1", 1,
		"Slightly increases sheathing speed.",
		"Moderately increases sheathing speed.",
		"Greatly increases sheathing speed."),
	RAZOR_SHARP_SPARE_SHOT("Razor Sharp/Spare Shot", "", 0,
		"Halves sharpness loss. For bows and bowguns, shots have a chance not to expend coatings or ammo."),
	RECOVERY_SPEED("Recovery Speed", "Recovery Jewel 1", 1,
		"Doubles the speed at which you heal recoverable damage.",
		"Triples the speed at which you heal recoverable damage.",
		"Quadruples the speed at which you heal recoverable damage."),
	RECOVERY_UP("Recovery Up", "Medicine Jewel 1", 1,
		"Recovery +10%",
		"Recovery +20%",
		"Recovery +30%"),
	RESENTMENT("Resentment", "Furor Jewel 2", 2,
		"While active, grants attack +5.",
		"While active, grants attack +10.",
		"While active, grants attack +15.",
		"While active, grants attack +20.",
		"While active, grants attack +25."),
	RESUSCITATE("Resuscitate", "Crisis Jewel 1", 1,
		"While active, greatly improves evasion invulnerability time and reduces stamina depletion."),
	SCENTHOUND("Scenthound", "Scent Jewel 1", 1,
		"Increases gauge fill rate by 50%."),
	SCHOLAR("Scholar", "", 0,
		"Speeds up progress on research levels and special investigations."),
	SCOUTFLY_RANGE_UP("Scoutfly Range Up", "", 0,
		"Expands your scoutflies' detection range."),
	SLEEP_ATTACK("Sleep Attack", "Sleep Jewel 1", 1,
		"Sleep buildup +5% Bonus: +10",
		"Sleep buildup +10% Bonus: +10",
		"Sleep buildup +20% Bonus: +10"),
	SLEEP_FUNCTIONALITY("Sleep Functionality", "Sleepcoat Jewel 3", 3,
		"Lets you use sleep coating."),
	SLEEP_RESISTANCE("Sleep Resistance", "Pep Jewel 1", 1,
		"Reduces the duration of sleep by 30%.",
		"Reduces the duration of sleep by 60%.",
		"Prevents sleep."),
	SLINGER_CAPACITY("Slinger Capacity", "Stonethrower Jewel 1", 1,
		"Increases the loading capacity for slinger ammo collected from the ground or plants.",
		"In addition, increases the loading capacity for some slinger ammo dropped by monsters.",
		"In addition, increases the loading capacity for all slinger ammo dropped by monsters."),
	SLUGGER("Slugger", "KO Jewel 2", 2,
		"Stun power +10%",
		"Stun power +20%",
		"Stun power +30%"),
	SPECIAL_AMMO_BOOST("Special Ammo Boost", "Trueshot Jewel 1", 1,
		"Increases the power of bowgun special ammo and Dragon Piercer by 10%.",
		"Increases the power of bowgun special ammo and Dragon Piercer by 20%."),
	SPEED_CRAWLER("Speed Crawler", "", 0,
		"Increases movement speed while crouching."),
	SPEED_EATING("Speed Eating", "Gobbler Jewel 1", 1,
		"Slightly increases item use speed.",
		"Moderately increases item use speed.",
		"Greatly increases item use speed."),
	SPEED_SHARPENING("Speed Sharpening", "Grinder Jewel 1", 1,
		"Removes one cycle from the sharpening process.",
		"Removes two cycles from the sharpening process.",
		"Removes three cycles from the sharpening process."),
	SPOREPUFF_EXPERT("Sporepuff Expert", "Smoke Jewel 1", 1,
		"Sporepuffs restore 20 health.",
		"Sporepuffs restore 35 health.",
		"Sporepuffs restore 60 health."),
	SPREAD_POWER_SHOTS("Spread/Power Shots", "Spread Jewel 3", 3,
		"Increases the power of spread ammo and power shot arrows."),
	STAMINA_CAP_UP("Stamina Cap Up", "", 0,
		"Increases stamina cap."),
	STAMINA_SURGE("Stamina Surge", "Refresh Jewel 2", 2,
		"Stamina recovery speed +10%",
		"Stamina recovery speed +20%",
		"Stamina recovery speed +30%"),
	STAMINA_THIEF("Stamina Thief", "Drain Jewel 1", 1,
		"Exhaust power +10%",
		"Exhaust power +20%",
		"Exhaust power +30%"),
	STEALTH("Stealth", "Tip Toe Jewel 1", 1,
		"Makes it slightly easier for monsters to lose sight of you.",
		"Makes it moderately easier for monsters to lose sight of you.",
		"Makes it much easier for monsters to lose sight of you."),
	STUN_RESISTANCE("Stun Resistance", "Steadfast Jewel 1", 1,
		"Reduces the duration of stun by 30%.",
		"Reduces the duration of stun by 60%.",
		"Prevents stun."),
	SUPER_RECOVERY("Super Recovery", "", 0,
		"Allows recovery to exceed the red portion of the health gauge."),
	THUNDER_ATTACK("Thunder Attack", "Bolt Jewel 1", 1,
		"Thunder attack +30",
		"Thunder attack +60",
		"Thunder attack +100",
		"Thunder attack +5% Bonus: +100",
		"Thunder attack +10% Bonus: +100"),
	THUNDER_RESISTANCE("Thunder Resistance", "Thunder Res Jewel 1", 1,
		"Thunder resistance +6",
		"Thunder resistance +12",
		"Thunder resistance +20 Defense +10"),
	TOOL_SPECIALIST("Tool Specialist", "", 0,
		"Shortens time until reuse by 5%.",
		"Shortens time until reuse by 10%.",
		"Shortens time until reuse by 20%."),
	TREMOR_RESISTANCE("Tremor Resistance", "Footing Jewel 2", 2,
		"Nullifies minor ground tremors.",
		"Nullifies minor ground tremors and reduces the effects of major ground tremors.",
		"Nullifies minor and major ground tremors."),
	WATER_ATTACK("Water Attack", "Stream Jewel 1", 1,
		"Water attack +30",
		"Water attack +60",
		"Water attack +100",
		"Water attack +5% Bonus: +100",
		"Water attack +10% Bonus: +100"),
	WATER_RESISTANCE("Water Resistance", "Water Res Jewel 1", 1,
		"Water resistance +6",
		"Water resistance +12",
		"Water resistance +20 Defense +10"),
	WEAKNESS_EXPLOIT("Weakness Exploit", "Tenderizer Jewel 2", 2,
		"Attacks that hit weak spots have 15% increased affinity.",
		"Attacks that hit weak spots have 30% increased affinity.",
		"Attacks that hit weak spots have 50% increased affinity."),
	WIDE_RANGE("Wide-Range", "Friendship Jewel 1", 1,
		"Items affect nearby allies with 33% of their efficacy.",
		"Items affect nearby allies in a wider radius with 33% of their efficacy.",
		"Items affect nearby allies in a wider radius with 66% of their efficacy.",
		"Items affect nearby allies in a much wider radius with 66% of their efficacy.",
		"Items affect nearby allies in a wider radius with full efficacy."),
	WINDPROOF("Windproof", "Wind Resist Jewel 2", 2,
		"Slightly reduces the effects of minor wind pressure.",
		"Reduces the effects of minor wind pressure.",
		"Negates minor wind pressure.",
		"Negates minor wind pressure and reduces the effects of major wind pressure.",
		"Negates minor and major wind pressure.");

	public static final Comparator<Skill> NAME_ORDER = (a, b) -> a.getName().compareTo(b.getName());
	public static final Comparator<Skill> DECORATION_AND_NAME_ORDER = (a, b) -> {
		int rv = Integer.compare(a.getDecorationLevel(), b.getDecorationLevel());
		if (rv == 0) {
				rv = a.getName().compareTo(b.getName());
		}
		return rv;
	};

	private String skillName;
	private List<String> descriptions;
	private String decorationName;
	private int decorationLevel;

	private Skill(String name, String decorationName, int decorationLevel, String... descriptions) {
		this.skillName = name;
		this.descriptions = Arrays.asList(descriptions);
		this.decorationName = decorationName;
		this.decorationLevel = decorationLevel;
	}

	public static Skill valueOfName(String skillName) {
		return valueOf(getEnumName(skillName));
	}

	public String getUrlName() {
		return skillName.toLowerCase().replace(" ", "-").replace("/", "-").replace("'", "");
	}

	public static String getEnumName(String skillName) {
		return skillName.toUpperCase().replace(" ", "_").replace("-", "_").replace("/", "_").replace("'", "");
	}

	public int getMaxLevel() {
		return descriptions.size();
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return skillName;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public String getDescription(int i) {
		return descriptions.get(i);
	}

	public String getDecorationName() {
		return decorationName;
	}

	public int getDecorationLevel() {
		return decorationLevel;
	}
}
