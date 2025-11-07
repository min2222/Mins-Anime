package com.min01.minsanime.entity.living;

import com.min01.minsanime.entity.AbstractAnimatableCreature;
import com.min01.minsanime.entity.ai.control.FlyingMoveControl;
import com.min01.minsanime.entity.ai.goal.FrierenZoltraakGoal;
import com.min01.minsanime.misc.AnimeEntityDataSerializers;
import com.min01.minsanime.misc.SmoothAnimationState;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityFrieren extends AbstractAnimatableCreature
{
	public static final EntityDataAccessor<Boolean> IS_FLYING = SynchedEntityData.defineId(EntityFrieren.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> CIRCLE_SIZE = SynchedEntityData.defineId(EntityFrieren.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Vec2> ROTATION = SynchedEntityData.defineId(EntityFrieren.class, AnimeEntityDataSerializers.VEC2.get());
	
	public final SmoothAnimationState zoltraakAnimationState = new SmoothAnimationState(1.0F);
	public final SmoothAnimationState flyStartAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState flyLoopAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState flyEndAnimationState = new SmoothAnimationState();
	
	public EntityFrieren(EntityType<? extends PathfinderMob> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
    			.add(Attributes.MAX_HEALTH, 300.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.45D)
        		.add(Attributes.ARMOR, 5.0D)
        		.add(Attributes.ARMOR_TOUGHNESS, 5.0D)
        		.add(Attributes.FOLLOW_RANGE, 100.0D);
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(IS_FLYING, false);
    	this.entityData.define(CIRCLE_SIZE, 0.0F);
    	this.entityData.define(ROTATION, Vec2.ZERO);
    }
    
    @Override
    protected void registerGoals()
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(0, new FrierenZoltraakGoal(this));
    	this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, false));
    	this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Phantom.class, false));
    	this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }
    
    @Override
    public void tick() 
    {
    	super.tick();
    	
    	if(this.level.isClientSide)
    	{
    		this.zoltraakAnimationState.updateWhen(this.isUsingSkill(1) && !this.isFlying(), this.tickCount);
    		this.flyStartAnimationState.updateWhen(this.getAnimationState() == 2, this.tickCount);
    		this.flyLoopAnimationState.updateWhen(this.isFlying() && this.getAnimationState() != 3, this.tickCount);
    		this.flyEndAnimationState.updateWhen(this.getAnimationState() == 3, this.tickCount);
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
					if(this.getAnimationState() != 3)
					{
						this.setAnimationState(3);
						this.setAnimationTick(20);
					}
				}				
				if(this.getTarget().getY() > groundPos.getY() + 3 && !this.isFlying())
				{
					if(this.getAnimationState() != 2)
					{
						this.setAnimationState(2);
						this.setAnimationTick(20);
					}
				}
			}
    	}
    	
    	if(this.isFlying())
    	{
			this.resetFallDistance();
    	}
    	
    	if(this.getAnimationTick() <= 0)
    	{
    		if(this.getAnimationState() == 2)
    		{
    			this.setAnimationState(0);
    			this.setFlying(true);
    		}
    		if(this.getAnimationState() == 3)
    		{
    			this.setAnimationState(0);
    			this.setFlying(false);
    		}
    	}
    }
    
    public void castZoltraak(Vec3 pos, Vec3 target, Vec2 rot, LaserType type)
    {
    	this.setRotation(rot);
    	AnimeShaderEffects.addZoltraak(this.level, pos, target, 80, type.maxScale, rot);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putBoolean("isFlying", this.isFlying());
    	p_21484_.putFloat("CircleSize", this.getCircleSize());
    	
    	CompoundTag tag = new CompoundTag();
    	tag.putFloat("XRot", this.getRotation().x);
    	tag.putFloat("YRot", this.getRotation().y);
    	p_21484_.put("Rotation", tag);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_)
    {
    	super.readAdditionalSaveData(p_21450_);
    	this.setFlying(p_21450_.getBoolean("isFlying"));
    	this.setCircleSize(p_21450_.getFloat("CircleSize"));
    	
    	CompoundTag tag = p_21450_.getCompound("Rotation");
    	this.setRotation(new Vec2(tag.getFloat("XRot"), tag.getFloat("YRot")));
    }
    
    public void switchControl(boolean isFlying)
    {
    	if(isFlying)
    	{
    		this.moveControl = new FlyingMoveControl(this);
    	}
    	else
    	{
    		this.moveControl = new MoveControl(this);
    	}
    }
    
    public void setFlying(boolean value)
    {
        this.setNoGravity(value);
		this.switchControl(value);
    	this.entityData.set(IS_FLYING, value);
    }
    
    public boolean isFlying() 
    {
    	return this.entityData.get(IS_FLYING);
    }
    
    public void setCircleSize(float size)
    {
    	this.entityData.set(CIRCLE_SIZE, size);
    }
    
    public float getCircleSize()
    {
    	return this.entityData.get(CIRCLE_SIZE);
    }
    
    public void setRotation(Vec2 rot)
    {
    	this.entityData.set(ROTATION, rot);
    }
    
    public Vec2 getRotation()
    {
    	return this.entityData.get(ROTATION);
    }
    
    public static enum LaserType
    {
    	SINGLE(0.5F),
    	RAPID(0.5F),
    	BIG(4.8F);
    	
    	private final float maxScale;
    	
    	private LaserType(float maxScale) 
    	{
    		this.maxScale = maxScale;
		}
    	
		public static LaserType getRandom(RandomSource source) 
		{
			return Util.getRandom(values(), source);
		}
    }
}
