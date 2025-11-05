package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.phys.Vec2;

public class BombDevilKickGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilKickGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(6);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && this.mob.distanceTo(this.mob.getTarget()) <= 6.0F;
	}

	@Override
	protected void performSkill()
	{
		this.mob.doExplosion(50.0F, 30.0F, 30.0F, 25, AnimeUtil.getLookPos(new Vec2(this.mob.getXRot(), this.mob.yHeadRot), this.mob.getEyePosition(), 0, 0, 1.5F));
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
		return 10;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 5;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 80;
	}
}
