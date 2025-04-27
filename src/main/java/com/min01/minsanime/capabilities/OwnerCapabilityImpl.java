package com.min01.minsanime.capabilities;

import java.util.UUID;

import com.min01.minsanime.network.AnimeNetwork;
import com.min01.minsanime.network.UpdateOwnerCapabilityPacket;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;

public class OwnerCapabilityImpl implements IOwnerCapability
{
	private Entity entity;
	private UUID ownerUUID;
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		if(this.ownerUUID != null)
		{
			tag.putUUID("OwnerUUID", this.ownerUUID);
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		if(nbt.contains("OwnerUUID"))
		{
			this.ownerUUID = nbt.getUUID("OwnerUUID");
		}
	}

	@Override
	public void setEntity(Entity entity) 
	{
		this.entity = entity;
	}
	
	@Override
	public void setOwner(LivingEntity entity)
	{
		this.ownerUUID = entity.getUUID();
		this.sendUpdatePacket();
	}
	
	@Override
	public Entity getOwner() 
	{
		return AnimeUtil.getEntityByUUID(this.entity.level, this.ownerUUID);
	}
	
	private void sendUpdatePacket() 
	{
		if(this.entity == null)
			return;
		if(!this.entity.level.isClientSide)
		{
			AnimeNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdateOwnerCapabilityPacket(this.entity.getUUID(), this.ownerUUID));
		}
	}
}
