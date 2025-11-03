package com.min01.minsanime.shader;

import java.util.ArrayList;
import java.util.List;

import com.min01.minsanime.util.AnimeClientUtil;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AnimeShaderEffects 
{
	public static final List<ShaderEffect> EFFECTS = new ArrayList<>();
	
	public static void addEffect(Level level, String name, Vec3 pos, int lifeTime)
	{
		if(level.isClientSide)
		{
			EFFECTS.add(new ShaderEffect(name, pos, lifeTime));
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
		public int tickCount;
		
		public ShaderEffect(String name, Vec3 pos, int lifeTime) 
		{
			this.name = name;
			this.pos = pos;
			this.lifeTime = lifeTime;
		}
		
		@OnlyIn(Dist.CLIENT)
		public void tick()
		{
			if(!AnimeClientUtil.MC.isPaused())
			{
				this.tickCount++;
			}
		}
		
		public boolean isAlive()
		{
			return this.tickCount < this.lifeTime;
		}
	}
}
