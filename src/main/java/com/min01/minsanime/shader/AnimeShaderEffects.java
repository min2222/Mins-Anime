package com.min01.minsanime.shader;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import com.min01.minsanime.network.AddShaderEffectPacket;
import com.min01.minsanime.network.AddZoltraakEffectPacket;
import com.min01.minsanime.network.AnimeNetwork;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class AnimeShaderEffects 
{
	public static final List<ShaderEffect> EFFECTS = new ArrayList<>();
	
	public static void addEffect(Level level, String name, Vec3 pos, int lifeTime, float scale)
	{
		addEffect(level, name, pos, lifeTime, scale, Vec3.ZERO);
	}
	
	public static void addZoltraak(Level level, Vec3 pos, Vec3 endPos, int lifeTime, float maxScale, Vec2 rotation)
	{
		if(!level.isClientSide)
		{
			AnimeNetwork.sendToAll(new AddZoltraakEffectPacket(level.dimension(), "Zoltraak", pos, endPos, lifeTime, maxScale, rotation));
		}
	}
	
	public static void addEffect(Level level, String name, Vec3 pos, int lifeTime, float scale, Vec3 color)
	{
		if(!level.isClientSide)
		{
			AnimeNetwork.sendToAll(new AddShaderEffectPacket(level.dimension(), name, pos, lifeTime, scale, color));
		}
	}
	
	public static class ZoltraakEffect extends ShaderEffect
	{
		public final Vec3 endPos;
		public final float maxScale;
		public final Vec2 rotation;
		
		public ZoltraakEffect(ResourceKey<Level> dimension, String name, Vec3 pos, Vec3 endPos, int lifeTime, float maxScale, Vec2 rotation) 
		{
			super(dimension, name, pos, lifeTime, 0.0F, Vec3.ZERO);
			this.endPos = endPos;
			this.maxScale = maxScale;
			this.rotation = rotation;
		}
	}
	
	public static class ShaderEffect
	{
		public final ResourceKey<Level> dimension;
		public final String name;
		public final Vec3 pos;
		public final int lifeTime;
		public final float scale;
		public final Vec3 color;
		public int tickCount;
		
	    private int volumeTextureId = -1;
	    private static final int VOLUME_WIDTH = 32;
	    private static final int VOLUME_HEIGHT = 32;
	    private static final int VOLUME_DEPTH = 32;
		
		public ShaderEffect(ResourceKey<Level> dimension, String name, Vec3 pos, int lifeTime, float scale)
		{
			this(dimension, name, pos, lifeTime, scale, Vec3.ZERO);
		}
		
		public ShaderEffect(ResourceKey<Level> dimension, String name, Vec3 pos, int lifeTime, float scale, Vec3 color) 
		{
			this.dimension = dimension;
			this.name = name;
			this.pos = pos;
			this.lifeTime = lifeTime;
			this.scale = scale;
			this.color = color;
		}
		
		public boolean isAlive()
		{
			return this.tickCount++ < this.lifeTime;
		}
		
	    public int getOrCreateVolumeTextureId()
	    {
	        if(this.volumeTextureId == -1)
	        {
	            this.volumeTextureId = TextureUtil.generateTextureId();
	            RenderSystem.bindTexture(this.volumeTextureId);
	            GlStateManager._texParameter(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	            GlStateManager._texParameter(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	            GlStateManager._texParameter(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
	            GlStateManager._texParameter(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	            GlStateManager._texParameter(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	            
	            int byteCount = VOLUME_WIDTH * VOLUME_HEIGHT * VOLUME_DEPTH;
	            ByteBuffer buffer = ByteBuffer.allocateDirect(byteCount);
	            Random random = new Random();
	            for(int i = 0; i < byteCount; i++) 
	            {
	                buffer.put((byte) random.nextInt(256));
	            }
	            buffer.flip();

	            RenderSystem.bindTexture(this.volumeTextureId);
	            RenderSystem.pixelStore(GL11.GL_UNPACK_ALIGNMENT, 1);
	            GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL30.GL_R8, VOLUME_WIDTH, VOLUME_HEIGHT, VOLUME_DEPTH, 0, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, buffer);
	            RenderSystem.pixelStore(GL11.GL_UNPACK_ALIGNMENT, 4);
	        }
	        return this.volumeTextureId;
	    }
	}
}
