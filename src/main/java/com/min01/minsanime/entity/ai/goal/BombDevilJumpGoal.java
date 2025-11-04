package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;

public class BombDevilJumpGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilJumpGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(5);
		this.mob.setExplosionScale(30.0F);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && (this.mob.distanceTo(this.mob.getTarget()) >= 6.0F || !this.mob.getTarget().onGround()) && this.mob.onGround();
	}

	@Override
	protected void performSkill()
	{
		this.mob.setDeltaMovement(0.0F, 2.5F, 0.0F);
		this.mob.doExplosion(10.0F, 20.0F, 5);
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.goal = BombDevilDashGoal.class;
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 30;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 18;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 100;
	}
}
