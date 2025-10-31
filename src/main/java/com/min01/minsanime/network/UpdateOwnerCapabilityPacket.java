package com.min01.minsanime.network;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import com.min01.minsanime.capabilities.AnimeCapabilities;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateOwnerCapabilityPacket 
{
	private final UUID entityUUID;
	private final Optional<UUID> ownerUUID;
	
	public UpdateOwnerCapabilityPacket(UUID entityUUID, Optional<UUID> ownerUUID) 
	{
		this.entityUUID = entityUUID;
		this.ownerUUID = ownerUUID;
	}

	public UpdateOwnerCapabilityPacket(FriendlyByteBuf buf)
	{
		this.entityUUID = buf.readUUID();
		this.ownerUUID = buf.readOptional(t -> t.readUUID());
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeOptional(this.ownerUUID, (t, u) -> t.writeUUID(u));
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
						entity.getCapability(AnimeCapabilities.OWNER).ifPresent(cap -> 
						{
							if(message.ownerUUID.isPresent())
							{
								Entity owner = AnimeUtil.getEntityByUUID(level, message.ownerUUID.get());
								cap.setOwner(owner);
							}
							else
							{
								cap.setOwner(null);
							}
						});
					});
				}
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
