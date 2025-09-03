package com.min01.minsanime.misc;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.entity.Entity;

@SuppressWarnings("removal")
public class AnimeSecurityManager extends SecurityManager
{
	public static final Map<Integer, Entity> ENTITY_MAP = new HashMap<>();
	public static final Map<Integer, Entity> ENTITY_MAP2 = new HashMap<>();
	
	public Class<?>[] getContext()
	{
		return this.getClassContext();
	}
	
	public static void addEntities(Entity entity)
	{
		ENTITY_MAP.put(entity.getClass().hashCode(), entity);
		ENTITY_MAP2.put(entity.getClass().getSuperclass().hashCode(), entity);
	}
	
	public Entity getCallerEntity(Class<?> clazz)
	{
		if(ENTITY_MAP.containsKey(clazz.hashCode()))
		{
			Entity entity = ENTITY_MAP.get(clazz.hashCode());
			if(entity != null && !entity.isAlive())
			{
				ENTITY_MAP.remove(clazz.hashCode(), entity);
			}
			return entity;
		}
		else if(ENTITY_MAP2.containsKey(clazz.hashCode()))
		{
			Entity entity = ENTITY_MAP2.get(clazz.hashCode());
			if(entity != null && !entity.isAlive())
			{
				ENTITY_MAP2.remove(clazz.hashCode(), entity);
			}
			return entity;
		}
		return null;
	}
}
