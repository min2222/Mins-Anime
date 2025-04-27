package com.min01.minsanime.event;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinsAnime.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge
{
	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
		LivingEntity living = event.getEntity();
		if(AnimeUtil.getOwner(living) != null)
		{
			if(living.tickCount >= 500 || !AnimeUtil.getOwner(living).isAlive())
			{
				living.discard();
			}
		}
	}
}
