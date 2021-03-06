package exnihiloomnia.blocks.barrels.states.dolls;

import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.client.textures.files.TextureLocator;
import exnihiloomnia.util.Color;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class BarrelStateBlaze extends BarrelState {
	private static String[] description = new String[]{""};
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.blaze";
	}
	
	@Override
	public void render(TileEntityBarrel barrel, double x, double y, double z) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null && fluid.getFluid() != null) {
			GlStateManager.pushMatrix();
			RenderHelper.disableStandardItemLighting();
			
			GlStateManager.disableBlend();

			Minecraft mc = Minecraft.getMinecraft();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			TextureAtlasSprite lava = TextureLocator.find(fluid.getFluid().getStill());

			GlStateManager.translate(x + 0.125d, y, z + 0.125d);
			GlStateManager.scale(0.75d, 1.0d, 0.75d);
			
			if (barrel.getBlockType().getDefaultState().getMaterial().isOpaque()) {
				BarrelRenderer.renderContentsSimple(lava, 1.0d, Color.WHITE);
			}
			else {
				BarrelRenderer.renderContentsComplex(lava, 1.0d, Color.WHITE);
			}
			
			RenderHelper.enableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public int getLuminosity(TileEntityBarrel barrel) {
		FluidStack fluid = barrel.getFluid();

		if (fluid != null)
			return fluid.getFluid().getLuminosity();

		return 0;
	}
	
	@Override
	public String[] getWailaBody(TileEntityBarrel barrel) {
		if (barrel.getTimerStatus() >= 0 && barrel.getTimerStatus() < 1.0d ) {
			description[0] = I18n.format("exnihiloomnia.info.barrel.blaze") + " " + I18n.format("entity.Blaze.name") + " " + String.format("%.0f", barrel.getTimerStatus() * 100) + "%";
			return description;
		}
		
		return null;
	}

	@Override
	public void provideInformation(TileEntityBarrel barrel, ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		info.text("Fabricating Blaze");
		info.progress((int) (barrel.getTimerStatus() * 100), 100);
		info.entity("Blaze");
	}
}
