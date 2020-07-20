package melonslise.locks.common.worldgen;

import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import melonslise.locks.common.config.LocksConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class PlacementAtChest extends Placement<NoPlacementConfig>
{
	public PlacementAtChest(Codec<NoPlacementConfig> codec)
	{
		super(codec);
	}

	@Override
	public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator gen, Random rand, NoPlacementConfig cfg, BlockPos pos)
	{
		double ch = LocksConfig.GENERATION_CHANCE.get();
		if(ch == 0d || rand.nextDouble() > ch)
			return Stream.empty();
		IChunk chunk = world.getChunk(pos);
		return chunk.getTileEntitiesPos().stream()
			.filter(tePos ->
			{
				BlockState teState = world.getBlockState(tePos);
				// Prevent from adding double chests twice
				return teState.func_235904_r_().contains(ChestBlock.TYPE) && teState.get(ChestBlock.TYPE) != ChestType.RIGHT;
			});
	}
}