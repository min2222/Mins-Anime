package com.min01.minsanime.network;

import java.util.function.Supplier;

import com.min01.minsanime.misc.AnimeEntityDataSerializers;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.shader.AnimeShaderEffects.ShaderEffect;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class AddShaderEffectPacket 
{
	private final String name;
	private final Vec3 pos;
	private final int lifeTime;
	private final float scale;
	private final Vec3 color;

	public AddShaderEffectPacket(String name, Vec3 pos, int lifeTime, float scale, Vec3 color) 
	{
		this.name = name;
		this.pos = pos;
		this.lifeTime = lifeTime;
		this.scale = scale;
		this.color = color;
	}

	public AddShaderEffectPacket(FriendlyByteBuf buf)
	{
		this.name = buf.readUtf();
		this.pos = AnimeEntityDataSerializers.readVec3(buf);
		this.lifeTime = buf.readInt();
		this.scale = buf.readFloat();
		this.color = AnimeEntityDataSerializers.readVec3(buf);
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUtf(this.name);
		AnimeEntityDataSerializers.writeVec3(buf, this.pos);
		buf.writeInt(this.lifeTime);
		buf.writeFloat(this.scale);
		AnimeEntityDataSerializers.writeVec3(buf, this.color);
	}

	public static class Handler 
	{
		public static boolean onMessage(AddShaderEffectPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() ->
			{
				if(ctx.get().getDirection().getReceptionSide().isClient())
				{
					AnimeShaderEffects.EFFECTS.add(new ShaderEffect(message.name, message.pos, message.lifeTime, message.scale, message.color));
				}
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
