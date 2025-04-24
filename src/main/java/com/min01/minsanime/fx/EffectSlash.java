package com.min01.minsanime.fx;

import net.minecraft.nbt.CompoundTag;

public class EffectSlash 
{
	public float size;
	public float maxSize;
	public float angle;
	
	public EffectSlash(float size, float maxSize, float angle) 
	{
		this.size = size;
		this.maxSize = maxSize;
		this.angle = angle;
	}
	
	public CompoundTag save()
	{
		CompoundTag tag = new CompoundTag();
		tag.putFloat("Size", this.size);
		tag.putFloat("MaxSize", this.maxSize);
		tag.putFloat("Angle", this.angle);
		return tag;
	}
	
	public static EffectSlash read(CompoundTag tag)
	{
		if(tag.contains("Size") && tag.contains("MaxSize") && tag.contains("Angle"))
		{
			return new EffectSlash(tag.getFloat("Size"), tag.getFloat("MaxSize"), tag.getFloat("Angle"));
		}
		return new EffectSlash(0, 0, 0);
	}
}
