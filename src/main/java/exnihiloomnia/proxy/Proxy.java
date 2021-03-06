package exnihiloomnia.proxy;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.compatibility.ENOCompatibility;
import exnihiloomnia.compatibility.ENOOres;
import exnihiloomnia.crafting.ENOCrafting;
import exnihiloomnia.entities.ENOEntities;
import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.items.ENOBucketHandler;
import exnihiloomnia.items.ENOFuelHandler;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.materials.ENOToolMaterials;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.world.ENOWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

//Commands that execute on both the client and the server.
public abstract class Proxy {

	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(ENO.INSTANCE);

		ENO.path = event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "ExNihiloOmnia" + File.separator;
		ENO.config = new Configuration(new File(ENO.path + "ExNihiloOmnia.cfg"));

		ENO.config.load();
		ENOConfig.configure(ENO.config);
		ENO.oreList = ENOOres.getActiveOres();

		ENOFluids.register();
		ENOToolMaterials.configure();
		ENOBlocks.init();
		ENOItems.init();

		ENOCrafting.configure(ENO.config);
		ENOWorld.configure(ENO.config);
		ENORegistries.configure(ENO.config);

		ENOEntities.configure();
		ENOCompatibility.configure(ENO.config);

		if(ENO.config.hasChanged())
			ENO.config.save();

		MinecraftForge.EVENT_BUS.register(new ENOBucketHandler());
		ENOCompatibility.preInit();
	}

	public void init(FMLInitializationEvent event) {

		ENOCrafting.registerRecipes();
		ENORegistries.initialize();
		ENOCompatibility.init();

		ENOOres.addCrafting();
		ENOOres.addSmelting();

		ENOWorld.registerWorldProviders();

		GameRegistry.registerFuelHandler(new ENOFuelHandler());
		BarrelStates.configure(ENO.config);
	}

	public void postInit(FMLPostInitializationEvent event) {
		ENOOres.addOreDict();
	}
}
