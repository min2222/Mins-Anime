package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.entity.projectile.EntityRezeBomb;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class BombDevilAirStrikeGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilAirStrikeGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(10);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && !this.mob.onGround() && this.mob.goal == this.getClass();
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(!this.mob.onGround())
		{
			this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.25F));
		}
	}

	@Override
	protected void performSkill()
	{
		RandomSource random = this.mob.getRandom();
		for(int i = 0; i < 8; i++)
		{
			Vec3 spreadPos = AnimeUtil.getSpreadPosition(this.mob.level, this.mob.position(), new Vec3(10, 2, 10));
			EntityRezeBomb bomb = new EntityRezeBomb(AnimeEntities.REZE_BOMB.get(), this.mob.level);
			bomb.setOwner(this.mob);
			bomb.setPos(spreadPos);
			bomb.setDeltaMovement(random.nextGaussian() * 0.15F, 0.0F, random.nextGaussian() * 0.15F);
			this.mob.level.addFreshEntity(bomb);
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
		return 30;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 10;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 120;
	}
}