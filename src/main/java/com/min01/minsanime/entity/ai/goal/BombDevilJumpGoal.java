package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.util.AnimeUtil;

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
		this.mob.setExplosionScale(20.0F);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && (this.mob.distanceTo(this.mob.getTarget()) >= 8.0F || !this.mob.getTarget().onGround()) && this.mob.onGround();
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.mob.goal == this.getClass() && this.mob.getAnimationTick() <= this.getSkillUsingTime() - (this.getSkillWarmupTime() + 5) && this.mob.getAnimationState() == 5)
		{
			this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.25F));
			if(this.mob.getTarget() != null)
			{
				if(this.mob.getY() >= this.mob.getTarget().getY() + 2)
				{
					this.mob.setAnimationState(8);
					this.mob.setAnimationTick(20);
					this.mob.setDeltaMovement(0.0F, -2.5F, 0.0F);
					this.mob.doExplosion(10.0F, 20.0F, 5);
					this.mob.getTarget().setDeltaMovement(0.0F, -2.5F, 0.0F);
				}
			}
		}
	}

	@Override
	protected void performSkill()
	{
		if(this.mob.goal == this.getClass())
		{
			if(this.mob.getTarget() != null)
			{
				this.mob.setDeltaMovement(AnimeUtil.fromToVector(this.mob.position(), this.mob.getTarget().getEyePosition(), 3.5F));
			}
		}
		else
		{
			this.mob.setDeltaMovement(0.0F, 2.5F, 0.0F);
		}
		this.mob.doExplosion(10.0F, 20.0F, 5);
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.setAnimationState(0);
		if(this.mob.goal != this.getClass())
		{
			if(this.mob.getRandom().nextBoolean())
			{
				this.mob.goal = BombDevilDashGoal.class;
			}
			else
			{
				this.mob.goal = BombDevilAirStrikeGoal.class;
			}
		}
		else
		{
			this.mob.goal = null;
		}
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
