package com.min01.minsanime.event;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.entity.living.EntityReze;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinsAnime.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler
{
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) 
    {
    	/*event.put(AnimeEntities.ALTAIR.get(), EntityAltair.createAttributes().build());
    	event.put(AnimeEntities.FRIEREN.get(), EntityFrieren.createAttributes().build());
    	event.put(AnimeEntities.ESDEATH.get(), EntityEsdeath.createAttributes().build());*/
    	event.put(AnimeEntities.REZE.get(), EntityReze.createAttributes().build());
    }
}
