package com.min01.minsanime.entity.ai.goal;

import java.util.ArrayList;
import java.util.Optional;

import com.min01.minsanime.entity.living.EntityAltair;
import com.min01.minsanime.entity.living.EntityAltair.HolopsiconMovement;
import com.min01.minsanime.sound.AnimeSounds;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class AltairHolopsiconGoal extends AbstractAltairSkillGoal
{
	public HolopsiconMovement movement;
	public AltairHolopsiconGoal(EntityAltair mob)
	{
		super(mob);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.mob.setAnimationState(1);
		if(!this.mob.getTarget().getActiveEffects().isEmpty())
		{
			this.movement = HolopsiconMovement.NO13;
		}
		Optional<MobEffectInstance> optional = new ArrayList<>(this.mob.getActiveEffects()).stream().filter(t -> t.getEffect().getCategory().equals(MobEffectCategory.HARMFUL)).findAny();
		if(this.mob.getTotalDamage() > 0.0F || optional.isPresent())
		{
			this.movement = HolopsiconMovement.NO14;
		}
		if(this.mob.getHealth() <= this.mob.getMaxHealth() / 2)
		{
			this.movement = HolopsiconMovement.NO20;
		}
	}

	@Override
	protected void performSkill() 
	{
		this.mob.setHolopsicon(true);
		this.mob.playSound(AnimeSounds.HOLOPSICON.get(), 15.0F, 1.0F);
		if(this.mob.getTarget() != null && this.movement != null)
		{
			switch(this.movement)
			{
			case NO20:
				Entity entity = this.mob.getTarget().getType().create(this.mob.level);
				AnimeUtil.setOwner(entity, this.mob);
				entity.setPos(AnimeUtil.getGroundPosAbove(this.mob.level, this.mob.getX(), this.mob.getY(), this.mob.getZ()));
				if(entity instanceof Mob mob)
				{
					mob.setTarget(this.mob.getTarget());
				}
				entity.lookAt(Anchor.EYES, this.mob.getTarget().getEyePosition());
				this.mob.level.addFreshEntity(entity);
				break;
			case NO14:
				this.mob.getTarget().setHealth(this.mob.getTarget().getHealth() - this.mob.getTotalDamage());
				this.mob.setHealth(this.mob.getHealth() + this.mob.getTotalDamage());
				this.mob.setTotalDamage(0.0F);
				new ArrayList<>(this.mob.getActiveEffects()).forEach(t -> 
				{
					if(t.getEffect().getCategory().equals(MobEffectCategory.HARMFUL))
					{
						this.mob.getTarget().addEffect(t);
						this.mob.removeEffect(t.getEffect());
					}
				});
				break;
			case NO13:
				this.mob.getTarget().removeAllEffects();
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAnimationState(0);
		this.mob.setHolopsicon(false);
	}

	@Override
	protected int getSkillUsingTime() 
	{
		return 60;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 31;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 500;
	}
}
