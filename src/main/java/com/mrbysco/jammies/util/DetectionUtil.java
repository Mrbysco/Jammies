package com.mrbysco.jammies.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.JukeboxBlock;

import java.util.List;

public class DetectionUtil {
	public static boolean closeToJukebox(LivingEntity livingEntity) {
		final BlockPos pos = livingEntity.blockPosition();
		final int offset = 5;
		final Level level = livingEntity.level();
		List<BlockPos> posList = BlockPos.betweenClosedStream(
				pos.offset(-offset, -offset, -offset),
				pos.offset(offset, offset, offset)).map(BlockPos::immutable).toList();

		for (BlockPos blockPos : posList) {
			if (level.getBlockState(blockPos).getBlock() instanceof JukeboxBlock && level.getBlockState(blockPos).getValue(JukeboxBlock.HAS_RECORD)) {
				return true;
			}
		}

		return false;
	}
}
