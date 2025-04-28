package com.min01.minsanime.entity.living;

import com.min01.minsanime.entity.AbstractAnimatableMonster;
import com.min01.minsanime.entity.IClipPos;
import com.min01.minsanime.entity.ai.control.FlyingMoveControl;
import com.min01.minsanime.entity.ai.goal.AltairHolopsiconGoal;
import com.min01.minsanime.entity.ai.goal.AltairSabreAttackGoal;
import com.min01.minsanime.entity.ai.goal.AltairSummonSabreGoal;
import com.min01.minsanime.particle.AnimeParticles;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

//https://www.youtube.com/watch?v=c5jWzDKG-OE
public class EntityAltair extends AbstractAnimatableMonster implements IClipPos
{
	public static final int MAX_SABRE_NUMBER = 32;
	
	public static final EntityDataAccessor<Integer> SABRE_COUNT = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_HOLOPSICON = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_HURT = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> HURT = SynchedEntityData.defineId(EntityAltair.class, EntityDataSerializers.FLOAT);

	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState holopsiconAnimationState = new AnimationState();
	public final AnimationState holopsiconOBJAnimationState = new AnimationState();
	public final AnimationState holopsiconUVAnimationState = new AnimationState();
	
	public AABB aabb;
	
	public EntityAltair(EntityType<? extends Monster> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
        this.moveControl = new FlyingMoveControl(this);
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
    	this.entityData.define(IS_HURT, false);
    	this.entityData.define(HURT, 0.0F);
    }
    
