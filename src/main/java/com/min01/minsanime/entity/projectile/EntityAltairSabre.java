package com.min01.minsanime.entity.projectile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class EntityAltairSabre extends ThrowableProjectile
{
	public SabreType type;
	
	public EntityAltairSabre(EntityType<? extends ThrowableProjectile> p_36721_, Level p_36722_)
	{
		super(p_36721_, p_36722_);
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
	public void addAdditionalSaveData(CompoundTag p_36772_) 
	{
		super.addAdditionalSaveData(p_36772_);
		if(this.type != null)
		{
			p_36772_.putInt("SabreType", this.type.ordinal());
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag p_36761_) 
	{
		super.readAdditionalSaveData(p_36761_);
		if(p_36761_.contains("SabreType"))
		{
			this.type = SabreType.values()[p_36761_.getInt("SabreType")];
		}
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.getOwner() != null)
		{
			if(this.type != null)
			{
				Mob owner = (Mob) this.getOwner();
				Entity target = owner.getTarget();
				if(target != null)
				{
					switch(this.type)
					{
					case HOMING:
				    	this.lookAt(Anchor.EYES, target.getEyePosition());
				    	this.shootFromRotation(owner, -this.getXRot(), -this.getYRot(), 0.0F, 1.5F, 1.0F);
						break;
					default:
						break;
					}
				}
				else if(!this.level.isClientSide)
				{
					this.discard();
				}
			}
		}
		else
		{
			this.discard();
		}
		
		if(this.type != SabreType.HOMING && this.tickCount >= 100 && !this.onGround())
		{
			this.type = SabreType.HOMING;
		}
		
		if(this.tickCount >= 300)
		{
			this.discard();
		}
	}
	
	@Override
	public void lookAt(Anchor p_20033_, Vec3 p_20034_) 
	{
		if(this.type == SabreType.HOMING)
		{
			Vec3 vec3 = p_20033_.apply(this);
			double d0 = p_20034_.x - vec3.x;
			double d1 = p_20034_.y - vec3.y;
			double d2 = p_20034_.z - vec3.z;
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			float yRot = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
			this.setXRot(Mth.wrapDegrees((float)((Mth.atan2(d1, d3) * (double)(180.0F / (float)Math.PI)))));
			this.setYRot(AnimeUtil.rotlerp(this.getYRot(), -yRot, 10.0F));
			this.setYHeadRot(this.getYRot());
			this.xRotO = this.getXRot();
			this.yRotO = this.getYRot();
		}
		else
		{
			super.lookAt(p_20033_, p_20034_);
		}
	}
	
	@Override
	protected void updateRotation()
	{
		
	}
	
	@Override
	protected void onHitEntity(EntityHitResult p_36757_)
	{
		Entity entity = p_36757_.getEntity();
		if(this.getOwner() != null)
		{
			if(entity != this.getOwner() && !entity.isAlliedTo(this.getOwner()))
			{
				if(entity.hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 15.0F))
				{
					entity.invulnerableTime = 0;
				}
				//TODO add slash effect
				if(this.type == SabreType.HOMING)
				{
					this.discard();
				}
			}
		}
	}
	
	@Override
	protected void onHitBlock(BlockHitResult p_37258_)
	{
		super.onHitBlock(p_37258_);
		Vec3 vec3 = p_37258_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
		this.setDeltaMovement(vec3);
		Vec3 vec31 = vec3.normalize().scale((double)0.05F);
		this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
		this.setOnGround(true);
		this.setNoGravity(false);
	}
	
	@Override
	public Entity getOwner()
	{
		UUID ownerUUID = ObfuscationReflectionHelper.getPrivateValue(Projectile.class, this, "f_37244_");
		if(ownerUUID != null)
		{
			return AnimeUtil.getEntityByUUID(this.level, ownerUUID);
		}
		return super.getOwner();
	}
	
	public enum SabreType
	{
		HOMING;
	}
	
	public enum SabreAttackType
	{
		CIRCLE,
		AROUND_TARGET,
		RANDOM_POS;
		
		private static final List<SabreAttackType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static SabreAttackType getRandom() 
		{
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
}
