package com.min01.minsanime.entity.model;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.projectile.EntityRezeBomb;
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

public class ModelRezeBomb extends EntityModel<EntityRezeBomb>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MinsAnime.MODID, "reze_bomb"), "main");
	private final ModelPart bomb;

	public ModelRezeBomb(ModelPart root)
	{
		this.bomb = root.getChild("bomb");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("bomb", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 21.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(EntityRezeBomb entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		bomb.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}