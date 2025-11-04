package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;

public class BombDevilDashGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilDashGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(4);
		this.mob.setExplosionScale(10.0F);
	}
	
	@Override
	public boolean canUse()
	{
		return !this.mob.onGround() && this.mob.goal == this.getClass();
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(this.mob.getTarget() != null)
		{
			this.mob.lookAt(Anchor.EYES, this.mob.getTarget().getEyePosition());
		}
		if(this.mob.onGround())
		{
			this.mob.doExplosion(15.0F, 15.0F, 10);
			this.mob.setAnimationTick(0);
		}
	}

	@Override
	protected void performSkill()
	{
		this.mob.doExplosion(5.0F, 5.0F, 10);
		if(this.mob.getTarget() != null)
		{
			this.mob.setDeltaMovement(AnimeUtil.fromToVector(this.mob.position(), this.mob.getTarget().position(), 5.0F));
		}
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
		this.mob.goal = null;
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
		return 60;
	}
}
