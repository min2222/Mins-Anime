package com.min01.minsanime.entity.living;

import java.util.ArrayList;
import java.util.List;

import com.min01.minsanime.entity.AbstractAnimatableCreature;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityFrieren extends AbstractAnimatableCreature
{
	public final List<Laser> zoltraak = new ArrayList<>();
	
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
    
    @Override
    public void tick() 
    {
    	super.tick();
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) 
    {
    	List<Creeper> list = this.level.getEntitiesOfClass(Creeper.class, this.getBoundingBox().inflate(10));
    	list.forEach(t ->
    	{
    		Vec3 pos = AnimeUtil.getSpreadPosition(this.level, this.position().add(0, 5, 0), new Vec3(4, 0, 4));
    		Vec2 rot = AnimeUtil.lookAt(pos, t.getEyePosition());
    		Laser laser = new Laser(pos,rot, t);
			this.zoltraak.add(laser);
    	});
    	return super.hurt(p_21016_, p_21017_);
    }
    
    public static class Laser
    {
    	public final Vec3 position;
    	public final Vec2 rotation;
    	
    	public final Entity target;
    	
    	public float size;
    	
    	public Laser(Vec3 position, Vec2 rotation, Entity target) 
    	{
    		this.position = position;
    		this.rotation = rotation;
    		this.target = target;
		}
    }
}
