package com.min01.minsanime.particle;

import com.min01.minsanime.MinsAnime;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AnimeParticles 
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MinsAnime.MODID);
	public static final RegistryObject<SimpleParticleType> SPARK = PARTICLES.register("spark", () -> new SimpleParticleType(false));
}
