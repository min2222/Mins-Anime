package com.min01.minsanime.mixin;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;
import com.min01.minsanime.entity.IClipPos;
import com.min01.minsanime.entity.living.EntityAltair;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.LevelEntityGetter;
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
	
	@Override
	public List<Entity> getEntities(Entity p_45936_, AABB p_45937_, Predicate<? super Entity> p_45938_) 
	{
		List<Entity> list = this.getEntitiesOriginal(p_45936_, p_45937_, p_45938_);
		if(p_45936_ != null)
		{
			for(Entity entity : list)
			{
				if(entity instanceof EntityAltair altair)
				{
					if(altair.isHurt())
					{
						altair.aabb = p_45937_;
					}
				}
			}
		}
		return list;
	}
	
	@SuppressWarnings("unused")
	public List<Entity> getEntitiesOriginal(@Nullable Entity p_46536_, AABB p_46537_, Predicate<? super Entity> p_46538_) 
	{
		Level.class.cast(this).getProfiler().incrementCounter("getEntities");
		List<Entity> list = Lists.newArrayList();
		this.getEntities().get(p_46537_, (p_151522_) ->
		{
			if(p_151522_ != p_46536_ && p_46538_.test(p_151522_))
			{
				list.add(p_151522_);
			}
			if(false)
			{
				if(p_151522_ instanceof EnderDragon) 
				{
					for(EnderDragonPart enderdragonpart : ((EnderDragon)p_151522_).getSubEntities())
					{
						if(p_151522_ != p_46536_ && p_46538_.test(enderdragonpart))
						{
							list.add(enderdragonpart);
						}
					}
				}
			}
		});
		for(net.minecraftforge.entity.PartEntity<?> p : Level.class.cast(this).getPartEntities())
		{
			if(p != p_46536_ && p.getBoundingBox().intersects(p_46537_) && p_46538_.test(p)) 
			{
				list.add(p);
			}
		}
		return list;
	}

	@Shadow
	protected abstract LevelEntityGetter<Entity> getEntities();
}
