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
		this.type = LaserType.getRandom();
	}

	@Override
	protected void performSkill() 
	{
		if(this.type == LaserType.SINGLE)
		{
			Vec3 pos = AnimeUtil.getLookPos(new Vec2(this.mob.getXRot(), this.mob.getYHeadRot()), this.mob.getEyePosition(), 0.0F, 0.0F, 1.5F);
			this.mob.castZoltraak(pos, AnimeUtil.lookAt(this.mob.getEyePosition(), this.mob.getTarget().getEyePosition()), this.mob.getTarget(), this.type);
		}
		if(this.type == LaserType.MULTI)
		{
			
		}
		if(this.type == LaserType.BIG)
		{
			Vec3 pos = AnimeUtil.getLookPos(new Vec2(this.mob.getXRot(), this.mob.getYHeadRot()), this.mob.getEyePosition(), 0.0F, 0.0F, 1.5F);
			this.mob.castZoltraak(pos, AnimeUtil.lookAt(this.mob.getEyePosition(), this.mob.getTarget().getEyePosition()), this.mob.getTarget(), this.type);
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
				this.mob.castZoltraak(pos, AnimeUtil.lookAt(this.mob.getEyePosition(), this.mob.getTarget().getEyePosition()), this.mob.getTarget(), this.type);
			}
		}
	}
	
	@Override
	public void stop() 
	{
		super.stop();
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 20;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 1;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 20;
	}
}
