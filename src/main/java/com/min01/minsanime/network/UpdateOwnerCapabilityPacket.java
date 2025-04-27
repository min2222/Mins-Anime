package com.min01.minsanime.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.minsanime.capabilities.AnimeCapabilities;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateOwnerCapabilityPacket 
{
	private final UUID entityUUID;
	private final UUID ownerUUID;
	
	public UpdateOwnerCapabilityPacket(UUID entityUUID, UUID ownerUUID) 
	{
		this.entityUUID = entityUUID;
		this.ownerUUID = ownerUUID;
	}

	public UpdateOwnerCapabilityPacket(FriendlyByteBuf buf)
	{
		this.entityUUID = buf.readUUID();
		this.ownerUUID = buf.readUUID();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeUUID(this.ownerUUID);
	}
	
	public static class Handler 
	{
		public static boolean onMessage(UpdateOwnerCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) 
		{
			ctx.get().enqueueWork(() ->
			{
				if(ctx.get().getDirection().getReceptionSide().isClient())
				{
					AnimeUtil.getClientLevel(level -> 
					{
						Entity entity = AnimeUtil.getEntityByUUID(level, message.entityUUID);
						if(entity instanceof LivingEntity living) 
						{
							living.getCapability(AnimeCapabilities.OWNER).ifPresent(cap -> 
							{
								Entity owner = AnimeUtil.getEntityByUUID(living.level, message.ownerUUID);
								if(owner instanceof LivingEntity livingOwner)
								{
									cap.setOwner(livingOwner);
								}
							});
						}
					});
				}
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
