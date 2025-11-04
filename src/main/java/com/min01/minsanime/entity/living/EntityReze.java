package com.min01.minsanime.entity.living;

import java.util.List;

import com.min01.minsanime.entity.AbstractAnimatableCreature;
import com.min01.minsanime.entity.EntityCameraShake;
import com.min01.minsanime.entity.ai.goal.AbstractAnimationSkillGoal;
import com.min01.minsanime.entity.ai.goal.BombDevilCatchingGoal;
import com.min01.minsanime.entity.ai.goal.BombDevilDashGoal;
import com.min01.minsanime.entity.ai.goal.BombDevilJumpGoal;
import com.min01.minsanime.entity.ai.goal.BombDevilKickGoal;
import com.min01.minsanime.entity.ai.goal.BombDevilShootingGoal;
import com.min01.minsanime.entity.ai.goal.RezeTransformingGoal;
import com.min01.minsanime.misc.SmoothAnimationState;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityReze extends AbstractAnimatableCreature
{
	public static final EntityDataAccessor<Boolean> IS_TRANSFORMED = SynchedEntityData.defineId(EntityReze.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> EXPLOSION_SCALE = SynchedEntityData.defineId(EntityReze.class, EntityDataSerializers.FLOAT);
	
	public final SmoothAnimationState transformAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState shootAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState catchAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState dashAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState jumpAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState kickAnimationState = new SmoothAnimationState();
	
	public Class<? extends AbstractAnimationSkillGoal<EntityReze>> goal;
	
	public EntityReze(EntityType<? extends PathfinderMob> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 200.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.45D)
        		.add(Attributes.ARMOR, 1.0D)
        		.add(Attributes.FOLLOW_RANGE, 60.0D)
        		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(IS_TRANSFORMED, false);
    	this.entityData.define(EXPLOSION_SCALE, 1.0F);
    }
    
    @Override
    protected void registerGoals() 
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(0, new RezeTransformingGoal(this));
    	this.goalSelector.addGoal(0, new BombDevilShootingGoal(this));
    	this.goalSelector.addGoal(0, new BombDevilCatchingGoal(this));
    	this.goalSelector.addGoal(0, new BombDevilJumpGoal(this));
    	this.goalSelector.addGoal(0, new BombDevilDashGoal(this));
    	this.goalSelector.addGoal(0, new BombDevilKickGoal(this));
    	this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
    	this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false));
    }
    
    @Override
    public void tick()
    {
    	super.tick();
    	
    	this.resetFallDistance();
    	
    	if(this.level.isClientSide)
    	{
    		this.transformAnimationState.updateWhen(this.isUsingSkill(1), this.tickCount);
    		this.shootAnimationState.updateWhen(this.isUsingSkill(2), this.tickCount);
    		this.catchAnimationState.updateWhen(this.isUsingSkill(3), this.tickCount);
    		this.dashAnimationState.updateWhen(this.isUsingSkill(4), this.tickCount);
    		this.jumpAnimationState.updateWhen(this.isUsingSkill(5), this.tickCount);
    		this.kickAnimationState.updateWhen(this.isUsingSkill(6), this.tickCount);
    	}
    	
    	if(this.getTarget() != null)
    	{
    		if(this.canLook())
    		{
        		this.getLookControl().setLookAt(this.getTarget(), 100.0F, 100.0F);
    		}
    		if(this.canMove())
    		{
    			this.getNavigation().moveTo(this.getTarget(), !this.isTransformed() ? 0.45F : 0.65F);
    		}
    	}
    }
    
    
    public void doExplosion(float damage, float radius, int fireSeconds)
    {
    	this.doExplosion(damage, radius, fireSeconds, 99);
    }
    
    public void doExplosion(float damage, float radius, int fireSeconds, int id)
    {
		this.level.broadcastEntityEvent(this, (byte) id);
		this.playSound(SoundEvents.GENERIC_EXPLODE, damage, 1.0F);
		EntityCameraShake.cameraShake(this.level, this.position(), damage, 0.35F, 0, 60);
		
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius / 2.0F), t -> t != this && !t.isAlliedTo(this) && !(t instanceof EntityReze));
		list.forEach(t -> 
		{
			if(t.hurt(this.damageSources().explosion(this, this), damage))
			{
	    		Vec3 motion = AnimeUtil.fromToVector(this.position(), t.position().add(0, 1, 0), 1.5F);
				t.setSecondsOnFire(fireSeconds);
	    		t.setDeltaMovement(motion.x, motion.y, motion.z);
	    		if(t instanceof ServerPlayer player)
	    		{
	    			player.connection.send(new ClientboundSetEntityMotionPacket(t));
	    		}
			}
		});
    }
    
    @Override
    public void handleEntityEvent(byte p_21375_)
    {
    	super.handleEntityEvent(p_21375_);
    	if(p_21375_ == 99)
    	{
        	AnimeShaderEffects.addEffect(this.level, "Explosion", this.position(), 90, this.getExplosionScale());
    	}
    	if(p_21375_ == 98)
    	{
        	AnimeShaderEffects.addEffect(this.level, "Explosion", AnimeUtil.getLookPos(new Vec2(this.getXRot(), this.yHeadRot), this.getEyePosition(), 0, 0, 1.5F), 90, this.getExplosionScale());
    	}
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putBoolean("isTransformed", this.isTransformed());
    	p_21484_.putFloat("ExplosionScale", this.getExplosionScale());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) 
    {
    	super.readAdditionalSaveData(p_21450_);
    	this.setTransformed(p_21450_.getBoolean("isTransformed"));
    	this.setExplosionScale(p_21450_.getFloat("ExplosionScale"));
    }
    
    @Override
    protected Component getTypeName()
    {
    	if(this.isTransformed())
    	{
    		return Component.translatable("entity.minsanime.bomb_devil");
    	}
    	return super.getTypeName();
    }
    
    public void setExplosionScale(float value)
    {
    	this.entityData.set(EXPLOSION_SCALE, value);
    }
    
    public float getExplosionScale()
    {
    	return this.entityData.get(EXPLOSION_SCALE);
    }
    
    public void setTransformed(boolean value)
    {
    	this.entityData.set(IS_TRANSFORMED, value);
    }
    
    public boolean isTransformed()
    {
    	return this.entityData.get(IS_TRANSFORMED);
    }
}
