package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;

import net.minecraft.world.phys.Vec3;

public class BombDevilRainbowExplosionGoal extends AbstractBombDevilSkillGoal
{
	public int count;
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
		if(this.count < 3)
		{
			if(this.count == 0)
			{
				this.mob.doRainbowExplosion(150.0F, 25.0F, 10.0F, 40, this.mob.position(), new Vec3(0.9F, 0.05F, 0.05F));
			}
			if(this.count == 1)
			{
				this.mob.doRainbowExplosion(150.0F, 25.0F, 10.0F, 40, this.mob.position().add(0, 10, 0), Vec3.fromRGB24(15797921));
			}
			if(this.count == 2)
			{
				this.mob.doRainbowExplosion(150.0F, 25.0F, 10.0F, 40, this.mob.position().add(0, 20, 0), new Vec3(0, 0, 1));
			}
			this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
			this.count++;
		}
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
		this.mob.goal = BombDevilMissileGoal.class;
		this.count = 0;
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 50;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 10;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 500;
	}
}
