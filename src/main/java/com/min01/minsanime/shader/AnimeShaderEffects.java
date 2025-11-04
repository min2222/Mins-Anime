package com.min01.minsanime.shader;

import java.util.ArrayList;
import java.util.List;

import com.min01.minsanime.network.AddShaderEffectPacket;
import com.min01.minsanime.network.AnimeNetwork;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AnimeShaderEffects 
{
	public static final List<ShaderEffect> EFFECTS = new ArrayList<>();
	
	public static void addEffect(Level level, String name, Vec3 pos, int lifeTime, float scale)
	{
		if(!level.isClientSide)
		{
			AnimeNetwork.sendToAll(new AddShaderEffectPacket(name, pos, lifeTime, scale));
		}
	}
	
	public static void tick()
	{
		new ArrayList<>(EFFECTS).forEach(t -> t.tick());
		EFFECTS.removeIf(t -> !t.isAlive());
	}
	
	public static class ShaderEffect
	{
		public final String name;
		public final Vec3 pos;
		public final int lifeTime;
		public final float scale;
		public int tickCount;
		
		public ShaderEffect(String name, Vec3 pos, int lifeTime, float scale) 
		{
			this.name = name;
			this.pos = pos;
			this.lifeTime = lifeTime;
			this.scale = scale;
		}
		
		public void tick()
		{
			this.tickCount++;
		}
		
		public boolean isAlive()
		{
			return this.tickCount < this.lifeTime;
		}
	}
}
