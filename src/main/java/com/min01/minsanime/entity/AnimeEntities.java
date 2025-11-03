package com.min01.minsanime.entity;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityAltair;
import com.min01.minsanime.entity.living.EntityEsdeath;
import com.min01.minsanime.entity.living.EntityFrieren;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.entity.projectile.EntityAltairSabre;
import com.min01.minsanime.entity.projectile.EntityIce;

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
	
	public static final RegistryObject<EntityType<Entity>> CAMERA_SHAKE = registerEntity("camera_shake", createBuilder(EntityCameraShake::new, MobCategory.MISC).sized(0.0F, 0.0F).updateInterval(1).setShouldReceiveVelocityUpdates(true).setTrackingRange(100).clientTrackingRange(100));
	
	public static final RegistryObject<EntityType<EntityAltair>> ALTAIR = registerEntity("altair", createBuilder(EntityAltair::new, MobCategory.MONSTER).fireImmune());
	public static final RegistryObject<EntityType<EntityAltairSabre>> ALTAIR_SABRE = registerEntity("altair_sabre", createBuilder(EntityAltairSabre::new, MobCategory.MISC).sized(0.5F, 0.5F));

	public static final RegistryObject<EntityType<EntityFrieren>> FRIEREN = registerEntity("frieren", createBuilder(EntityFrieren::new, MobCategory.CREATURE).fireImmune());
	
	public static final RegistryObject<EntityType<EntityEsdeath>> ESDEATH = registerEntity("esdeath", createBuilder(EntityEsdeath::new, MobCategory.MONSTER).fireImmune());
	public static final RegistryObject<EntityType<EntityIce>> ICE = registerEntity("ice", createBuilder(EntityIce::new, MobCategory.MISC));

	public static final RegistryObject<EntityType<EntityReze>> REZE = registerEntity("reze", createBuilder(EntityReze::new, MobCategory.MONSTER).fireImmune());
	
	public static <T extends Entity> EntityType.Builder<T> createBuilder(EntityType.EntityFactory<T> factory, MobCategory category)
	{
		return EntityType.Builder.<T>of(factory, category);
	}
	
	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) 
	{
		return ENTITY_TYPES.register(name, () -> builder.build(new ResourceLocation(MinsAnime.MODID, name).toString()));
	}
}
