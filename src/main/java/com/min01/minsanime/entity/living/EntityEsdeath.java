package com.min01.minsanime.entity.living;

import java.util.List;

import com.min01.minsanime.entity.AbstractAnimatableMonster;
import com.min01.minsanime.entity.ai.goal.EsdeathIceBarrierGoal;
import com.min01.minsanime.entity.ai.goal.EsdeathIceWallGoal;
import com.min01.minsanime.misc.SmoothAnimationState;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityEsdeath extends AbstractAnimatableMonster
{
	public final SmoothAnimationState stompAnimationState = new SmoothAnimationState();
	
	public EntityEsdeath(EntityType<? extends Monster> p_33002_, Level p_33003_)
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 350.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.55D)
        		.add(Attributes.ARMOR, 10.0D)
        		.add(Attributes.ARMOR_TOUGHNESS, 5.0D)
        		.add(Attributes.FOLLOW_RANGE, 100.0D)
        		.add(Attributes.KNOCKBACK_RESISTANCE, 15.0D);
    }
    
    @Override
    protected void registerGoals() 
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(0, new EsdeathIceWallGoal(this));
    	this.goalSelector.addGoal(0, new EsdeathIceBarrierGoal(this));
    	this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
    	this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false));
    }
    
    @Override
    public void tick()
    {
    	super.tick();
    	
    	if(this.level.isClientSide)
    	{
    		this.stompAnimationState.updateWhen(this.isUsingSkill(1), this.tickCount);
    	}
    	
    	if(this.getTarget() != null)
    	{
    		if(this.canLook())
    		{
        		this.getLookControl().setLookAt(this.getTarget(), 30.0F, 30.0F);
    		}
    	}
    	
		List<Entity> list = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(3.5F), t -> t != this && !t.isAlliedTo(this) && (!(t instanceof Player player) || !player.getAbilities().instabuild));
		list.forEach(t -> 
		{
            AnimeUtil.performCheck(t, this);
		});
    }
}
