package com.min01.minsanime.network;

import java.util.function.Supplier;

import com.min01.minsanime.entity.living.EntityFrieren;
import com.min01.minsanime.entity.living.EntityFrieren.Laser;
import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class UpdateZoltraakPacket 
{
	private final int entityId;
	private final Laser zoltraak;

	public UpdateZoltraakPacket(EntityFrieren entity, Laser zoltraak) 
	{
		this.entityId = entity.getId();
		this.zoltraak = zoltraak;
	}

	public UpdateZoltraakPacket(FriendlyByteBuf buf)
	{
		this.entityId = buf.readInt();
		this.zoltraak = Laser.read(buf);
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeInt(this.entityId);
		Laser.write(this.zoltraak, buf);
	}

	public static class Handler 
	{
		public static boolean onMessage(UpdateZoltraakPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() ->
			{
				AnimeUtil.getClientLevel(t -> 
				{
					if(t.getEntity(message.entityId) instanceof EntityFrieren frieren)
					{
						frieren.zoltraak.add(message.zoltraak);
					}
				});
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
