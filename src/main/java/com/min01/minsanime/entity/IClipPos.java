package com.min01.minsanime.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface IClipPos 
{
	default boolean shouldClip(Vec3 from, Vec3 to, Entity entity)
	{
		return true;
	}
	
	public Vec3 getClipPos(Vec3 from, Vec3 to, Entity entity);
}