package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;

public abstract class AbstractBombDevilSkillGoal extends BasicAnimationSkillGoal<EntityReze>
{
	public AbstractBombDevilSkillGoal(EntityReze mob)
	{
		super(mob);
	}

	@Override
	public boolean canUse() 
	{
		if(this.mob.isTransformed())
		{
			return super.canUse() || this.mob.goal == this.getClass();
		}
		return false;
	}
}
