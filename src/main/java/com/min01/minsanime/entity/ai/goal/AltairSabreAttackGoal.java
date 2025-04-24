package com.min01.minsanime.entity.ai.goal;

import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.entity.living.EntityAltair;
import com.min01.minsanime.entity.projectile.EntityAltairSabre;
import com.min01.minsanime.entity.projectile.EntityAltairSabre.SabreAttackType;
import com.min01.minsanime.entity.projectile.EntityAltairSabre.SabreType;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.phys.Vec3;

public class AltairSabreAttackGoal extends AbstractAltairSkillGoal
{
	public SabreAttackType type;
	public AltairSabreAttackGoal(EntityAltair mob) 
	{
		super(mob);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.type = SabreAttackType.getRandom();
	}

	@Override
	protected void performSkill() 
	{
		switch(this.type)
		{
		case CIRCLE:
			for(float i = 0.0F; i < 360.0F; i += 360.0F / this.mob.getSabreCount())
			{
				EntityAltairSabre sabre = new EntityAltairSabre(AnimeEntities.ALTAIR_SABRE.get(), this.mob.level);
				sabre.setOwner(this.mob);
				sabre.setNoGravity(true);
				sabre.type = SabreType.HOMING;
		    	Vec3 lookPos = AnimeUtil.getLookPos(0, this.mob.getYRot() + i, 0, 4.5F);
		    	sabre.setPos(this.mob.getEyePosition().add(lookPos));
		    	sabre.shootFromRotation(this.mob, 0, this.mob.getYRot() + i, 0.0F, 1.5F, 1.0F);
				this.mob.level.addFreshEntity(sabre);
			}
			break;
		case AROUND_TARGET:
			for(int i = 0; i < this.mob.level.random.nextInt(8, 12); i++)
			{
				EntityAltairSabre sabre = new EntityAltairSabre(AnimeEntities.ALTAIR_SABRE.get(), this.mob.level);
				sabre.setOwner(this.mob);
				sabre.setNoGravity(true);
		    	sabre.setPos(AnimeUtil.getSpreadPosition(sabre.level, this.mob.getTarget().getEyePosition().add(0, 10, 0), new Vec3(this.mob.getTarget().getBbWidth() + 10.0D, 0, this.mob.getTarget().getBbWidth() + 10.0D)));
		    	sabre.lookAt(Anchor.EYES, this.mob.getTarget().getEyePosition());
		    	sabre.shootFromRotation(this.mob, sabre.getXRot(), sabre.getYRot(), 0.0F, 3.0F, 1.0F);
				this.mob.level.addFreshEntity(sabre);
			}
			break;
		case RANDOM_POS:
			for(int i = 0; i < this.mob.level.random.nextInt(8, 12); i++)
			{
				EntityAltairSabre sabre = new EntityAltairSabre(AnimeEntities.ALTAIR_SABRE.get(), this.mob.level);
				sabre.setOwner(this.mob);
				sabre.setNoGravity(true);
		    	sabre.setPos(AnimeUtil.getSpreadPosition(sabre.level, this.mob.getEyePosition().add(0, 10, 0), new Vec3(8, 4, 8)));
		    	sabre.lookAt(Anchor.EYES, this.mob.getTarget().position());
		    	sabre.shootFromRotation(this.mob, sabre.getXRot(), sabre.getYRot(), 0.0F, 3.0F, 1.0F);
				this.mob.level.addFreshEntity(sabre);
			}
			break;
		default:
			break;
		}
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
		return 60;
	}
}
