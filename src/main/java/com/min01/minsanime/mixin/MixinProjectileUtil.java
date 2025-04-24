package com.min01.minsanime.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.minsanime.entity.projectile.EntityAltairSabre;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(ProjectileUtil.class)
public class MixinProjectileUtil 
{
	@Inject(at = @At(value = "HEAD"), method = "getHitResult", cancellable = true)
	private static void getHitResult(Vec3 p_278237_, Entity p_278320_, Predicate<Entity> p_278257_, Vec3 p_278342_, Level p_278321_, CallbackInfoReturnable<HitResult> cir)
	{
		if(p_278320_ instanceof EntityAltairSabre || p_278320_ instanceof Player)
		{
			cir.cancel();
			Vec3 vec3 = p_278237_.add(p_278342_);
			HitResult hitresult = AnimeUtil.clip(p_278321_, new ClipContext(p_278237_, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p_278320_));
			if(hitresult.getType() != HitResult.Type.MISS) 
			{
				vec3 = hitresult.getLocation();
			}
			HitResult hitresult1 = ProjectileUtil.getEntityHitResult(p_278321_, p_278320_, p_278237_, vec3, p_278320_.getBoundingBox().expandTowards(p_278342_).inflate(1.0D), p_278257_);
			if(hitresult1 != null) 
			{
				hitresult = hitresult1;
			}
			cir.setReturnValue(hitresult);
		}
	}
}
