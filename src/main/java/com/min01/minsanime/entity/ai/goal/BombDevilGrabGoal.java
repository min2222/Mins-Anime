package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class BombDevilGrabGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilGrabGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(3);
		this.mob.setCanLook(false);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && this.mob.distanceTo(this.mob.getTarget()) <= 3.0F && !this.mob.isPassenger();
	}

	@Override
	protected void performSkill()
	{
		this.mob.doExplosion(20.0F, 20.0F, 20.0F, 20, AnimeUtil.getLookPos(new Vec2(this.mob.getXRot(), this.mob.yHeadRot), this.mob.getEyePosition(), 0, 0, 1.5F));
	}
	
	@Override
	public boolean canContinueToUse() 
	{
		return super.canContinueToUse() && this.mob.getTarget() != null && this.mob.distanceTo(this.mob.getTarget()) <= 3.0F;
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		
		if(this.mob.getAnimationTick() <= this.getSkillUsingTime() - 5 && this.mob.getAnimationTick() > this.getSkillUsingTime() - this.getSkillWarmupTime())
		{
			if(this.mob.getTarget() != null && this.mob.distanceTo(this.mob.getTarget()) <= 3.0F)
			{
				this.mob.getTarget().moveTo(AnimeUtil.getLookPos(new Vec2(this.mob.getXRot(), this.mob.yHeadRot), this.mob.getEyePosition(), 0, 0, 1.5F));
				this.mob.getTarget().setDeltaMovement(Vec3.ZERO);
				this.mob.getTarget().resetFallDistance();
			}
		}
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
		this.mob.setCanLook(true);
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 60;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 40;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 250;
	}
}
