package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityAltair;

public class AltairSummonSabreGoal extends AbstractAltairSkillGoal
{
	public AltairSummonSabreGoal(EntityAltair mob) 
	{
		super(mob);
	}
	
	@Override
	public boolean additionalStartCondition()
	{
		return this.mob.getSabreCount() < EntityAltair.MAX_SABRE_NUMBER - 1;
	}

	@Override
	protected void performSkill()
	{
		if(this.mob.getSabreCount() < EntityAltair.MAX_SABRE_NUMBER - 1)
		{
			this.mob.setSabreCount(this.mob.getSabreCount() + 1);
			this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
		}
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 1 * EntityAltair.MAX_SABRE_NUMBER;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 1;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 100;
	}
}
