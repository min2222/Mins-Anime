package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;

public class RezeTransformingGoal extends BasicAnimationSkillGoal<EntityReze>
{
	public RezeTransformingGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.mob.setAnimationState(1);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && !this.mob.isTransformed();
	}

	@Override
	protected void performSkill() 
	{
		this.mob.setTransformed(true);
		this.mob.doExplosion(100.0F, 25.0F, 50.0F, 10);
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.setAnimationState(0);
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 25;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 20;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 1000;
	}
}
