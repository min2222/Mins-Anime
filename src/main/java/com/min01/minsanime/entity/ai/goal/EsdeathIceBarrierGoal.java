package com.min01.minsanime.entity.ai.goal;

import java.util.EnumSet;

import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.entity.living.EntityEsdeath;
import com.min01.minsanime.entity.projectile.EntityIce;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EsdeathIceBarrierGoal extends Goal 
{
    public final EntityEsdeath mob;
    
    public Vec3 motionDirection;
    public int delay;
    
    public EsdeathIceBarrierGoal(EntityEsdeath entity) 
    {
        this.mob = entity;
        this.setFlags(EnumSet.of(Flag.JUMP));
    }
    
    public void setDirection(Vec3 arrowDirection) 
    {
        if(arrowDirection == null)
        {
        	this.motionDirection = null;
        }
        else if(this.delay <= 0)
        {
        	this.motionDirection = arrowDirection;
        }
    }
    
    @Override
    public boolean canUse()
    {
        return this.delay-- <= 0 && this.motionDirection != null && this.mob.getTarget() != null;
    }
    
    @Override
    public void start() 
    {
        if(this.motionDirection != null) 
        {
        	if(this.mob.onGround())
        	{
            	this.spawnBarrier();
        	}
            this.setDirection(null);
            this.delay = 40;
        }
    }
    
    @Override
    public boolean canContinueToUse() 
    {
        return false;
    }
    
    public void spawnBarrier()
    {
		Vec3 lookPos = AnimeUtil.getLookPos(new Vec2(0.0F, this.mob.getYHeadRot()), this.mob.position(), 0.0F, 0.0F, 2.5F);
		EntityIce ice = new EntityIce(AnimeEntities.ICE.get(), this.mob.level);
		ice.setPos(AnimeUtil.getGroundPosAbove(this.mob.level, lookPos.x, lookPos.y + 5, lookPos.z));
		ice.setOwner(this.mob);
		ice.setIceSize(2.5F);
		ice.setMaxIceYSize(5.0F);
		ice.setScaleAmount(0.5F);
		ice.setIceDamage(1.0F);
		this.mob.level.addFreshEntity(ice);
    }
}