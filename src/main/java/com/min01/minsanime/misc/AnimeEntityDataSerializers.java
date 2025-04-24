package com.min01.minsanime.misc;

import com.min01.minsanime.MinsAnime;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AnimeEntityDataSerializers
{
	public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MinsAnime.MODID);
	
	public static final RegistryObject<EntityDataSerializer<Vec3>> VEC3 = SERIALIZERS.register("serializer_vec3", () -> EntityDataSerializer.simple(AnimeEntityDataSerializers::writeVec3, AnimeEntityDataSerializers::readVec3));
	
	public static ByteBuf writeVec3(FriendlyByteBuf buf, Vec3 vec)
	{
		buf.writeDouble(vec.x);
		buf.writeDouble(vec.y);
		buf.writeDouble(vec.z);
		return buf;
	}
	
	public static Vec3 readVec3(ByteBuf buf)
	{
		return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
}
