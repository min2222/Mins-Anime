package com.min01.minsanime.entity.projectile;

import java.util.List;

import com.min01.minsanime.entity.AbstractOwnableEntity;
import com.min01.minsanime.entity.EntityCameraShake;
import com.min01.minsanime.entity.living.EntityEsdeath;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityIce extends AbstractOwnableEntity<EntityEsdeath>
{
	public static final EntityDataAccessor<Float> ICE_SIZE = SynchedEntityData.defineId(EntityIce.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ICE_Y_SIZE = SynchedEntityData.defineId(EntityIce.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAX_ICE_Y_SIZE = SynchedEntityData.defineId(EntityIce.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> SCALE_AMOUNT = SynchedEntityData.defineId(EntityIce.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ICE_DAMAGE = SynchedEntityData.defineId(EntityIce.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(EntityIce.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_CAMERA_SHAKE = SynchedEntityData.defineId(EntityIce.class, EntityDataSerializers.BOOLEAN);
	
	public EntityIce(EntityType<?> p_19870_, Level p_19871_)
	{
		super(p_19870_, p_19871_);
		this.noCulling = true;
	}
	
	@Override
	protected void defineSynchedData() 
	{
		super.defineSynchedData();
		this.entityData.define(ICE_SIZE, 1.0F);
		this.entityData.define(ICE_Y_SIZE, 0.0F);
		this.entityData.define(MAX_ICE_Y_SIZE, 1.0F);
		this.entityData.define(SCALE_AMOUNT, 1.0F);
		this.entityData.define(ICE_DAMAGE, 1.0F);
		this.entityData.define(DELAY, 0);
		this.entityData.define(IS_CAMERA_SHAKE, false);
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		
		if(this.tickCount >= this.getDelay())
		{
			if(this.getIceYSize() > 0.0F)
			{
				if(this.getOwner() != null)
				{
					List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.5F), EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(t -> t != this.getOwner() && !t.isAlliedTo(this.getOwner())));
					list.forEach(t -> 
					{
						t.setDeltaMovement(Vec3.ZERO);
						if(t instanceof ServerPlayer player)
						{
							player.connection.send(new ClientboundSetEntityMotionPacket(player));
						}
						if(t.hurt(this.damageSources().freeze(), this.getIceDamage()))
						{
							t.setTicksFrozen(140);
						}
					});
				}
			}
		}
		
		if(this.tickCount >= 200)
		{
			this.doCameraShake();
			if(this.getIceYSize() > 0.0F)
			{
				this.setIceYSize(this.getIceYSize() - this.getScaleAmount());
			}
			else
			{
				this.discard();
			}
		}
		else if(this.tickCount >= this.getDelay())
		{
			this.doCameraShake();
			if(this.getIceYSize() < this.getMaxIceYSize() + this.getDelay())
			{
				this.setIceYSize(this.getIceYSize() + this.getScaleAmount());
			}
		}
	}
	
	public void doCameraShake()
	{
		if(this.isCameraShake())
		{
			EntityCameraShake.cameraShake(this.level, this.position(), 100, 0.005F, 0, 5);
		}
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> p_20059_)
	{
		super.onSyncedDataUpdated(p_20059_);
		if(ICE_SIZE.equals(p_20059_) || ICE_Y_SIZE.equals(p_20059_))
		{
			this.refreshDimensions();
			this.setBoundingBox(this.makeBoundingBox());
		}
	}
	
	@Override
	public boolean canBeCollidedWith() 
	{
		return true;
	}
	
	@Override
	public boolean displayFireAnimation() 
	{
		return false;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag p_37265_)
	{
		super.addAdditionalSaveData(p_37265_);
		p_37265_.putFloat("IceSize", this.getIceSize());
		p_37265_.putFloat("IceYSize", this.getIceYSize());
		p_37265_.putFloat("MaxIceYSize", this.getMaxIceYSize());
		p_37265_.putFloat("ScaleAmount", this.getScaleAmount());
		p_37265_.putFloat("IceDamage", this.getIceDamage());
		p_37265_.putInt("Delay", this.getDelay());
		p_37265_.putBoolean("isCameraShake", this.isCameraShake());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag p_37262_) 
	{
		super.readAdditionalSaveData(p_37262_);
		this.setIceSize(p_37262_.getFloat("IceSize"));
		this.setIceYSize(p_37262_.getFloat("IceYSize"));
		this.setMaxIceYSize(p_37262_.getFloat("MaxIceYSize"));
		this.setScaleAmount(p_37262_.getFloat("ScaleAmount"));
		this.setIceDamage(p_37262_.getFloat("IceDamage"));
		this.setDelay(p_37262_.getInt("Delay"));
		this.setCameraShake(p_37262_.getBoolean("isCameraShake"));
	}
	
	@Override
	protected AABB makeBoundingBox() 
	{
		return this.getIceAABB().move(this.blockPosition());
	}
	
	@Override
	public boolean isPickable()
	{
		return true;
	}
	
	public AABB getIceAABB()
	{
		return new AABB(-this.getIceSize(), 0, -this.getIceSize(), this.getIceSize(), this.getIceYSize(), this.getIceSize());
	}
	
	public void setCameraShake(boolean value)
	{
		this.entityData.set(IS_CAMERA_SHAKE, value);
	}
	
	public boolean isCameraShake()
	{
		return this.entityData.get(IS_CAMERA_SHAKE);
	}
	
	public void setIceDamage(float damage)
	{
		this.entityData.set(ICE_DAMAGE, damage);
	}
	
	public float getIceDamage()
	{
		return this.entityData.get(ICE_DAMAGE);
	}
	
	public void setScaleAmount(float amount)
	{
		this.entityData.set(SCALE_AMOUNT, amount);
	}
	
	public float getScaleAmount()
	{
		return this.entityData.get(SCALE_AMOUNT);
	}
	
	public void setIceSize(float size)
	{
		this.entityData.set(ICE_SIZE, size);
	}
	
	public float getIceSize()
	{
		return this.entityData.get(ICE_SIZE);
	}
	
	public void setMaxIceYSize(float size)
	{
		this.entityData.set(MAX_ICE_Y_SIZE, size);
	}
	
	public float getMaxIceYSize()
	{
		return this.entityData.get(MAX_ICE_Y_SIZE);
	}
	
	public void setIceYSize(float size)
	{
		this.entityData.set(ICE_Y_SIZE, size);
	}
	
	public float getIceYSize()
	{
		return this.entityData.get(ICE_Y_SIZE);
	}
	
	public void setDelay(int delay)
	{
		this.entityData.set(DELAY, delay);
	}
	
	public int getDelay()
	{
		return this.entityData.get(DELAY);
	}
}
