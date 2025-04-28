package com.min01.minsanime.entity.living;

import com.min01.minsanime.entity.AbstractAnimatableCreature;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;

public class EntityFrieren extends AbstractAnimatableCreature
{
	public EntityFrieren(EntityType<? extends PathfinderMob> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
    			.add(Attributes.MAX_HEALTH, 100.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.45D)
        		.add(Attributes.ARMOR, 5.0D)
        		.add(Attributes.ARMOR_TOUGHNESS, 5.0D)
        		.add(Attributes.FOLLOW_RANGE, 100.0D);
    }
    
    @Override
    protected void registerGoals()
    {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.45D));
    	this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    	this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }
}
