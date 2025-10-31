package com.min01.minsanime.misc;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.item.AnimeItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AnimeCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MinsAnime.MODID);
    
    public static final RegistryObject<CreativeModeTab> MINS_ANIME = CREATIVE_MODE_TAB.register("minsanime", () -> CreativeModeTab.builder()
    		.title(Component.translatable("itemGroup.minsanime"))
    		.icon(() -> new ItemStack(AnimeItems.FRIEREN_SPAWN_EGG.get()))
    		.displayItems((enabledFeatures, output) -> 
    		{
    			output.accept(AnimeItems.ALTAIR_SPAWN_EGG.get());
    			output.accept(AnimeItems.FRIEREN_SPAWN_EGG.get());
    			output.accept(AnimeItems.ESDEATH_SPAWN_EGG.get());
    		}).build());
}
