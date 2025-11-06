package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.entity.projectile.EntityRezeBullet;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.phys.Vec2;

public class BombDevilRapidFireGoal extends AbstractBombDevilSkillGoal
{
	private int count;
	
	public BombDevilRapidFireGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		if(this.mob.getRandom().nextBoolean())
		{
			this.mob.setAnimationState(11);
		}
		else
		{
			this.mob.setAnimationState(12);
		}
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && this.mob.distanceTo(this.mob.getTarget()) >= 12.0F;
	}

	@Override
	protected void performSkill()
	{
		if(this.count < 5 && this.mob.getTarget() != null)
		{
			EntityRezeBullet bullet = new EntityRezeBullet(AnimeEntities.REZE_BULLET.get(), this.mob.level);
			bullet.setOwner(this.mob);
			bullet.setPos(AnimeUtil.getLookPos(new Vec2(this.mob.getXRot(), this.mob.yHeadRot), this.mob.getEyePosition(), 0, 0, 1.5F));
			bullet.setNoGravity(true);
			if(this.mob.getTarget() != null)
			{
				bullet.setDeltaMovement(AnimeUtil.fromToVector(bullet.position(), this.mob.getTarget().position(), 2.5F));
			}
			this.mob.level.addFreshEntity(bullet);
			
			if(this.mob.getRandom().nextBoolean())
			{
				this.mob.setAnimationState(11);
			}
			else
			{
				this.mob.setAnimationState(12);
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
		this.count = 0;
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 60;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 10;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 250;
	}
}
