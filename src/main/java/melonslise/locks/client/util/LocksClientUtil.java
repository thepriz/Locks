package melonslise.locks.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LocksClientUtil
{
	private LocksClientUtil() {}

	/*
	 * 
	 * Rendering
	 * 
	 */

	public static void drawTexturedRectangle(MatrixStack mtx, float x, float y, int u, int v, int width, int height, int texWidth, int texHeight)
	{
		Matrix4f last = mtx.getLast().getMatrix();
		float f = 1f / (float) texWidth;
		float f1 = 1f / (float) texHeight;
		BufferBuilder buf = Tessellator.getInstance().getBuffer();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.pos(last, x, y + (float) height, 0f).tex(((float) u * f), ((float) (v + height) * f1)).endVertex();
		buf.pos(last, x + (float) width, y + (float) height, 0f).tex(((float) (u + width) * f), ((float) (v + height) * f1)).endVertex();
		buf.pos(last, x + (float) width, y, 0f).tex(((float) (u + width) * f),  ((float) v * f1)).endVertex();
		buf.pos(x, y, 0f).tex(((float) u * f), ((float) v * f1)).endVertex();
		buf.finishDrawing();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.draw(buf);
	}

	public static ClippingHelper getClippingHelper(MatrixStack mtx, Matrix4f proj, ActiveRenderInfo info)
	{
		ClippingHelper ch = Minecraft.getInstance().worldRenderer.debugFixedClippingHelper;
		if(ch != null)
			return ch;
		ch = new ClippingHelper(mtx.getLast().getMatrix(), proj);
		Vector3d pos = info.getProjectedView();
		ch.setCameraPosition(pos.x, pos.y, pos.z);
		return ch;
	}

	/*
	 * 
	 * Animation
	 * 
	 */

	public static float lerp(float start, float end, float progress)
	{
		return start + (end - start) * progress;
	}

	public static double lerp(double start, double end, double progress)
	{
		return start + (end - start) * progress;
	}

	/*
	 * FIXME 2d bezier
	 * Implement 2d cubic bezier function
	 * https://stackoverflow.com/questions/11696736/recreating-css3-transitions-cubic-bezier-curve
	 * https://math.stackexchange.com/questions/26846/is-there-an-explicit-form-for-cubic-b%C3%A9zier-curves
	 * https://www.gamedev.net/forums/topic/572263-bezier-curve-for-animation/
	 * https://math.stackexchange.com/questions/2571471/understanding-of-cubic-b%C3%A9zier-curves-in-one-dimension
	 */
	public static float cubicBezier1d(float anchor1, float anchor2, float progress)
	{
		float oneMinusP = 1f - progress;
		return 3 * oneMinusP * oneMinusP * progress * anchor1 + 3 * oneMinusP * progress * progress * anchor2 + progress * progress * progress;
	}
}