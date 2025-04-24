package com.min01.minsanime.particle;

import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SparkParticle extends AbstractTrailParticle
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(MinsAnime.MODID, "textures/particle/spark.png");
	public static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(MinsAnime.MODID, "textures/effect/trail.png");
	
    public SparkParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) 
    {
		super(world, x, y, z, xd, yd, zd);
        this.alpha = 1.0F;
        this.hasPhysics = true;
        this.gravity = 0.1F;
        this.lifetime = 10;
    }

    @Override
    public int getLightColor(float partialTicks) 
    {
        return LightTexture.FULL_BLOCK;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick)
    {
        super.render(vertexConsumer, camera, partialTick);
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.zo, this.z) - vec3.z());
        Quaternionf quaternion;
        if(this.roll == 0.0F)
        {
            quaternion = camera.rotation();
        } 
        else
        {
            quaternion = new Quaternionf(camera.rotation());
            float f3 = Mth.lerp(partialTick, this.oRoll, this.roll);
            quaternion.mul(Axis.ZP.rotation(f3));
        }

        MultiBufferSource.BufferSource multibuffersource$buffersource = AnimeClientUtil.MC.renderBuffers().bufferSource();
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(RenderType.eyes(TEXTURE));

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = 0.3F;

        for(int i = 0; i < 4; ++i) 
        {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        float alpha = Mth.clamp(1.0F - this.age / (float) this.lifetime, 0.0F, 1.0F);
        PoseStack posestack = new PoseStack();
        PoseStack.Pose posestack$pose = posestack.last();
        Matrix3f matrix3f = posestack$pose.normal();
        vertexconsumer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f8, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BLOCK).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f8, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BLOCK).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f7, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BLOCK).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f7, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BLOCK).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        multibuffersource$buffersource.endBatch();
    }

    @Override
    public float getTrailHeight() 
    {
        return 0.2F;
    }

    @Override
    public ResourceLocation getTrailTexture()
    {
        return TRAIL_TEXTURE;
    }

    @Override
    public int sampleCount() 
    {
        return Math.min(10, this.lifetime - this.age);
    }
    
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		@Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) 
		{
            SparkParticle particle = new SparkParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            Vec3 color = Vec3.fromRGB24(16765696);
            particle.trailR = (float) color.x;
            particle.trailG = (float) color.y;
            particle.trailB = (float) color.z;
            return particle;
		}
	}
}
