package com.min01.minsanime.entity.living;

import com.min01.minsanime.entity.AbstractAnimatableMonster;
import com.min01.minsanime.entity.IClipPos;
import com.min01.minsanime.entity.ai.control.HoveringMoveControl;
import com.min01.minsanime.entity.ai.goal.AltairHolopsiconGoal;
import com.min01.minsanime.entity.ai.goal.AltairSabreAttackGoal;
import com.min01.minsanime.entity.ai.goal.AltairSummonSabreGoal;
import com.min01.minsanime.entity.projectile.EntityAltairSabre;
import com.min01.minsanime.misc.SmoothAnimationState;
import com.min01.minsanime.particle.AnimeParticles;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

//https://www.youtube.com/watch?v=c5jWzDKG-OE
public class EntityAltair extends AbstractAnimatableMonster implements IClipPos
{
	public static final int MAX_SABRE_NUMBER = 32;
	
	public static final EntityDataAccessor<Integer> SABRE_COUNT = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_HOLOPSICON = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_FLYING = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_LAST_HURT = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> TOTAL_DAMAGE = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.FLOAT);

	public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState floatingAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState holopsiconAnimationState = new SmoothAnimationState();
	public final AnimationState holopsiconOBJAnimationState = new AnimationState();
	public final AnimationState holopsiconUVAnimationState = new AnimationState();
	
	public Entity clipEntity;
	
	public EntityAltair(EntityType<? extends Monster> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 200.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.55D)
    			.add(Attributes.FLYING_SPEED, 0.55D)
        		.add(Attributes.ARMOR, 30.0D)
        		.add(Attributes.ARMOR_TOUGHNESS, 30.0D)
        		.add(Attributes.FOLLOW_RANGE, 50.0D)
        		.add(Attributes.KNOCKBACK_RESISTANCE, 5.0D);
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(SABRE_COUNT, 0);
    	this.entityData.define(IS_HOLOPSICON, false);
    	this.entityData.define(IS_FLYING, false);
    	this.entityData.define(IS_LAST_HURT, false);
    	this.entityData.define(TOTAL_DAMAGE, 0.0F);
    }
    
    @Override
    protected void registerGoals() 
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(4, new AltairSabreAttackGoal(this));
    	this.goalSelector.addGoal(0, new AltairSummonSabreGoal(this));
    	this.goalSelector.addGoal(0, new AltairHolopsiconGoal(this));
    	this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }
    
    @Override
    public void travel(Vec3 p_20818_)
    {
    	if(this.isFlying())
    	{
            if(this.isControlledByLocalInstance()) 
            {
            	if(this.isInWater()) 
            	{
            		this.moveRelative(0.02F, p_20818_);
            		this.move(MoverType.SELF, this.getDeltaMovement());
            		this.setDeltaMovement(this.getDeltaMovement().scale((double)0.8F));
                } 
            	else if(this.isInLava())
                {
            		this.moveRelative(0.02F, p_20818_);
            		this.move(MoverType.SELF, this.getDeltaMovement());
            		this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                } 
                else
                {
                	BlockPos ground = getBlockPosBelowThatAffectsMyMovement();
                	float f = 0.91F;
                	if(this.onGround())
                	{
                		f = this.level.getBlockState(ground).getFriction(this.level, ground, this) * 0.91F;
                	}
    	
                	float f1 = 0.16277137F / (f * f * f);
                	f = 0.91F;
                	if(this.onGround()) 
                	{
                		f = this.level.getBlockState(ground).getFriction(this.level, ground, this) * 0.91F;
                	}
                	this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, p_20818_);
                	this.move(MoverType.SELF, this.getDeltaMovement());
                	this.setDeltaMovement(this.getDeltaMovement().scale((double)f));
                }
            }
            this.calculateEntityAnimation(false);
    	}
    	else
    	{
    		super.travel(p_20818_);
    	}
    }
    
    @Override
    public void tick() 
    {
    	super.tick();
    	this.resetFallDistance();
    	
    	if(this.level.isClientSide)
    	{
    		this.idleAnimationState.updateWhen(!this.isFlying(), this.tickCount);
    		this.floatingAnimationState.updateWhen(this.isFlying(), this.tickCount);
    		this.holopsiconAnimationState.updateWhen(this.isUsingSkill(1), this.tickCount);
    		this.holopsiconOBJAnimationState.animateWhen(this.isHolopsicon() && this.isUsingSkill(1), this.tickCount);
    		this.holopsiconUVAnimationState.startIfStopped(this.tickCount);
    	}
    	
    	if(this.getTarget() != null)
    	{
    		if(this.canLook())
    		{
        		this.getLookControl().setLookAt(this.getTarget(), 30.0F, 30.0F);
    		}
    		if(this.canMove() && this.distanceTo(this.getTarget()) >= 8.0F)
    		{
				if(this.isFlying())
				{
					Vec3 vec3 = this.getTarget().position();
					this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 0.8F);
				}
				else
				{
					this.getNavigation().moveTo(this.getTarget(), 0.5F);
				}
    		}
			if(!this.isUsingSkill())
			{
				BlockPos groundPos = AnimeUtil.getGroundPos(this.level, this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ());
				if(this.getTarget().getY() <= groundPos.getY() + 3 && this.isFlying())
				{
					this.setFlying(false);
				}				
				if(this.getTarget().getY() > groundPos.getY() + 3 && !this.isFlying())
				{
					this.setFlying(true);
				}
			}
    	}
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_)
    {
    	if(!p_21016_.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
    	{
    		if(this.clipEntity == p_21016_.getDirectEntity())
    		{
    			return false;
    		}
    		if(this.isHolopsicon())
    		{
    			return false;
    		}
    		this.setTotalDamage(this.getTotalDamage() + p_21017_);
    	}
    	return super.hurt(p_21016_, p_21017_);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putInt("SabreCount", this.getSabreCount());
    	p_21484_.putBoolean("isHolopsicon", this.isHolopsicon());
    	p_21484_.putFloat("TotalDamage", this.getTotalDamage());
    	p_21484_.putBoolean("isFlying", this.isFlying());
    	p_21484_.putBoolean("isLastHurt", this.isLastHurt());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_)
    {
    	super.readAdditionalSaveData(p_21450_);
		this.setSabreCount(p_21450_.getInt("SabreCount"));
		this.setHolopsicon(p_21450_.getBoolean("isHolopsicon"));
		this.setTotalDamage(p_21450_.getFloat("TotalDamage"));
    	this.setFlying(p_21450_.getBoolean("isFlying"));
    	this.setLastHurt(p_21450_.getBoolean("isLastHurt"));
    }
    
    @Override
    public boolean shouldClip(Vec3 from, Vec3 to, Entity entity) 
    {
    	if(this.hasSabre())
    	{
    		AABB aabb = new AABB(from, to).inflate(1.0F);
			return this.getBoundingBox().inflate(1.5F).intersects(aabb) && !(entity instanceof EntityAltairSabre);
    	}
    	return false;
    }
    
    @Override
    public Vec3 getClipPos(Vec3 from, Vec3 to, Entity entity)
    {
    	Vec3 motion = AnimeUtil.fromToVector(from, to, (float) from.distanceTo(this.getEyePosition()) - 1.5F);
    	Vec3 pos = from.add(motion);
    	this.addSparks(pos);
    	this.clipEntity = entity;
    	return pos;
    }
    
    public void addSparks(Vec3 pos)
    {
		for(int i = 0; i < 5; i++)
		{
            double speed1 = this.random.nextGaussian() * 1.0F;
            double speed2 = this.random.nextGaussian() * 1.0F;
            double speed3 = this.random.nextGaussian() * 1.0F;
			this.level.addAlwaysVisibleParticle(AnimeParticles.SPARK.get(), pos.x, pos.y, pos.z, speed1, speed2, speed3);
		}
    }
    
    public void switchControl(boolean isFlying)
    {
    	if(isFlying)
    	{
    		this.moveControl = new HoveringMoveControl(this);
    	}
    	else
    	{
    		this.moveControl = new MoveControl(this);
    	}
    }
    
    public void setLastHurt(boolean value)
    {
    	this.entityData.set(IS_LAST_HURT, value);
    }
    
    public boolean isLastHurt() 
    {
    	return this.entityData.get(IS_LAST_HURT);
    }
    
    public void setFlying(boolean value)
    {
		this.switchControl(value);
    	this.entityData.set(IS_FLYING, value);
    }
    
    public boolean isFlying() 
    {
    	return this.entityData.get(IS_FLYING);
    }
    
    public static enum HolopsiconMovement
    {
    	//https://namu.wiki/w/%EA%B5%B0%EB%B3%B5%20%EA%B3%B5%EC%A3%BC
    	NO20, //summon same entity as enemy;
    	NO14, //reverse damage, damage to target and heal self, also transfer bad effects to enemy;
    	NO13; //clean every effects from enemy;
    }
    
    public boolean hasSabre()
    {
    	return this.getSabreCount() > 0;
    }
    
    public void setTotalDamage(float value)
    {
    	this.entityData.set(TOTAL_DAMAGE, value);
    }
    
    public float getTotalDamage()
    {
    	return this.entityData.get(TOTAL_DAMAGE);
    }
    
    public void setHolopsicon(boolean value)
    {
    	this.entityData.set(IS_HOLOPSICON, value);
    }
    
    public boolean isHolopsicon()
    {
    	return this.entityData.get(IS_HOLOPSICON);
    }
    
    public void setSabreCount(int value)
    {
    	this.entityData.set(SABRE_COUNT, value);
    }
    
    public int getSabreCount()
    {
    	return this.entityData.get(SABRE_COUNT);
    }
}
