package com.min01.minsanime.item;

import java.util.function.Supplier;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.AnimeEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AnimeItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MinsAnime.MODID);
	
	public static final RegistryObject<Item> ALTAIR_SPAWN_EGG = registerSpawnEgg("altair_spawn_egg", () -> AnimeEntities.ALTAIR.get(), 3026486, 12958088);
	public static final RegistryObject<Item> FRIEREN_SPAWN_EGG = registerSpawnEgg("frieren_spawn_egg", () -> AnimeEntities.FRIEREN.get(), 16514043, 12422219);
	public static final RegistryObject<Item> ESDEATH_SPAWN_EGG = registerSpawnEgg("esdeath_spawn_egg", () -> AnimeEntities.ESDEATH.get(), 14343647, 9085884);
	public static final RegistryObject<Item> REZE_SPAWN_EGG = registerSpawnEgg("reze_spawn_egg", () -> AnimeEntities.REZE.get(), 16184554, 3809598);
	
	public static <T extends Mob> RegistryObject<Item> registerSpawnEgg(String name, Supplier<EntityType<T>> entity, int color1, int color2) 
	{
		return ITEMS.register(name, () -> new ForgeSpawnEggItem(entity, color1, color2, new Item.Properties()));
	}
}
