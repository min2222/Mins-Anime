package com.min01.minsanime.entity.projectile;

import java.util.List;

import com.min01.minsanime.entity.ITrail;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityRezeMissile extends ThrowableProjectile implements ITrail
{
    private Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;
    
	public EntityRezeMissile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_)
	{
		super(p_37466_, p_37467_);
	}

	@Override
	protected void defineSynchedData()
	{
		
	}
	
	@Override
	public boolean displayFireAnimation()
	{
		return false;
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		
		if(this.level.isClientSide)
		{
			this.tickTrail();
		}
		
		if(this.tickCount >= 200)
		{
			this.discard();
		}
		
		if(this.getOwner() != null)
		{
			if(this.getOwner() instanceof Mob mob && mob.getTarget() != null)
			{
				this.setDeltaMovement(AnimeUtil.fromToVector(this.position(), mob.getTarget().getEyePosition(), Math.min(this.tickCount / 20.0F, 2.5F)));
			}
		}
	}
	
    public boolean hasTrail() 
    {
        return this.trailPointer != -1;
    }
	
    public void tickTrail() 
    {
        Vec3 currentPosition = this.position();
        if(this.trailPointer == -1) 
        {
            for(int i = 0; i < this.trailPositions.length; i++)
            {
            	this.trailPositions[i] = currentPosition;
            }
        }
        if(++this.trailPointer == this.trailPositions.length)
        {
        	this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = currentPosition;
    }
	
    @Override
    public Vec3 getTrailPosition(int pointer, float partialTick)
    {
        if(this.isRemoved())
        {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }
	
	@Override
	protected void onHit(HitResult p_37260_)
	{
		super.onHit(p_37260_);
		if(this.getOwner() != null)
		{
        	AnimeShaderEffects.addEffect(this.level, "Explosion", this.position(), 90, 15.0F);
			List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(12.5F), t -> t != this.getOwner() && !t.isAlliedTo(this.getOwner()) && !(t instanceof EntityReze));
			list.forEach(t -> 
			{
				if(t.hurt(this.damageSources().explosion(this.getOwner(), this.getOwner()), 50.0F))
				{
		    		Vec3 motion = AnimeUtil.fromToVector(this.position(), t.position().add(0, 1, 0), 1.5F);
					t.setSecondsOnFire(5);
		    		t.setDeltaMovement(motion.x, motion.y, motion.z);
		    		if(t instanceof ServerPlayer player)
		    		{
		    			player.connection.send(new ClientboundSetEntityMotionPacket(t));
		    		}
				}
			});
			this.discard();
		}
	}
}
