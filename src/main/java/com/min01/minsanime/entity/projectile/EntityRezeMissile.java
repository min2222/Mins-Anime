package com.min01.minsanime.entity.projectile;

import java.util.List;

import com.min01.minsanime.entity.EntityCameraShake;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityRezeMissile extends ThrowableProjectile
{
	public EntityRezeMissile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_)
	{
		super(p_37466_, p_37467_);
	}

	@Override
	protected void defineSynchedData()
	{
		
	}
	
	@Override
	public boolean displayFireAnimation()
	{
		return false;
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		
		if(this.level.isClientSide)
		{
			this.level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, this.getX(), this.getY() + 0.25F, this.getZ(), 0, 0, 0);
		}
		
		if(this.tickCount >= 150)
		{
			this.discard();
		}
		
		if(this.getOwner() != null)
		{
			if(this.getOwner() instanceof Mob mob && mob.getTarget() != null && mob.getTarget().isAlive())
			{
				this.setDeltaMovement(AnimeUtil.fromToVector(this.position(), mob.getTarget().getEyePosition(), Math.min(this.tickCount / 20.0F, 2.5F)));
			}
		}
	}
	
	@Override
	protected void onHit(HitResult p_37260_)
	{
		super.onHit(p_37260_);
		if(this.getOwner() != null)
		{
			this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.0F);
			EntityCameraShake.cameraShake(this.level, this.position(), 50.0F, 0.05F, 0, 20);
			
        	AnimeShaderEffects.addEffect(this.level, "Explosion", this.position(), 90, 15.0F);
			List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(12.5F), t -> t != this.getOwner() && !t.isAlliedTo(this.getOwner()) && !(t instanceof EntityReze));
			list.forEach(t -> 
			{
				if(t.hurt(this.damageSources().explosion(this.getOwner(), this.getOwner()), 50.0F))
				{
		    		Vec3 motion = AnimeUtil.fromToVector(this.position(), t.position().add(0, 1, 0), 1.5F);
					t.setSecondsOnFire(5);
		    		t.setDeltaMovement(motion.x, motion.y, motion.z);
		    		if(t instanceof ServerPlayer player)
		    		{
		    			player.connection.send(new ClientboundSetEntityMotionPacket(t));
		    		}
				}
			});
			this.discard();
		}
	}
}
