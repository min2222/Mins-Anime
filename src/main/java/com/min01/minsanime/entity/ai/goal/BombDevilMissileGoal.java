package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.entity.projectile.EntityRezeMissile;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class BombDevilMissileGoal extends AbstractBombDevilSkillGoal
{
	public BombDevilMissileGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(7);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && (this.mob.distanceTo(this.mob.getTarget()) >= 12.0F || !this.mob.getTarget().onGround()) && this.mob.onGround();
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
		for(int i = 0; i < 10; i++)
		{
			Vec3 lookPos = AnimeUtil.getLookPos(new Vec2(this.mob.getXRot(), this.mob.yHeadRot), this.mob.getEyePosition(), 0, 0, -2.5F);
			Vec3 spreadPos = AnimeUtil.getSpreadPosition(this.mob.level, lookPos, new Vec3(5, 4, 5));
			EntityRezeMissile missile = new EntityRezeMissile(AnimeEntities.REZE_MISSILE.get(), this.mob.level);
			missile.setOwner(this.mob);
			missile.setPos(spreadPos);
			missile.setNoGravity(true);
			this.mob.level.addFreshEntity(missile);
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
		return 220;
	}
}