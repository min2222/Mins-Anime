package com.min01.minsanime.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

import org.joml.Math;

import com.min01.minsanime.capabilities.AnimeCapabilities;
import com.min01.minsanime.capabilities.IOwnerCapability;
import com.min01.minsanime.capabilities.OwnerCapabilityImpl;
import com.min01.minsanime.entity.ai.goal.EsdeathIceBarrierGoal;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class AnimeUtil 
{
	public static final Method GET_ENTITY = ObfuscationReflectionHelper.findMethod(Level.class, "m_142646_");
	
	@SuppressWarnings("unchecked")
	public static Iterable<Entity> getAllEntities(Level level)
	{
		try 
		{
			LevelEntityGetter<Entity> entities = (LevelEntityGetter<Entity>) GET_ENTITY.invoke(level);
			return entities.getAll();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
    public static void performCheck(Entity arrow, PathfinderMob mob)
    {
        if(!(arrow.level instanceof ServerLevel)) 
        	return;
        float width = arrow.getBbWidth() + 0.3F;
        Vec3 arrowMotion = arrow.getDeltaMovement();
        double vH = Math.sqrt(arrowMotion.x * arrowMotion.x + arrowMotion.z * arrowMotion.z);
        Vec3 arrowDirection = new Vec3(arrowMotion.x / vH, arrowMotion.y / vH, arrowMotion.z / vH);
        int rangeVertical = 16;
        int rangeHorizontal = 24;
        int distanceY = Math.abs((int) mob.position().y - (int) arrow.position().y);
        if(distanceY <= rangeVertical) 
        {
            double distanceX = mob.position().x - arrow.position().x;
            double distanceZ = mob.position().z - arrow.position().z;
            double distanceH = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
            if(distanceH <= rangeHorizontal)
            {
                double cos = (arrowDirection.x * distanceX + arrowDirection.z * distanceZ) / distanceH;
                double sin = Math.sqrt(1 - cos * cos);
                if(width >= distanceH * sin)
                {
                	setDirection(mob, arrowDirection);
                }
            }
        }
    }
    
    public static void setDirection(PathfinderMob entity, Vec3 arrowDirection) 
    {
        for(WrappedGoal task : new ArrayList<>(entity.goalSelector.getAvailableGoals())) 
        {
            if(task.getGoal() instanceof EsdeathIceBarrierGoal goal) 
            {
                goal.setDirection(arrowDirection);
            }
        }
    }
	
    @SuppressWarnings("deprecation")
	public static BlockPos getGroundPos(BlockGetter pLevel, double pX, double startY, double pZ)
    {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(pX, startY, pZ);
        do
        {
        	blockpos$mutable.move(Direction.DOWN);
        }
        while((pLevel.getBlockState(blockpos$mutable).isAir() || pLevel.getBlockState(blockpos$mutable).liquid() || !pLevel.getBlockState(blockpos$mutable).isCollisionShapeFullBlock(pLevel, blockpos$mutable)) && blockpos$mutable.getY() > pLevel.getMinBuildHeight());

        BlockPos blockpos = blockpos$mutable.below();

        return blockpos;
    }
	
	@SuppressWarnings("deprecation")
	public static Vec3 getGroundPosAbove(BlockGetter pLevel, double pX, double startY, double pZ)
    {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(pX, startY, pZ);
        do
        {
        	blockpos$mutable.move(Direction.DOWN);
        } 
        while((pLevel.getBlockState(blockpos$mutable).isAir() || pLevel.getBlockState(blockpos$mutable).liquid() || !pLevel.getBlockState(blockpos$mutable).isCollisionShapeFullBlock(pLevel, blockpos$mutable)) && blockpos$mutable.getY() > pLevel.getMinBuildHeight());

        BlockPos blockpos = blockpos$mutable.above();

        return Vec3.atBottomCenterOf(blockpos);
    }
	
	public static Vec3 fromToVector(Vec3 from, Vec3 to)
	{
		Vec3 motion = to.subtract(from).normalize();
		return motion;
	}
	
	public static void tickOwner(Entity entity)
	{
		IOwnerCapability cap = entity.getCapability(AnimeCapabilities.OWNER).orElse(new OwnerCapabilityImpl());
		cap.tick(entity);
	}
	
	public static void setOwner(Entity entity, Entity owner)
	{
		IOwnerCapability cap = entity.getCapability(AnimeCapabilities.OWNER).orElse(new OwnerCapabilityImpl());
		cap.setOwner(owner);
	}
    
	public static Entity getOwner(Entity entity)
	{
		IOwnerCapability cap = entity.getCapability(AnimeCapabilities.OWNER).orElse(new OwnerCapabilityImpl());
		return cap.getOwner(entity);
	}
	
	public static Vec3 bezierMotionVector(Vec3 start, Vec3 control, Vec3 end, float t, float speed) 
	{
	    Vec3 derivative = start.subtract(control).scale(2 * (1 - t)).add(end.subtract(control).scale(2 * t));
	    return derivative.normalize().scale(speed);
	}
	
	public static Vec3 fromToVector(Vec3 from, Vec3 to, float scale)
	{
		Vec3 motion = to.subtract(from).normalize();
		return motion.scale(scale);
	}
	
	public static Vec3 fromToPos(Vec3 from, Vec3 to)
	{
		Vec3 pos = to.subtract(from);
		return pos;
	}
	
	public static void getClientLevel(Consumer<Level> consumer)
	{
		LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).filter(ClientLevel.class::isInstance).ifPresent(level -> 
		{
			consumer.accept(level);
		});
	}
	
	@SuppressWarnings("unchecked")
	public static  <T extends Entity> T getEntityByUUID(Level level, UUID uuid)
	{
		try 
		{
			LevelEntityGetter<Entity> entities = (LevelEntityGetter<Entity>) GET_ENTITY.invoke(level);
			return (T) entities.get(uuid);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static Vec3 getSpreadPosition(Level level, Vec3 startPos, Vec3 range)
	{
        double x = startPos.x + (level.random.nextDouble() - level.random.nextDouble()) * range.x + 0.5D;
        double y = startPos.y + (level.random.nextDouble() - level.random.nextDouble()) * range.y + 0.5D;
        double z = startPos.z + (level.random.nextDouble() - level.random.nextDouble()) * range.z + 0.5D;
        return new Vec3(x, y, z);
	}
	
	public static Vec3 getSpreadPosition(Level level, Vec3 startPos, double range)
	{
        double x = startPos.x + (level.random.nextDouble() - level.random.nextDouble()) * range + 0.5D;
        double y = startPos.y + (level.random.nextDouble() - level.random.nextDouble()) * range + 0.5D;
        double z = startPos.z + (level.random.nextDouble() - level.random.nextDouble()) * range + 0.5D;
        return new Vec3(x, y, z);
	}
	
	public static Vec3 getLookPos(Vec2 rotation, Vec3 position, double left, double up, double forwards) 
	{
		Vec2 vec2 = rotation;
		Vec3 vec3 = position;
		float f = Mth.cos((vec2.y + 90.0F) * ((float)Math.PI / 180.0F));
		float f1 = Mth.sin((vec2.y + 90.0F) * ((float)Math.PI / 180.0F));
		float f2 = Mth.cos(-vec2.x * ((float)Math.PI / 180.0F));
		float f3 = Mth.sin(-vec2.x * ((float)Math.PI / 180.0F));
		float f4 = Mth.cos((-vec2.x + 90.0F) * ((float)Math.PI / 180.0F));
		float f5 = Mth.sin((-vec2.x + 90.0F) * ((float)Math.PI / 180.0F));
		Vec3 vec31 = new Vec3((double)(f * f2), (double)f3, (double)(f1 * f2));
		Vec3 vec32 = new Vec3((double)(f * f4), (double)f5, (double)(f1 * f4));
		Vec3 vec33 = vec31.cross(vec32).scale(-1.0D);
		double d0 = vec31.x * forwards + vec32.x * up + vec33.x * left;
		double d1 = vec31.y * forwards + vec32.y * up + vec33.y * left;
		double d2 = vec31.z * forwards + vec32.z * up + vec33.z * left;
		return new Vec3(vec3.x + d0, vec3.y + d1, vec3.z + d2);
	}
	
	public static Vec2 lookAt(Vec3 startPos, Vec3 pos)
	{
		Vec3 vec3 = startPos;
		double d0 = pos.x - vec3.x;
		double d1 = pos.y - vec3.y;
		double d2 = pos.z - vec3.z;
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		float xRot = Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180.0F / (float)Math.PI))));
		float yRot = Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180.0F / (float)Math.PI)) - 90.0F);
	    return new Vec2(xRot, yRot);
	}
	
	public static Vec3 getLookPos(float xRot, float yRot, float yPos, double distance)
	{
		float f = -Mth.sin(yRot * ((float)Math.PI / 180F)) * Mth.cos(xRot * ((float)Math.PI / 180F));
		float f1 = -Mth.sin((xRot + yPos) * ((float)Math.PI / 180F));
		float f2 = Mth.cos(yRot * ((float)Math.PI / 180F)) * Mth.cos(xRot * ((float)Math.PI / 180F));
		return new Vec3(f, f1, f2).scale(distance);
	}
	
	public static float rotlerp(float p_24992_, float p_24993_, float p_24994_)
	{
		float f = Mth.wrapDegrees(p_24993_ - p_24992_);
		
		if(f > p_24994_) 
		{
			f = p_24994_;
		}

		if(f < -p_24994_) 
		{
			f = -p_24994_;
		}

		float f1 = p_24992_ + f;
		
		if(f1 < 0.0F)
		{
			f1 += 360.0F;
		}
		else if(f1 > 360.0F)
		{
			f1 -= 360.0F;
		}
		
		return f1;
	}
}
