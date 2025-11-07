package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityEsdeath;

public class EsdeathIceWallGoal extends BasicAnimationSkillGoal<EntityEsdeath>
{
	public EsdeathIceWallGoal(EntityEsdeath mob)
	{
		super(mob);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.mob.setAnimationState(1);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && this.mob.distanceTo(this.mob.getTarget()) >= 12.0F && this.mob.getHealth() <= this.mob.getMaxHealth() / 2;
	}

	@Override
	protected void performSkill()
	{
		/*int dist = (int) Math.floor(this.mob.distanceTo(this.mob.getTarget()) / 5.0F);
		for(int i = 0; i < dist; i++)
		{
			int index = i + 1;
			Vec3 lookPos = AnimeUtil.getLookPos(new Vec2(0.0F, this.mob.getYHeadRot()), this.mob.position(), 0, 0, 4 + (index * 10));
			EntityIce ice = new EntityIce(AnimeEntities.ICE.get(), this.mob.level);
			ice.setPos(AnimeUtil.getGroundPosAbove(this.mob.level, lookPos.x, lookPos.y + 5, lookPos.z));
			ice.setOwner(this.mob);
			ice.setIceSize(5.0F);
			ice.setMaxIceYSize(35.0F);
			ice.setScaleAmount(2.0F);
			ice.setDelay(index * 8);
			ice.setIceDamage(15.0F);
			ice.setCameraShake(true);
			ice.setXRot(this.mob.getXRot() + 10.0F);
			this.mob.level.addFreshEntity(ice);
		}*/
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
		return 8;
	}

	@Override
	protected int getSkillUsingInterval()
	{
		return 350;
	}
}
