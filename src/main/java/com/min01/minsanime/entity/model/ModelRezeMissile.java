package com.min01.minsanime.entity.model;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.projectile.EntityRezeMissile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelRezeMissile extends EntityModel<EntityRezeMissile>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MinsAnime.MODID, "reze_missile"), "main");
	private final ModelPart missile;

	public ModelRezeMissile(ModelPart root) 
	{
		this.missile = root.getChild("missile");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition missile = partdefinition.addOrReplaceChild("missile", CubeListBuilder.create().texOffs(0, 34).addBox(-5.0F, -11.0F, -15.0F, 10.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -12.0F, -11.0F, 12.0F, 12.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(5, 52).addBox(-0.5F, -15.0F, 8.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		missile.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(5, 52).mirror().addBox(0.5F, -5.0F, -1.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -5.0F, 9.0F, 0.0F, 0.0F, -1.5708F));

		missile.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(5, 52).addBox(-1.5F, -5.0F, -1.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -5.0F, 9.0F, 0.0F, 0.0F, 1.5708F));

		missile.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(5, 52).addBox(-1.5F, -5.0F, -1.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 9.0F, 0.0F, 0.0F, 3.1416F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityRezeMissile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		missile.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}