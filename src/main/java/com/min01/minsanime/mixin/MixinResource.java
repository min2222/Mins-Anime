package com.min01.minsanime.mixin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.animedp.AnimeUtil;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;

@Mixin(Resource.class)
public class MixinResource 
{
	@Unique
	private IoSupplier<InputStream> stream;
	
    @Inject(method = "<init>(Lnet/minecraft/server/packs/PackResources;Lnet/minecraft/server/packs/resources/IoSupplier;Lnet/minecraft/server/packs/resources/IoSupplier;)V", at = @At("TAIL"), cancellable = true)
    private void init(PackResources p_250802_, IoSupplier<InputStream> p_248585_, IoSupplier<ResourceMetadata> p_250094_, CallbackInfo ci)
    {
    	this.stream = p_248585_;
    }
    
    @Inject(method = "<init>(Lnet/minecraft/server/packs/PackResources;Lnet/minecraft/server/packs/resources/IoSupplier;)V", at = @At("TAIL"), cancellable = true)
    private void init2(PackResources p_250372_, IoSupplier<InputStream> p_248749_, CallbackInfo ci)
    {
    	this.stream = p_248749_;
    }
	
    @Inject(method = "open", at = @At("HEAD"), cancellable = true)
    private void open(CallbackInfoReturnable<InputStream> cir) throws IOException 
    {
        InputStream stream = this.stream.get();
        byte[] data = stream.readAllBytes();
        if(data.length > 8 && new String(data, 0, 8, StandardCharsets.UTF_8).equals(AnimeUtil.getMagicHeader())) 
        {
            try 
            {
                ByteArrayInputStream decrypted = new ByteArrayInputStream(AnimeUtil.decryptResource(data, AnimeUtil.generateToken()));
                cir.setReturnValue(decrypted);
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            }
        }
    }
}