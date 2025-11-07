package com.min01.minsanime.misc;

import com.min01.minsanime.MinsAnime;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AnimeEntityDataSerializers
{
	public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MinsAnime.MODID);
	
	public static final RegistryObject<EntityDataSerializer<Vec3>> VEC3 = SERIALIZERS.register("serializer_vec3", () -> EntityDataSerializer.simple(AnimeEntityDataSerializers::writeVec3, AnimeEntityDataSerializers::readVec3));
	public static final RegistryObject<EntityDataSerializer<Vec2>> VEC2 = SERIALIZERS.register("serializer_vec2", () -> EntityDataSerializer.simple(AnimeEntityDataSerializers::writeVec2, AnimeEntityDataSerializers::readVec2));
	
	public static ByteBuf writeVec3(FriendlyByteBuf buf, Vec3 vec3)
	{
		buf.writeDouble(vec3.x);
		buf.writeDouble(vec3.y);
		buf.writeDouble(vec3.z);
		return buf;
	}
	
	public static Vec3 readVec3(ByteBuf buf)
	{
		return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
	
	public static ByteBuf writeVec2(FriendlyByteBuf buf, Vec2 vec2)
	{
		buf.writeFloat(vec2.x);
		buf.writeFloat(vec2.y);
		return buf;
	}
	
	public static Vec2 readVec2(ByteBuf buf)
	{
		return new Vec2(buf.readFloat(), buf.readFloat());
	}
}
