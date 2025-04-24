package com.min01.minsanime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.minsanime.entity.living.EntityAltair;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity
{
	public MixinLivingEntity(EntityType<?> p_19870_, Level p_19871_)
	{
		super(p_19870_, p_19871_);
	}

	@Inject(at = @At(value = "HEAD"), method = "hasLineOfSight", cancellable = true)
	private void hasLineOfSight(Entity p_147185_, CallbackInfoReturnable<Boolean> cir)
	{
		if((LivingEntity.class.cast(this) instanceof Mob mob && mob.getTarget() instanceof EntityAltair) || LivingEntity.class.cast(this) instanceof EntityAltair)
		{
			cir.cancel();
			if(p_147185_.level() != this.level())
			{
				cir.setReturnValue(false);
			}
			else
			{
				Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
				Vec3 vec31 = new Vec3(p_147185_.getX(), p_147185_.getEyeY(), p_147185_.getZ());
				if(vec31.distanceTo(vec3) > 128.0D)
				{
					cir.setReturnValue(false);
				} 
				else
				{
					cir.setReturnValue(AnimeUtil.clip(this.level(), new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS);
				}
			}
		}
	}
}
