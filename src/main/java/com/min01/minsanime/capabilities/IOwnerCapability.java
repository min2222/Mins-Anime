package com.min01.minsanime.capabilities;

import com.min01.minsanime.MinsAnime;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IOwnerCapability extends INBTSerializable<CompoundTag>
{
	ResourceLocation ID = new ResourceLocation(MinsAnime.MODID, "owner");

	void setEntity(Entity entity);
	
	void setOwner(LivingEntity entity);
	
	Entity getOwner();
}
