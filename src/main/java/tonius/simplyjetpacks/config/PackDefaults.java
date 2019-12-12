package tonius.simplyjetpacks.config;

import java.util.HashMap;
import java.util.Map;

public class PackDefaults
{
	private static final Map<String, PackDefaults> DEFAULTS = new HashMap<String, PackDefaults>();

	public final Section section;

	// PackBase
	public Integer fuelCapacity;
	public Integer fuelUsage;
	public Integer fuelPerTickIn;
	public Integer fuelPerTickOut;
	public Integer armorReduction;
	public Integer armorFuelPerHit;
	public Integer enchantability;

	// Jetpack
	public Double speedVertical;
	public Double accelVertical;
	public Double speedVerticalHover;
	public Double speedVerticalHoverSlow;
	public Double speedSideways;
	public Double sprintSpeedModifier;
	public Double sprintFuelModifier;
	public Boolean emergencyHoverMode;

	public PackDefaults(String key, String sectionTitle)
	{
		this.section = new Section(false, "Tuning - " + sectionTitle, "tuning." + key);
		DEFAULTS.put(key, this);
	}

	public static PackDefaults get(String key)
	{
		return DEFAULTS.get(key);
	}

	// the great mighty List of Defaults
	static
	{
		// Simply Jetpacks
		PackDefaults d = new PackDefaults("jetpackPotato", "Tuberous Jetpack");
		d.fuelCapacity = 1200;
		d.fuelUsage = 45;
		d.speedVertical = 0.9D;
		d.accelVertical = 0.5D;

		d = new PackDefaults("jetpackCreative", "Creative Jetpack");
		d.fuelCapacity = 200000;
		d.fuelPerTickOut = 50000;
		d.fuelPerTickIn = 0;
		d.armorReduction = 12;
		d.enchantability = 20;
		d.speedVertical = 0.9D;
		d.accelVertical = 0.15D;
		d.speedVerticalHover = 0.45D;
		d.speedVerticalHoverSlow = 0.0D;
		d.speedSideways = 0.21D;
		d.sprintSpeedModifier = 2.5D;
		d.emergencyHoverMode = true;

		d = new PackDefaults("fluxPackCreative", "Creative Flux Pack");
		d.fuelCapacity = 200000;
		d.fuelPerTickOut = 50000;
		d.fuelPerTickIn = 0;
		d.armorReduction = 8;
		d.enchantability = 10;

		/*if(ModType.ENDER_IO.loaded)
		{
			d = new PackDefaults("jetpackEIO1", "Conductive Iron Jetpack (EIO 1)");
			d.fuelCapacity = 80000;
			d.fuelUsage = 32;
			d.fuelPerTickIn = 400;
			d.armorReduction = 5;
			d.armorFuelPerHit = 80;
			d.enchantability = 4;
			d.speedVertical = 0.22D;
			d.accelVertical = 0.1D;
			d.speedVerticalHover = 0.18D;
			d.speedVerticalHoverSlow = 0.14D;
			d.speedSideways = 0.0D;
			d.sprintSpeedModifier = 1.0D;
			d.sprintFuelModifier = 1.0D;
			d.emergencyHoverMode = false;

			d = new PackDefaults("jetpackEIO2", "Electrical Steel Jetpack (EIO 2)");
			d.fuelCapacity = 400000;
			d.fuelUsage = 50;
			d.fuelPerTickIn = 2000;
			d.armorReduction = 6;
			d.armorFuelPerHit = 100;
			d.enchantability = 8;
			d.speedVertical = 0.3D;
			d.accelVertical = 0.12D;
			d.speedVerticalHover = 0.18D;
			d.speedVerticalHoverSlow = 0.1D;
			d.speedSideways = 0.08D;
			d.sprintSpeedModifier = 1.0D;
			d.sprintFuelModifier = 1.0D;
			d.emergencyHoverMode = false;

			d = new PackDefaults("jetpackEIO3", "Energetic Jetpack (EIO 3)");
			d.fuelCapacity = 4000000;
			d.fuelUsage = 200;
			d.fuelPerTickIn = 20000;
			d.armorReduction = 7;
			d.armorFuelPerHit = 120;
			d.enchantability = 13;
			d.speedVertical = 0.48D;
			d.accelVertical = 0.13D;
			d.speedVerticalHover = 0.34D;
			d.speedVerticalHoverSlow = 0.03D;
			d.speedSideways = 0.14D;
			d.sprintSpeedModifier = 1.3D;
			d.sprintFuelModifier = 2.5D;
			d.emergencyHoverMode = true;

			d = new PackDefaults("jetpackEIO4", "Vibrant Jetpack (EIO 4)");
			d.fuelCapacity = 20000000;
			d.fuelUsage = 450;
			d.fuelPerTickIn = 50000;
			d.armorReduction = 8;
			d.armorFuelPerHit = 160;
			d.enchantability = 17;
			d.speedVertical = 0.8D;
			d.accelVertical = 0.14D;
			d.speedVerticalHover = 0.4D;
			d.speedVerticalHoverSlow = 0.005D;
			d.speedSideways = 0.19D;
			d.sprintSpeedModifier = 1.8D;
			d.sprintFuelModifier = 4.0D;
			d.emergencyHoverMode = true;

			d = new PackDefaults("jetpackEIO5", "Dark Soularium JetPlate (EIO 5)");
			d.fuelCapacity = 60000000;
			d.fuelUsage = 850;
			d.fuelPerTickIn = 200000;
			d.fuelPerTickOut = 32000;
			d.armorReduction = 12;
			d.armorFuelPerHit = 240;
			d.enchantability = 20;
			d.speedVertical = 0.9D;
			d.accelVertical = 0.15D;
			d.speedVerticalHover = 0.45D;
			d.speedVerticalHoverSlow = 0.0D;
			d.speedSideways = 0.21D;
			d.sprintSpeedModifier = 2.4D;
			d.sprintFuelModifier = 6.0D;
			d.emergencyHoverMode = true;

			d = new PackDefaults("fluxPackEIO1", "Basic Capacitor Pack (EIO 1)");
			d.fuelCapacity = 800000;
			d.fuelPerTickIn = 800;
			d.fuelPerTickOut = 800;
			d.enchantability = 4;

			d = new PackDefaults("fluxPackEIO2", "Capacitor Pack (EIO 2)");
			d.fuelCapacity = 4000000;
			d.fuelPerTickIn = 4000;
			d.fuelPerTickOut = 4000;
			d.armorReduction = 6;
			d.armorFuelPerHit = 100;
			d.enchantability = 6;

			d = new PackDefaults("fluxPackEIO3", "Vibrant Capacitor Pack (EIO 3)");
			d.fuelCapacity = 20000000;
			d.fuelPerTickIn = 20000;
			d.fuelPerTickOut = 20000;
			d.armorReduction = 7;
			d.armorFuelPerHit = 140;
			d.enchantability = 8;

			d = new PackDefaults("fluxPackEIO4", "Octadic Capacitor Pack (EIO 4)");
			d.fuelCapacity = 80000000;
			d.fuelPerTickIn = 32000;
			d.fuelPerTickOut = 32000;
			d.armorReduction = 7;
			d.armorFuelPerHit = 160;
			d.enchantability = 10;
		}

		if (ModType.THERMAL_EXPANSION.loaded) {
			d = new PackDefaults("jetpackTE1", "Leadstone Jetpack (TE 1)");
			d.fuelCapacity = 800000;
			d.fuelUsage = 32;
			d.fuelPerTickIn = 1500;
			d.armorReduction = 5;
			d.armorFuelPerHit = 80;
			d.enchantability = 4;
			d.speedVertical = 0.22D;
			d.accelVertical = 0.1D;
			d.speedVerticalHover = 0.18D;
			d.speedVerticalHoverSlow = 0.14D;
			d.speedSideways = 0.0D;
			d.sprintSpeedModifier = 1.0D;
			d.sprintFuelModifier = 1.0D;
			d.emergencyHoverMode = false;

			d = new PackDefaults("jetpackTE2", "Hardened Jetpack (TE 2)");
			d.fuelCapacity = 3000000;
			d.fuelUsage = 50;
			d.fuelPerTickIn = 8000;
			d.armorReduction = 6;
			d.armorFuelPerHit = 80;
			d.enchantability = 8;
			d.speedVertical = 0.3D;
			d.accelVertical = 0.12D;
			d.speedVerticalHover = 0.18D;
			d.speedVerticalHoverSlow = 0.1D;
			d.speedSideways = 0.08D;
			d.sprintSpeedModifier = 1.0D;
			d.sprintFuelModifier = 1.0D;
			d.emergencyHoverMode = false;

			d = new PackDefaults("jetpackTE3", "Reinforced Jetpack (TE 3)");
			d.fuelCapacity = 6000000;
			d.fuelUsage = 200;
			d.fuelPerTickIn = 15000;
			d.armorReduction = 7;
			d.armorFuelPerHit = 120;
			d.enchantability = 13;
			d.speedVertical = 0.48D;
			d.accelVertical = 0.13D;
			d.speedVerticalHover = 0.34D;
			d.speedVerticalHoverSlow = 0.03D;
			d.speedSideways = 0.14D;
			d.sprintSpeedModifier = 1.3D;
			d.sprintFuelModifier = 2.5D;
			d.emergencyHoverMode = true;

			d = new PackDefaults("jetpackTE4", "Resonant Jetpack (TE 4)");
			d.fuelCapacity = 25000000;
			d.fuelUsage = 450;
			d.fuelPerTickIn = 20000;
			d.armorReduction = 8;
			d.armorFuelPerHit = 160;
			d.enchantability = 17;
			d.speedVertical = 0.8D;
			d.accelVertical = 0.14D;
			d.speedVerticalHover = 0.4D;
			d.speedVerticalHoverSlow = 0.005D;
			d.speedSideways = 0.19D;
			d.sprintSpeedModifier = 1.8D;
			d.sprintFuelModifier = 4.0D;
			d.emergencyHoverMode = true;

			d = new PackDefaults("jetpackTE5", "Flux-Infused JetPlate (TE 5)");
			d.fuelCapacity = 50000000;
			d.fuelUsage = 850;
			d.fuelPerTickIn = 30000;
			d.fuelPerTickOut = 30000;
			d.armorReduction = 12;
			d.armorFuelPerHit = 240;
			d.enchantability = 20;
			d.speedVertical = 0.9D;
			d.accelVertical = 0.15D;
			d.speedVerticalHover = 0.45D;
			d.speedVerticalHoverSlow = 0.0D;
			d.speedSideways = 0.21D;
			d.sprintSpeedModifier = 2.4D;
			d.sprintFuelModifier = 6.0D;
			d.emergencyHoverMode = true;

			if(ModType.REDSTONE_REPOSITORY.loaded){
				d = new PackDefaults("jetpackTE5enderium", "Flux-Infused JetPlate (Enderium Armored) (TE/RR 5)");
				d.fuelCapacity = 50000000;
				d.fuelUsage = 850;
				d.fuelPerTickIn = 30000;
				d.fuelPerTickOut = 30000;
				d.armorReduction = 12;
				d.armorFuelPerHit = 4500;
				d.enchantability = 20;
				d.speedVertical = 0.9D;
				d.accelVertical = 0.15D;
				d.speedVerticalHover = 0.45D;
				d.speedVerticalHoverSlow = 0.0D;
				d.speedSideways = 0.21D;
				d.sprintSpeedModifier = 2.4D;
				d.sprintFuelModifier = 6.0D;
				d.emergencyHoverMode = true;
			}

			d = new PackDefaults("fluxPackTE1", "Basic Flux Pack (TE 1)");
			d.fuelCapacity = 1500000;
			d.fuelPerTickIn = 800;
			d.fuelPerTickOut = 800;
			d.enchantability = 4;

			d = new PackDefaults("fluxPackTE2", "Reinforced Flux Pack (TE 2)");
			d.fuelCapacity = 12000000;
			d.fuelPerTickIn = 6000;
			d.fuelPerTickOut = 6000;
			d.armorReduction = 6;
			d.armorFuelPerHit = 100;
			d.enchantability = 6;

			d = new PackDefaults("fluxPackTE3", "Resonant Flux Pack (TE 3)");
			d.fuelCapacity = 40000000;
			d.fuelPerTickIn = 20000;
			d.fuelPerTickOut = 20000;
			d.armorReduction = 7;
			d.armorFuelPerHit = 140;
			d.enchantability = 8;
		}*/
	}
}