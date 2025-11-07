package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityFrieren;
import com.min01.minsanime.entity.living.EntityFrieren.LaserType;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class FrierenZoltraakGoal extends BasicAnimationSkillGoal<EntityFrieren>
{
	public LaserType type;
	
	public FrierenZoltraakGoal(EntityFrieren mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(1);
		this.type = LaserType.SINGLE;
	}

	@Override
	protected void performSkill() 
	{
		if(this.mob.getTarget() != null)
		{
			if(this.type != LaserType.RAPID)
			{
				Vec3 pos = AnimeUtil.getLookPos(new Vec2(this.mob.getXRot(), this.mob.getYHeadRot()), this.mob.getEyePosition(), 0.0F, 0.0F, 1.5F);
				this.mob.castZoltraak(pos, this.mob.getTarget().getEyePosition(), AnimeUtil.lookAt(this.mob.getEyePosition(), this.mob.getTarget().getEyePosition()), this.type);
			}
		}
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.type == LaserType.RAPID)
		{
			if(this.mob.getAnimationTick() % 2 == 0 && this.mob.getTarget() != null)
			{
				Vec3 pos = AnimeUtil.getSpreadPosition(this.mob.level, this.mob.getEyePosition(), new Vec3(4, 2, 4));
				this.mob.castZoltraak(pos, this.mob.getTarget().getEyePosition(), AnimeUtil.lookAt(this.mob.getEyePosition(), this.mob.getTarget().getEyePosition()), this.type);
			}
		}
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
		return 20;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 10;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 40;
	}
}
