package com.min01.minsanime.network;

import java.util.function.Supplier;

import com.min01.minsanime.misc.AnimeEntityDataSerializers;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.shader.AnimeShaderEffects.ZoltraakEffect;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class AddZoltraakEffectPacket 
{
	private final ResourceKey<Level> dimension;
	private final String name;
	private final Vec3 pos;
	private final Vec3 endPos;
	private final int lifeTime;
	private final float maxScale;
	private final Vec2 rotation;

	public AddZoltraakEffectPacket(ResourceKey<Level> dimension, String name, Vec3 pos, Vec3 endPos, int lifeTime, float maxScale, Vec2 rotation) 
	{
		this.dimension = dimension;
		this.name = name;
		this.pos = pos;
		this.endPos = endPos;
		this.lifeTime = lifeTime;
		this.maxScale = maxScale;
		this.rotation = rotation;
	}

	public AddZoltraakEffectPacket(FriendlyByteBuf buf)
	{
		this.dimension = buf.readResourceKey(Registries.DIMENSION);
		this.name = buf.readUtf();
		this.pos = AnimeEntityDataSerializers.readVec3(buf);
		this.endPos = AnimeEntityDataSerializers.readVec3(buf);
		this.lifeTime = buf.readInt();
		this.maxScale = buf.readFloat();
		this.rotation = AnimeEntityDataSerializers.readVec2(buf);
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeResourceKey(this.dimension);
		buf.writeUtf(this.name);
		AnimeEntityDataSerializers.writeVec3(buf, this.pos);
		AnimeEntityDataSerializers.writeVec3(buf, this.endPos);
		buf.writeInt(this.lifeTime);
		buf.writeFloat(this.maxScale);
		AnimeEntityDataSerializers.writeVec2(buf, this.rotation);
	}

	public static class Handler 
	{
		public static boolean onMessage(AddZoltraakEffectPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() ->
			{
				if(ctx.get().getDirection().getReceptionSide().isClient())
				{
					AnimeShaderEffects.EFFECTS.add(new ZoltraakEffect(message.dimension, message.name, message.pos, message.endPos, message.lifeTime, message.maxScale, message.rotation));
				}
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
