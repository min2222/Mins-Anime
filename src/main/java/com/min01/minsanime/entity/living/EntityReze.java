package com.min01.minsanime.entity.living;

import com.min01.minsanime.entity.AbstractAnimatableMonster;
import com.min01.minsanime.shader.AnimeShaderEffects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class EntityReze extends AbstractAnimatableMonster
{
	public EntityReze(EntityType<? extends Monster> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 200.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.45D)
        		.add(Attributes.ARMOR, 1.0D)
        		.add(Attributes.FOLLOW_RANGE, 60.0D)
        		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }
    
    @Override
    protected void registerGoals() 
    {
    	super.registerGoals();
    	this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
    	this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false));
    }
    
    @Override
    public void tick()
    {
    	super.tick();
    	
    	if(this.getTarget() != null)
    	{
    		if(this.canLook())
    		{
        		this.getLookControl().setLookAt(this.getTarget(), 30.0F, 30.0F);
    		}
    	}
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) 
    {
    	AnimeShaderEffects.addEffect(this.level, "Explosion", this.position(), 90);
    	return super.hurt(p_21016_, p_21017_);
    }
}
