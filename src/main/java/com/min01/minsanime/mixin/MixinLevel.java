package com.min01.minsanime.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import com.min01.minsanime.entity.IClipPos;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(Level.class)
public abstract class MixinLevel implements LevelAccessor
{
	@Override
	public BlockHitResult clip(ClipContext ctx) 
	{
	    BlockHitResult hitResult = AnimeUtil.clip(Level.class.cast(this), ctx);
	    Vec3 from = ctx.getFrom();
	    Vec3 to = ctx.getTo();
	    AABB aabb = new AABB(from, to).inflate(1.0F);
	    List<Entity> entities = Level.class.cast(this).getEntitiesOfClass(Entity.class, aabb, t -> t instanceof IClipPos);
		for(Entity entity : entities)
		{
			if(entity instanceof IClipPos clip)
			{
				if(clip.shouldClip())
				{
					return new BlockHitResult(clip.getClipPos(from, to), hitResult.getDirection(), hitResult.getBlockPos(), hitResult.isInside());
				}
			}
		}
		return hitResult;
	}
}