    @Override
    protected void registerGoals() 
    {
    	this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    	this.goalSelector.addGoal(4, new AltairSabreAttackGoal(this));
    	this.goalSelector.addGoal(0, new AltairSummonSabreGoal(this));
    	this.goalSelector.addGoal(0, new AltairHolopsiconGoal(this));
    	this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }
    
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> p_219422_) 
	{
        if(ANIMATION_STATE.equals(p_219422_) && this.level.isClientSide) 
        {
            switch(this.getAnimationState()) 
            {
        		case 0: 
        		{
        			this.stopAllAnimationStates();
        			break;
        		}
        		case 1: 
        		{
        			this.stopAllAnimationStates();
        			this.holopsiconAnimationState.start(this.tickCount);
        			break;
        		}
            }
        }
	}
	
	@Override
	public void stopAllAnimationStates() 
	{
		this.holopsiconAnimationState.stop();
	}
    
    @Override
    public void travel(Vec3 p_20818_)
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
    
    @Override
    public void tick() 
    {
    	super.tick();
    	this.resetFallDistance();
    	if(this.getTarget() != null)
    	{
    		this.lookAt(Anchor.EYES, this.getTarget().getEyePosition());
    		if(this.distanceTo(this.getTarget()) >= 12.0F)
    		{
    			Vec3 motion = AnimeUtil.fromToVector(AnimeUtil.getSpreadPosition(this.level, this.position(), 8), this.getTarget().position(), 0.25F);
    			this.setDeltaMovement(this.getDeltaMovement().add(motion));
    		}
    		if(this.distanceTo(this.getTarget()) <= 8.0F)
    		{
    			Vec3 motion = AnimeUtil.fromToVector(this.getTarget().position(), AnimeUtil.getSpreadPosition(this.level, this.position(), 8), 0.25F);
    			this.setDeltaMovement(this.getDeltaMovement().add(motion));
    		}
    		if(this.getY() >= this.getTarget().getY() + 6)
    		{
    			Vec3 motion = AnimeUtil.fromToVector(this.getTarget().position(), AnimeUtil.getSpreadPosition(this.level, this.position(), 8), 0.25F);
    			this.setDeltaMovement(this.getDeltaMovement().add(new Vec3(motion.x, -0.5F, motion.z)));
    		}
    	}
    	
    	if(this.level.isClientSide)
    	{
    		this.idleAnimationState.startIfStopped(this.tickCount);
    		this.holopsiconOBJAnimationState.animateWhen(this.isHolopsicon(), this.tickCount);
    		this.holopsiconUVAnimationState.startIfStopped(this.tickCount);
    	}
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_)
    {
    	if(!p_21016_.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
    	{
    		if(this.hasSabre())
    		{
        		this.setHurt(true);
    			AABB aabb = this.getBoundingBox().inflate(1.5F);
    			Entity entity = p_21016_.getDirectEntity();
        		if(entity != null)
        		{
        			if(aabb.intersects(entity.getBoundingBox().inflate(1.5F)))
        			{
        	    		for(int i = 0; i < 5; i++)
        	    		{
            				Vec3 pos = this.getClipPos(entity.getBoundingBox().getCenter(), this.getBoundingBox().getCenter());
        	                double speed1 = this.random.nextGaussian() * 1.0F;
        	                double speed2 = this.random.nextGaussian() * 1.0F;
        	                double speed3 = this.random.nextGaussian() * 1.0F; //speed;
        	    			this.level.addAlwaysVisibleParticle(AnimeParticles.SPARK.get(), pos.x, pos.y, pos.z, speed1, speed2, speed3);
        	    		}
        	    		return false;
        			}
        			else if(this.aabb != null)
        			{
            			if(aabb.intersects(this.aabb))
            			{
            	    		for(int i = 0; i < 5; i++)
            	    		{
                				Vec3 pos = this.getClipPos(this.aabb.getCenter(), this.getBoundingBox().getCenter());
            	                double speed1 = this.random.nextGaussian() * 1.0F;
            	                double speed2 = this.random.nextGaussian() * 1.0F;
            	                double speed3 = this.random.nextGaussian() * 1.0F; //speed;
            	    			this.level.addAlwaysVisibleParticle(AnimeParticles.SPARK.get(), pos.x, pos.y, pos.z, speed1, speed2, speed3);
            	    		}
            	    		this.aabb = null;
            	    		this.setHurt(false);
            	    		return false;
            			}
        			}
        		}
    		}
    		if(this.isHolopsicon())
    		{
    			return false;
    		}
    	}
		this.setHurt(this.getHurt() + p_21017_);
    	return super.hurt(p_21016_, p_21017_);
    }
    
    @Override
    public boolean causeFallDamage(float p_147105_, float p_147106_, DamageSource p_147107_)
    {
    	return false;
    }

    @Override
    protected void checkFallDamage(double p_20809_, boolean p_20810_, BlockState p_20811_, BlockPos p_20812_) 
    {
    	
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putInt("SabreCount", this.getSabreCount());
    	p_21484_.putBoolean("isHolopsicon", this.isHolopsicon());
    	p_21484_.putBoolean("isHurt", this.isHurt());
    	p_21484_.putFloat("Hurt", this.getHurt());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_)
    {
    	super.readAdditionalSaveData(p_21450_);
    	if(p_21450_.contains("SabreCount"))
    	{
    		this.setSabreCount(p_21450_.getInt("SabreCount"));
    	}
    	if(p_21450_.contains("isHolopsicon"))
    	{
    		this.setHolopsicon(p_21450_.getBoolean("isHolopsicon"));
    	}
    	if(p_21450_.contains("isHurt"))
    	{
    		this.setHurt(p_21450_.getBoolean("isHurt"));
    	}
    	if(p_21450_.contains("Hurt"))
    	{
    		this.setHurt(p_21450_.getFloat("Hurt"));
    	}
    }
    
    @Override
    public boolean shouldClip() 
    {
    	return this.hasSabre();
    }
    
    @Override
    public Vec3 getClipPos(Vec3 from, Vec3 to)
    {
    	Vec3 motion = AnimeUtil.fromToVector(from, to, (float) from.distanceTo(this.getEyePosition()) - 1.5F);
    	Vec3 pos = from.add(motion);
    	if(!this.level.isClientSide)
    	{
    		for(int i = 0; i < 5; i++)
    		{
                double speed1 = this.random.nextGaussian() * 1.0F;
                double speed2 = this.random.nextGaussian() * 1.0F;
                double speed3 = this.random.nextGaussian() * 1.0F; //speed;
    			((ServerLevel)this.level).sendParticles(AnimeParticles.SPARK.get(), pos.x, pos.y, pos.z, 1, speed1, speed2, speed3, 1.0F);
    		}
    	}
    	return pos;
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
    
    public void setHurt(float value)
    {
    	this.entityData.set(HURT, value);
    }
    
    public float getHurt()
    {
    	return this.entityData.get(HURT);
    }
    
    public void setHurt(boolean value)
    {
    	this.entityData.set(IS_HURT, value);
    }
    
    public boolean isHurt()
    {
    	return this.entityData.get(IS_HURT);
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
