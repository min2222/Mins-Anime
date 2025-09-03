package com.min01.minsanime.entity.living;

import java.util.ArrayList;
import java.util.List;

import com.min01.minsanime.entity.AbstractAnimatableCreature;
import com.min01.minsanime.entity.ai.control.FlyingMoveControl;
import com.min01.minsanime.entity.ai.goal.FrierenZoltraakGoal;
import com.min01.minsanime.misc.AnimeEntityDataSerializers;
import com.min01.minsanime.misc.SmoothAnimationState;
import com.min01.minsanime.network.AnimeNetwork;
import com.min01.minsanime.network.UpdateZoltraakPacket;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityFrieren extends AbstractAnimatableCreature
{
	public static final EntityDataAccessor<Boolean> IS_FLYING = SynchedEntityData.defineId(EntityFrieren.class, EntityDataSerializers.BOOLEAN);
	
	public final List<Laser> zoltraak = new ArrayList<>();
	
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
    			.add(Attributes.MAX_HEALTH, 100.0D)
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
    	this.zoltraak.forEach(t -> 
    	{
    		t.tick(this.level);
    	});
    	this.zoltraak.removeIf(t -> t.shouldRemove());
    	
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
    
    public void castZoltraak(Vec3 pos, Vec2 rot, Entity target, LaserType type)
    {
		Laser laser = new Laser(pos, rot, this, target, type);
		this.zoltraak.add(laser);
		AnimeNetwork.sendToAll(new UpdateZoltraakPacket(this, laser));
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putBoolean("isFlying", this.isFlying());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_)
    {
    	super.readAdditionalSaveData(p_21450_);
    	this.setFlying(p_21450_.getBoolean("isFlying"));
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
    
    public static enum LaserType
    {
    	SINGLE,
    	MULTI,
    	RAPID,
    	BIG;
    	
		public static LaserType getRandom(RandomSource source) 
		{
			return Util.getRandom(values(), source);
		}
    }
    
    public static class Laser
    {
    	public final Vec3 position;
    	public final Vec2 rotation;
    	
    	public Vec3 targetPos = Vec3.ZERO;
    	public Vec3 targetPos2 = Vec3.ZERO;

    	public final int ownerId;
    	public final int targetId;

    	public float distance;
    	public float laserSize;
    	public float circleSize;
    	
    	public float laserMaxSize = 0.5F;
    	public float circleMaxSize = 1.0F;

    	public float circleIncrease = 0.1F;
    	public float circleDecrease = 0.1F;
    	
    	public float laserIncrease = 0.1F;
    	public float laserDecrease = 0.1F;
    	
    	public float distanceIncrease = 2.0F;
    	
    	public final LaserType type;
    	
    	public String mode = "";
    	
    	public Laser(Vec3 position, Vec2 rotation, int ownerId, int targetId, LaserType type) 
    	{
    		this.position = position;
    		this.rotation = rotation;
    		this.ownerId = ownerId;
    		this.targetId = targetId;
    		this.type = type;
    		this.init();
		}
    	
    	public Laser(Vec3 position, Vec2 rotation, Entity owner, Entity target, LaserType type) 
    	{
    		this.position = position;
    		this.rotation = rotation;
    		this.ownerId = owner.getId();
    		this.targetId = target.getId();
    		this.type = type;
    		this.init();
		}
    	
    	public void init()
    	{
    		if(this.type == LaserType.BIG)
    		{
    			this.laserMaxSize = 4.8F;
    			this.circleMaxSize = 5.0F;
    			
    			this.circleIncrease = 0.5F;
    			this.circleDecrease = 0.5F;
    			this.laserIncrease = 0.5F;
    			this.laserDecrease = 0.5F;
    			
    			this.distanceIncrease = 5.0F;
    		}
    	}
    	
    	public void tick(Level level)
    	{
    		Entity target = this.getTarget(level);
    		if(target != null)
    		{
    			this.targetPos = target.getEyePosition();
    		}
    		if(this.type == LaserType.BIG)
    		{
    			this.targetPos = AnimeUtil.getLookPos(this.rotation, this.position, 0.0F, 0.0F, 100.0F);
    		}
    		if(this.mode.equals("Decrease"))
    		{
    			this.laserSize -= this.laserDecrease;
    			this.laserSize = Math.max(this.laserSize, 0.0F);
    			
    			if(Math.floor(this.laserSize) == 0.0F)
    			{
        			this.circleSize -= this.circleDecrease;
        			this.circleSize = Math.max(this.circleSize, 0.0F);
    			}
    		}
    		else
    		{
        		this.circleSize += this.circleIncrease;
        		this.circleSize = (float) Math.min(this.circleSize, this.circleMaxSize);
        		
        		if(Math.floor(this.circleSize) == this.circleMaxSize)
        		{
            		this.laserSize += this.laserIncrease;
            		this.laserSize = (float) Math.min(this.laserSize, this.laserMaxSize);
        		}
    		}
    		if(Math.floor(this.circleSize) == this.circleMaxSize)
    		{
    			this.distance += this.distanceIncrease;
    			this.distance = (float) Math.min(this.distance, this.position.distanceTo(this.targetPos));
    		}
	        if(this.laserSize == this.laserMaxSize)
	        {
	        	Entity owner = this.getOwner(level);
	        	if(Math.floor(this.distance) == Math.floor(this.position.distanceTo(this.targetPos)))
	        	{
    	        	if(owner != null && target != null && this.type != LaserType.BIG)
    	        	{
        	        	target.hurt(target.damageSources().indirectMagic(owner, owner), 20.0F);
    	        	}
    	        	this.mode = "Decrease";
	        	}
	        	if(this.type == LaserType.BIG)
	        	{
    	        	if(this.distance >= 100.0F)
    	        	{
        	        	this.mode = "Decrease";
    	        	}
    	        	if(owner != null)
    	        	{
    					List<LivingEntity> arrayList = new ArrayList<>();
    		        	Vec3 startPos = this.position;
    					Vec3 endPos = AnimeUtil.getLookPos(this.rotation, this.position, 0.0F, 0.0F, this.distance);
    					HitResult hitResult = level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner));
    		        	Vec3 hitPos = hitResult.getLocation();
    		            Vec3 targetPos = hitPos.subtract(startPos).normalize();
    		            for(int i = 1; i < (float) startPos.distanceTo(hitPos); ++i)
    		            {
    		            	Vec3 rayPos = startPos.add(targetPos.scale(i));
    		            	List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(rayPos, rayPos).inflate(this.laserMaxSize), t -> t != owner && !t.isAlliedTo(owner));
    		            	list.forEach(t -> 
    		            	{
    		            		if(!arrayList.contains(t))
    		            		{
    		            			arrayList.add(t);
    		            		}
    		            	});
    		            }
    		            arrayList.forEach(t -> 
    		            {
            	        	t.hurt(t.damageSources().indirectMagic(owner, owner), 40.0F);
    		            });
    	        	}
	        	}
	        }
    	}
    	
    	public Vec3 getEndPos(float partialTicks, Level level) 
    	{
    		Entity target = this.getTarget(level);
    		if(target != null)
    		{
    			this.targetPos2 = target.getEyePosition(partialTicks);
    		}
    	    if(this.type == LaserType.BIG)
    	    {
    	    	return AnimeUtil.getLookPos(this.rotation, Vec3.ZERO, 0.0F, 0.0F, this.distance);
    	    }
    	    Vec3 start = this.position;
    	    Vec3 end = this.targetPos2;
    	    Vec3 control = start.add(end).scale(0.5F).add(0, this.distance / 2, 0);
    	    Vec3 motion = AnimeUtil.bezierMotionVector(start, control, end, this.distance, this.distance);
    	    return motion;
    	}
    	
    	public Entity getOwner(Level level)
    	{
    		return level.getEntity(this.ownerId);
    	}
    	
    	public Entity getTarget(Level level)
    	{
    		return level.getEntity(this.targetId);
    	}
    	
    	public boolean shouldRemove()
    	{
    		return this.distance > 0.0F && this.laserSize <= 0.0F && this.circleSize <= 0.0F;
    	}
    	
    	public static Laser read(FriendlyByteBuf buf)
    	{
    		return new Laser(AnimeEntityDataSerializers.readVec3(buf), new Vec2(buf.readFloat(), buf.readFloat()), buf.readInt(), buf.readInt(), LaserType.values()[buf.readInt()]);
    	}
    	
    	public static void write(Laser laser, FriendlyByteBuf buf)
    	{
    		AnimeEntityDataSerializers.writeVec3(buf, laser.position);
    		buf.writeFloat(laser.rotation.x);
    		buf.writeFloat(laser.rotation.y);
    		buf.writeInt(laser.ownerId);
    		buf.writeInt(laser.targetId);
    		buf.writeInt(laser.type.ordinal());
    	}
    }
}
