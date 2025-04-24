package com.min01.minsanime.entity;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityAltair;
import com.min01.minsanime.entity.projectile.EntityAltairSabre;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AnimeEntities 
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MinsAnime.MODID);
	
	public static final RegistryObject<EntityType<EntityAltair>> ALTAIR = registerEntity("altair", createBuilder(EntityAltair::new, MobCategory.MONSTER).fireImmune());
	public static final RegistryObject<EntityType<EntityAltairSabre>> ALTAIR_SABRE = registerEntity("altair_sabre", createBuilder(EntityAltairSabre::new, MobCategory.MISC).sized(0.5F, 0.5F));
	
	public static <T extends Entity> EntityType.Builder<T> createBuilder(EntityType.EntityFactory<T> factory, MobCategory category)
	{
		return EntityType.Builder.<T>of(factory, category);
	}
	
	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) 
	{
		return ENTITY_TYPES.register(name, () -> builder.build(new ResourceLocation(MinsAnime.MODID, name).toString()));
	}
}
