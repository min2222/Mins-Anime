package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityAltair;

public abstract class AbstractAltairSkillGoal extends BasicAnimationSkillGoal<EntityAltair>
{
	public AbstractAltairSkillGoal(EntityAltair mob) 
	{
		super(mob);
	}

	@Override
	public boolean stopMovingWhenStart()
	{
		return false;
	}
	
	@Override
	public boolean additionalStartCondition() 
	{
		return this.mob.getSabreCount() > 0;
	}
}
