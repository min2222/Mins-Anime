package com.min01.minsanime;

import com.min01.minsanime.capabilities.AnimeCapabilities;
import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.item.AnimeItems;
import com.min01.minsanime.misc.AnimeCreativeTabs;
import com.min01.minsanime.misc.AnimeEntityDataSerializers;
import com.min01.minsanime.network.AnimeNetwork;
import com.min01.minsanime.particle.AnimeParticles;
import com.min01.minsanime.sound.AnimeSounds;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinsAnime.MODID)
public class MinsAnime 
{
	public static final String MODID = "minsanime";
	
	public MinsAnime() 
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		AnimeEntities.ENTITY_TYPES.register(bus);
		AnimeItems.ITEMS.register(bus);
		AnimeEntityDataSerializers.SERIALIZERS.register(bus);
		AnimeParticles.PARTICLES.register(bus);
		AnimeSounds.SOUNDS.register(bus);
		AnimeCreativeTabs.CREATIVE_MODE_TAB.register(bus);
		
		AnimeNetwork.registerMessages();
		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, AnimeCapabilities::attachEntityCapability);
	}
}
