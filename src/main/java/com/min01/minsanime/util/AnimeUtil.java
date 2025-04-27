package com.min01.minsanime.util;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Consumer;

import com.min01.minsanime.capabilities.AnimeCapabilities;
import com.min01.minsanime.capabilities.IOwnerCapability;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class AnimeUtil 
{
	//vanilla copy
	public static BlockHitResult clip(Level level, ClipContext p_45548_)
	{
		return BlockGetter.traverseBlocks(p_45548_.getFrom(), p_45548_.getTo(), p_45548_, (p_151359_, p_151360_) -> 
		{
			BlockState blockstate = level.getBlockState(p_151360_);
			FluidState fluidstate = level.getFluidState(p_151360_);
			Vec3 vec3 = p_151359_.getFrom();
			Vec3 vec31 = p_151359_.getTo();
			VoxelShape voxelshape = p_151359_.getBlockShape(blockstate, level, p_151360_);
			BlockHitResult blockhitresult = level.clipWithInteractionOverride(vec3, vec31, p_151360_, voxelshape, blockstate);
			VoxelShape voxelshape1 = p_151359_.getFluidShape(fluidstate, level, p_151360_);
			BlockHitResult blockhitresult1 = voxelshape1.clip(vec3, vec31, p_151360_);
			double d0 = blockhitresult == null ? Double.MAX_VALUE : p_151359_.getFrom().distanceToSqr(blockhitresult.getLocation());
			double d1 = blockhitresult1 == null ? Double.MAX_VALUE : p_151359_.getFrom().distanceToSqr(blockhitresult1.getLocation());
			return d0 <= d1 ? blockhitresult : blockhitresult1;
		}, (p_275153_) ->
		{
			Vec3 vec3 = p_275153_.getFrom().subtract(p_275153_.getTo());
			return BlockHitResult.miss(p_275153_.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(p_275153_.getTo()));
		});
	}
	
	public static Entity getOwner(Entity entity)
	{
		LazyOptional<IOwnerCapability> cap = entity.getCapability(AnimeCapabilities.OWNER);
		if(entity.getCapability(AnimeCapabilities.OWNER).isPresent())
		{
			return cap.resolve().get().getOwner();
		}
		return null;
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

        return Vec3.atCenterOf(blockpos);
    }
	
	public static Vec3 fromToVector(Vec3 from, Vec3 to)
	{
		Vec3 motion = to.subtract(from).normalize();
		return motion;
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
	public static <T extends Entity> T getEntityByUUID(Level level, UUID uuid)
	{
		Method m = ObfuscationReflectionHelper.findMethod(Level.class, "m_142646_");
		try 
		{
			LevelEntityGetter<Entity> entities = (LevelEntityGetter<Entity>) m.invoke(level);
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
