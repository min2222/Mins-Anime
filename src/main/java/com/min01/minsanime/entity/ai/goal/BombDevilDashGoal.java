package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.phys.Vec3;

public class BombDevilDashGoal extends AbstractBombDevilSkillGoal
{
	private boolean isGround;
	public BombDevilDashGoal(EntityReze mob) 
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setAnimationState(4);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && this.mob.distanceTo(this.mob.getTarget()) >= 12.0F;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(this.mob.getTarget() != null)
		{
			this.mob.lookAt(Anchor.EYES, this.mob.getTarget().getEyePosition());
		}
		if(this.mob.onGround())
		{
			this.mob.doExplosion(35.0F, 15.0F, 20.0F, 10);
			this.mob.setAnimationTick(0);
			this.mob.setAnimationState(0);
		}
	}

	@Override
	protected void performSkill()
	{
		this.mob.doExplosion(5.0F, 5.0F, 20.0F, 10);
		if(this.mob.getTarget() != null)
		{
			if(this.mob.isFlying())
			{
				Vec3 groundPos = AnimeUtil.getGroundPosAbove(this.mob.level, this.mob.getTarget().getX(), this.mob.getY() + 2, this.mob.getTarget().getZ());
				this.mob.setDeltaMovement(AnimeUtil.fromToVector(this.mob.position(), groundPos, 5.0F));
				if(Math.random() <= 0.4F)
				{
					this.isGround = true;
				}
			}
			else
			{
				this.mob.setDeltaMovement(AnimeUtil.fromToVector(this.mob.position(), this.mob.getTarget().position(), 7.0F));
			}
		}
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
		this.mob.goal = null;
		if(this.isGround)
		{
			this.mob.setFlying(false);
			this.isGround = false;
		}
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 15;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 5;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 90;
	}
}
