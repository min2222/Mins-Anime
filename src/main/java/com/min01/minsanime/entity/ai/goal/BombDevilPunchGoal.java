package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;

public class BombDevilPunchGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilPunchGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(9);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && this.mob.distanceTo(this.mob.getTarget()) <= 6.0F;
	}

	@Override
	protected void performSkill()
	{
		if(this.mob.getTarget() != null)
		{
			if(this.mob.distanceTo(this.mob.getTarget()) <= 6.0F)
			{
				this.mob.doExplosion(20.0F, 20.0F, 20.0F, 5);
				this.mob.getTarget().setDeltaMovement(0.0F, 2.5F, 0.0F);
			}
		}
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
		if(Math.random() <= 0.8F)
		{
			this.mob.goal = BombDevilJumpGoal.class;
		}
		else
		{
			this.mob.goal = BombDevilRainbowExplosionGoal.class;
	    	this.nextSkillTickCount = this.mob.tickCount + 200;
		}
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 20;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 5;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 100;
	}
}
