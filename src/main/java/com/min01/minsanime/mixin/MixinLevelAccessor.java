package com.min01.minsanime.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import com.min01.minsanime.entity.IClipPos;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

@Mixin(LevelAccessor.class)
public interface MixinLevelAccessor extends BlockGetter
{
	@Override
	default BlockHitResult clip(ClipContext ctx) 
	{
	    BlockHitResult hitResult = BlockGetter.super.clip(ctx);
		return this.doClip(hitResult, ctx);
	}
	
	default BlockHitResult doClip(BlockHitResult hitResult, ClipContext ctx)
	{
		if(ctx.collisionContext instanceof EntityCollisionContext entityCtx)
		{
			Entity ctxEntity = entityCtx.getEntity();
			if(ctxEntity != null)
			{
				if(!(ctxEntity instanceof LivingEntity))
				{
				    Vec3 from = ctx.getFrom();
				    Vec3 to = ctx.getTo();
				    AABB aabb = new AABB(from, to).inflate(1.0F);
				    List<Entity> entities = Level.class.cast(this).getEntitiesOfClass(Entity.class, aabb, t -> t instanceof IClipPos clip && clip.shouldClip(from, to, ctxEntity));
					for(Entity entity : entities)
					{
						return new BlockHitResult(((IClipPos) entity).getClipPos(from, to, ctxEntity), hitResult.getDirection(), hitResult.getBlockPos(), hitResult.isInside());
					}
				}
			}
		}
		return hitResult;
	}
}
