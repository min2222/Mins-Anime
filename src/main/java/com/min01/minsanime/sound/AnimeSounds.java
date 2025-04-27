package com.min01.minsanime.sound;

import com.min01.minsanime.MinsAnime;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AnimeSounds
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MinsAnime.MODID);
	public static final RegistryObject<SoundEvent> HOLOPSICON = registerSound("holopsicon");
	
	private static RegistryObject<SoundEvent> registerSound(String name) 
	{
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MinsAnime.MODID, name)));
    }
}
