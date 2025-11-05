package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;

import net.minecraft.world.phys.Vec3;

public class BombDevilRainbowExplosionGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilRainbowExplosionGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(13);
	}
	
	@Override
	public boolean canUse()
	{
		return this.mob.onGround() && this.mob.goal == this.getClass();
	}

	@Override
	protected void performSkill()
	{
		this.mob.doRainbowExplosion(150.0F, 25.0F, 40.0F, 40, this.mob.position(), new Vec3(1, 0, 0));
		this.mob.doRainbowExplosion(150.0F, 25.0F, 40.0F, 40, this.mob.position().add(0, 15, 0), Vec3.fromRGB24(15797921));
		this.mob.doRainbowExplosion(150.0F, 25.0F, 40.0F, 40, this.mob.position().add(0, 30, 0), new Vec3(0, 0, 1));
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
		this.mob.goal = BombDevilMissileGoal.class;
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
		return 300;
	}
}
