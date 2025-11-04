package com.min01.minsanime.shader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.min01.minsanime.MinsAnime;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class AnimeShaders implements ResourceManagerReloadListener 
{
	protected static final List<ExtendedPostChain> SHADERS = new ArrayList<>();
	
	protected static ExtendedPostChain EXPLOSION;
	protected static ExtendedPostChain BULLET;

	@Override
	public void onResourceManagerReload(ResourceManager manager)
	{
		this.clear();
		try
		{
			init(manager);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void init(ResourceManager manager) throws IOException
	{
		EXPLOSION = add(new ExtendedPostChain(MinsAnime.MODID, "explosion"));
		BULLET = add(new ExtendedPostChain(MinsAnime.MODID, "bullet"));
	}

	public void clear()
	{
		SHADERS.forEach(PostChain::close);
		SHADERS.clear();
	}

	public static ExtendedPostChain add(ExtendedPostChain shader)
	{
		SHADERS.add(shader);
		return shader;
	}
	
	public static ExtendedPostChain getBullet() 
	{
		return BULLET;
	}
	
	public static ExtendedPostChain getExplosion() 
	{
		return EXPLOSION;
	}
}
