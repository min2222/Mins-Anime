package com.min01.minsanime.util;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.min01.minsanime.obj.Face;
import com.min01.minsanime.obj.ObjAnimationDefinition;
import com.min01.minsanime.obj.ObjAnimations;
import com.min01.minsanime.obj.WavefrontObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AnimeClientUtil 
{
	public static final Minecraft MC = Minecraft.getInstance();
	
	public static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
	
    public static void renderFlatQuad(PoseStack stack, VertexConsumer consumer, float size, int packedLightIn) 
    {
        float minU = 0;
        float minV = 0;
        float maxU = 1;
        float maxV = 1;
        PoseStack.Pose matrixstack$entry = stack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, consumer, size, size, 0, minU, minV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, size, -size, 0, minU, maxV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, -size, -size, 0, maxU, maxV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, -size, size, 0, maxU, minV, 1.0F, packedLightIn);
    }
    
    public static void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn)
    {
    	vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
	
	public static void resetPose(WavefrontObject obj)
	{
		obj.x = 0.0F;
		obj.y = 0.0F;
		obj.z = 0.0F;
		obj.xRot = 0.0F;
		obj.yRot = 0.0F;
		obj.zRot = 0.0F;
		obj.xScale = 1.0F;
		obj.yScale = 1.0F;
		obj.zScale = 1.0F;
		obj.xUV = 0.0F;
		obj.yUV = 0.0F;
	}

	public static void animateObj(WavefrontObject obj, AnimationState state, ObjAnimationDefinition definition, float ageInTicks) 
	{
		state.updateTime(ageInTicks, 1.0F);
		state.ifStarted((anim) -> 
		{
			ObjAnimations.animate(obj, definition, anim.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
		});
	}
	
	public static void renderObj(PoseStack stack, MultiBufferSource source, WavefrontObject obj, RenderType renderType, int light)
	{
		stack.pushPose();
		Face.setUvOperator(1, 1, obj.xUV, obj.yUV);
		Face.setMatrix(stack);
		Face.setLightMap(light);
		stack.translate(obj.x / 16.0F, obj.y / 16.0F, obj.z / 16.0F);
		if(obj.xRot != 0.0F || obj.yRot != 0.0F || obj.zRot != 0.0F) 
		{
			stack.mulPose((new Quaternionf()).rotationZYX(obj.zRot, obj.yRot, obj.xRot));
		}
		if(obj.xScale != 1.0F || obj.yScale != 1.0F || obj.zScale != 1.0F)
		{
			stack.scale(obj.xScale, obj.yScale, obj.zScale);
		}
		obj.tessellateAll(source.getBuffer(renderType));
		Face.resetUvOperator();
		Face.resetMatrix();
		Face.resetLightMap();
		stack.popPose();
	}
	
	public static void animateHead(ModelPart head, float netHeadYaw, float headPitch)
	{
		head.yRot += Math.toRadians(netHeadYaw);
		head.xRot += Math.toRadians(headPitch);
	}
	
    public static void drawBox(AABB boundingBox, PoseStack stack, MultiBufferSource bufferIn, Vec3 rgb, int alpha) 
    {
    	drawBox(boundingBox, stack, bufferIn, rgb, LightTexture.FULL_BLOCK, alpha, RenderType.entityCutoutNoCull(new ResourceLocation("textures/block/ice.png")));
    }
    
    //ChatGPT ahh;
    public static void drawCurvedCylinder(PoseStack stack, VertexConsumer buffer, Vec3 start, Vec3 end, float radius, float curveHeight, int curveSteps, int circleSegments, float r, float g, float b, float a)
    {
        Matrix4f matrix = stack.last().pose();

        // Control point for a curved path (simple quadratic bezier)
        Vec3 control = start.add(end).scale(0.1F).add(0, curveHeight / 2, 0);

        List<Vec3> curvePoints = new ArrayList<>();
        List<Vec3> tangents = new ArrayList<>();

        // Generate curve points and tangents
        for(int i = 0; i <= curveSteps; i++) 
        {
            float t = i / (float) curveSteps;

            Vec3 p0 = start.lerp(control, t);
            Vec3 p1 = control.lerp(end, t);
            Vec3 point = p0.lerp(p1, t);
            curvePoints.add(point);

            // Compute tangent using central difference if possible
            float dt = 1.0F / curveSteps;
            Vec3 tangent;
            if(i == 0)
            {
                // Forward difference at start
                float t1 = t + dt;
                Vec3 p0b = start.lerp(control, t1);
                Vec3 p1b = control.lerp(end, t1);
                Vec3 nextPoint = p0b.lerp(p1b, t1);
                tangent = nextPoint.subtract(point).normalize();
            }
            else if(i == curveSteps)
            {
                // Backward difference at end
                float t0 = t - dt;
                Vec3 p0b = start.lerp(control, t0);
                Vec3 p1b = control.lerp(end, t0);
                Vec3 prevPoint = p0b.lerp(p1b, t0);
                tangent = point.subtract(prevPoint).normalize();
            } 
            else 
            {
                // Centered difference
                float t0 = t - dt;
                float t1 = t + dt;
                Vec3 p0a = start.lerp(control, t0);
                Vec3 p1a = control.lerp(end, t0);
                Vec3 prevPoint = p0a.lerp(p1a, t0);

                Vec3 p0b = start.lerp(control, t1);
                Vec3 p1b = control.lerp(end, t1);
                Vec3 nextPoint = p0b.lerp(p1b, t1);

                tangent = nextPoint.subtract(prevPoint).normalize();
            }

            tangents.add(tangent);
        }

        // Draw dome at the start
        Vec3 startDir = curvePoints.get(1).subtract(curvePoints.get(0)).normalize();
        drawHemisphereCap(buffer, matrix, curvePoints.get(0), startDir.scale(-1), radius, circleSegments, r, g, b, a);

        List<Vec3> prevRing = null;

        // Draw cylinder body
        for(int i = 0; i < curvePoints.size(); i++) 
        {
            Vec3 center = curvePoints.get(i);
            Vec3 tangent = tangents.get(i);

            // Orthonormal basis
            Vec3 arbitrary = Math.abs(tangent.y) < 0.99 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
            Vec3 normal = tangent.cross(arbitrary).normalize();
            Vec3 binormal = tangent.cross(normal).normalize();

            // Generate ring
            List<Vec3> ring = new ArrayList<>();
            for(int j = 0; j < circleSegments; j++) 
            {
                float angle = (float) (2 * Math.PI * j / circleSegments);
                Vec3 offset = normal.scale(Math.cos(angle) * radius).add(binormal.scale(Math.sin(angle) * radius));
                ring.add(center.add(offset));
            }

            if(prevRing != null) 
            {
                for(int j = 0; j < circleSegments; j++)
                {
                    Vec3 v0 = prevRing.get(j);
                    Vec3 v1 = prevRing.get((j + 1) % circleSegments);
                    Vec3 v2 = ring.get((j + 1) % circleSegments);
                    Vec3 v3 = ring.get(j);

                    // Two triangles per quad
                    vertex(buffer, matrix, v0, r, g, b, a);
                    vertex(buffer, matrix, v1, r, g, b, a);
                    vertex(buffer, matrix, v2, r, g, b, a);

                    vertex(buffer, matrix, v2, r, g, b, a);
                    vertex(buffer, matrix, v3, r, g, b, a);
                    vertex(buffer, matrix, v0, r, g, b, a);
                }
            }

            prevRing = ring;
        }

        // Draw dome at the end
        Vec3 endDir = curvePoints.get(curvePoints.size() - 1).subtract(curvePoints.get(curvePoints.size() - 2)).normalize();
        drawHemisphereCap(buffer, matrix, curvePoints.get(curvePoints.size() - 1), endDir, radius, circleSegments, r, g, b, a);
    }

    public static void drawHemisphereCap(VertexConsumer buffer, Matrix4f matrix, Vec3 center, Vec3 direction, float radius, int segments, float r, float g, float b, float a)
    {
        Vec3 arbitrary = Math.abs(direction.y) < 0.99 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 normal = direction.cross(arbitrary).normalize();
        Vec3 binormal = direction.cross(normal).normalize();

        int rings = segments / 2; // Half sphere

        for(int i = 0; i < rings; i++) 
        {
            float theta1 = (float) (Math.PI / 2 * i / rings);
            float theta2 = (float) (Math.PI / 2 * (i + 1) / rings);

            float y1 = (float) Math.sin(theta1);
            float y2 = (float) Math.sin(theta2);
            float r1 = (float) Math.cos(theta1);
            float r2 = (float) Math.cos(theta2);

            for(int j = 0; j < segments; j++) 
            {
                float phi1 = (float) (2 * Math.PI * j / segments);
                float phi2 = (float) (2 * Math.PI * (j + 1) / segments);

                Vec3 v00 = direction.scale(y1 * radius).add(normal.scale(r1 * radius * Math.cos(phi1)).add(binormal.scale(r1 * radius * Math.sin(phi1))));
                Vec3 v01 = direction.scale(y1 * radius).add(normal.scale(r1 * radius * Math.cos(phi2)).add(binormal.scale(r1 * radius * Math.sin(phi2))));
                Vec3 v10 = direction.scale(y2 * radius).add(normal.scale(r2 * radius * Math.cos(phi1)).add(binormal.scale(r2 * radius * Math.sin(phi1))));
                Vec3 v11 = direction.scale(y2 * radius).add(normal.scale(r2 * radius * Math.cos(phi2)).add(binormal.scale(r2 * radius * Math.sin(phi2))));

                // Translate all points to the center
                v00 = v00.add(center);
                v01 = v01.add(center);
                v10 = v10.add(center);
                v11 = v11.add(center);

                // Two triangles
                vertex(buffer, matrix, v00, r, g, b, a);
                vertex(buffer, matrix, v01, r, g, b, a);
                vertex(buffer, matrix, v11, r, g, b, a);

                vertex(buffer, matrix, v11, r, g, b, a);
                vertex(buffer, matrix, v10, r, g, b, a);
                vertex(buffer, matrix, v00, r, g, b, a);
            }
        }
    }
    
    public static void vertex(VertexConsumer buffer, Matrix4f matrix, Vec3 pos, float r, float g, float b, float a)
    {
    	buffer.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z).color(r, g, b, a).uv(0, 0).endVertex();
    }
    
    //DeepSeek ahh;
    public static void drawCylinder(PoseStack stack, VertexConsumer buffer, float radius, float height, int segments, float r, float g, float b, float a)
    {
		Matrix4f matrix = stack.last().pose();
		float halfHeight = height / 2;
		
		// Draw side of cylinder
		for(int i = 0; i <= segments; i++) 
		{
			float angle = (float) (2 * Math.PI * i / segments);
			float x = radius * Mth.cos(angle);
			float z = radius * Mth.sin(angle);
			
			// Add vertices for top and bottom edges
			buffer.vertex(matrix, x, halfHeight, z).color(r, g, b, a).uv(0, 0).endVertex();
			buffer.vertex(matrix, x, -halfHeight, z).color(r, g, b, a).uv(0, 0).endVertex();
		}
		
		// Draw top cap
		buffer.vertex(matrix, 0, halfHeight, 0).color(r, g, b, a).uv(0, 0).endVertex();
		for(int i = 0; i <= segments; i++)
		{
			float angle = (float) (2 * Math.PI * i / segments);
			float x = radius * Mth.cos(angle);
			float z = radius * Mth.sin(angle);
			buffer.vertex(matrix, x, halfHeight, z).color(r, g, b, a).uv(0, 0).endVertex();
		}
		
		// Draw bottom cap
		buffer.vertex(matrix, 0, -halfHeight, 0).color(r, g, b, a).uv(0, 0).endVertex();
		for(int i = 0; i <= segments; i++)
		{
			float angle = (float) (2 * Math.PI * i / segments);
			float x = radius * Mth.cos(angle);
			float z = radius * Mth.sin(angle);
			buffer.vertex(matrix, x, -halfHeight, z).color(r, g, b, a).uv(0, 0).endVertex();
		}
    }
    
    public static void drawBox(AABB boundingBox, PoseStack stack, MultiBufferSource bufferIn, Vec3 rgb, int light, int alpha, RenderType renderType) 
    {
        VertexConsumer vertexbuffer = bufferIn.getBuffer(renderType);
        Matrix4f matrix4f = stack.last().pose();
        float maxX = (float) boundingBox.maxX * 0.625F;
        float minX = (float) boundingBox.minX * 0.625F;
        float maxY = (float) boundingBox.maxY * 0.625F;
        float minY = (float) boundingBox.minY * 0.625F;
        float maxZ = (float) boundingBox.maxZ * 0.625F;
        float minZ = (float) boundingBox.minZ * 0.625F;

        float maxU = maxZ - minZ;
        float maxV = maxY - minY;
        float minU = minZ - maxZ;
        float minV = minY - maxY;
        // X+
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1.0F, 0.0F, 0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1.0F, 0.0F, 0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1.0F, 0.0F, 0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1.0F, 0.0F, 0F).endVertex();

        // X-
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(-1.0F, 0.0F, 0.0F).endVertex();


        maxU = maxX - minX;
        maxV = maxY - minY;
        minU = minX - maxX;
        minV = minY - maxY;
        // Z-
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, -1.0F).endVertex();

        // Z+
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();


        maxU = maxZ - minZ;
        maxV = maxX - minX;
        minU = minZ - maxZ;
        minV = minX - maxX;
        // Y+
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        // Y-
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, -1.0F, 0.0F).endVertex();
    }
}
