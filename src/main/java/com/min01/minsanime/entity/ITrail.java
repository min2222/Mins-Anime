package com.min01.minsanime.entity;

import net.minecraft.world.phys.Vec3;

public interface ITrail 
{
	Vec3 getTrailPosition(int pointer, float partialTick);
}
