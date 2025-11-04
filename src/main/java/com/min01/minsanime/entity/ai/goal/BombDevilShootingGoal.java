package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.entity.projectile.EntityRezeBullet;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.phys.Vec2;

public class BombDevilShootingGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilShootingGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(2);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && (this.mob.distanceTo(this.mob.getTarget()) >= 8.0F || !this.mob.getTarget().onGround());
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.25F));
	}

	@Override
	protected void performSkill()
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
		return 30;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 20;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 180;
	}
}
