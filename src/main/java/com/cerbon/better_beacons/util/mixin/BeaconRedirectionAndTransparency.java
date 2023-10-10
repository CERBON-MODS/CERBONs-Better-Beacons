package com.cerbon.better_beacons.util.mixin;

import com.cerbon.better_beacons.advancement.BBCriteriaTriggers;
import com.cerbon.better_beacons.config.BBCommonConfigs;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.BBUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import vazkii.quark.content.world.block.CorundumClusterBlock;
import vazkii.quark.content.world.module.CorundumModule;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class BeaconRedirectionAndTransparency {
    public static int horizontalMoveLimit = BBCommonConfigs.HORIZONTAL_MOVE_LIMIT.get();
    public static boolean allowRedirecting = BBCommonConfigs.ENABLE_BEACON_BEAM_REDIRECTION.get();
    public static boolean allowTintedGlassTransparency = BBCommonConfigs.ENABLE_BEACON_BEAM_TRANSPARENCY.get();

    // The value that comes out of this is fed onto a constant for the FOR loop that
    // computes the beacon segments, so we return 0 to run that code, or MAX_VALUE to not
    public static int tickBeacon(BeaconBlockEntity beacon) {
        Level level = beacon.getLevel();
        BlockPos beaconPos = beacon.getBlockPos();
        BlockPos currPos = beaconPos;

        int i = beaconPos.getX();
        int j = beaconPos.getY();
        int k = beaconPos.getZ();

        int horizontalMoves = horizontalMoveLimit;
        int targetHeight = Objects.requireNonNull(level).getHeight(Heightmap.Types.WORLD_SURFACE, beaconPos.getX(), beaconPos.getZ());

        boolean broke = false;
        boolean didRedirection = false;

        beacon.checkingBeamSections.clear();

        float[] currColor = new float[] { 1, 1, 1 };
        float alpha = 1F;

        Direction lastDir = null;
        ExtendedBeamSegment currSegment = new ExtendedBeamSegment(Direction.UP, Vec3i.ZERO, currColor, alpha);

        Collection<BlockPos> seenPositions = new HashSet<>();
        boolean hardColorSet = false;

        while(level.isInWorldBounds(currPos) && horizontalMoves > 0) {
            if(currSegment.dir == Direction.UP && currSegment.dir != lastDir) {
                int heightmapVal = level.getHeight(Heightmap.Types.WORLD_SURFACE, currPos.getX(), currPos.getZ());
                if(heightmapVal == (currPos.getY() + 1)) {
                    currSegment.setHeight(heightmapVal + 1000);
                    break;
                }


                lastDir = currSegment.dir;
            }


            currPos = currPos.relative(currSegment.dir);
            if(currSegment.dir.getAxis().isHorizontal())
                horizontalMoves--;
            else horizontalMoves = horizontalMoveLimit;


            BlockState blockstate = level.getBlockState(currPos);
            Block block = blockstate.getBlock();
            float[] targetColor = blockstate.getBeaconColorMultiplier(level, currPos, beaconPos);
            float targetAlpha = -1;

            if(allowTintedGlassTransparency) {
                if(block.defaultBlockState().is(BBConstants.BEACON_TRANSPARENCY)) {
                    targetAlpha = (alpha < 0.3F ? 0F : (alpha / 2F));

                    if (targetAlpha <= 0)
                        for(ServerPlayer serverplayer : Objects.requireNonNull(beacon.getLevel()).getEntitiesOfClass(ServerPlayer.class, (new AABB(i, j, k, i, j - 4, k)).inflate(10.0D, 5.0D, 10.0D)))
                            BBCriteriaTriggers.INVISIBLE_BEAM.trigger(serverplayer);
                }
            }

            if(isRedirectingBlock(block) && allowRedirecting) {
                Direction dir = blockstate.getValue(BlockStateProperties.FACING);
                if(dir == currSegment.dir)
                    currSegment.increaseHeight();
                else {
                    beacon.checkingBeamSections.add(currSegment);

                    targetColor = getTargetColor(block);
                    if(targetColor[0] == 1F && targetColor[1] == 1F && targetColor[2] == 1F)
                        targetColor = currColor;

                    float[] mixedColor = new float[]{(currColor[0] + targetColor[0] * 3) / 4.0F, (currColor[1] + targetColor[1] * 3) / 4.0F, (currColor[2] + targetColor[2] * 3) / 4.0F};
                    currColor = mixedColor;
                    alpha = 1F;
                    didRedirection = true;
                    lastDir = currSegment.dir;
                    currSegment = new ExtendedBeamSegment(dir, currPos.subtract(beaconPos), currColor, alpha);
                }
            } else if(targetColor != null || targetAlpha != -1) {
                if(Arrays.equals(targetColor, currColor) && targetAlpha == alpha)
                    currSegment.increaseHeight();
                else {
                    beacon.checkingBeamSections.add(currSegment);

                    float[] mixedColor = currColor;
                    if(targetColor != null) {
                        mixedColor = new float[]{(currColor[0] + targetColor[0]) / 2.0F, (currColor[1] + targetColor[1]) / 2.0F, (currColor[2] + targetColor[2]) / 2.0F};

                        if(!hardColorSet) {
                            mixedColor = targetColor;
                            hardColorSet = true;
                        }

                        currColor = mixedColor;
                    }

                    if(targetAlpha != -1)
                        alpha = targetAlpha;

                    lastDir = currSegment.dir;
                    currSegment = new ExtendedBeamSegment(currSegment.dir, currPos.subtract(beaconPos), mixedColor, alpha);
                }
            } else {
                boolean bedrock = blockstate.is(BBConstants.BEACON_TRANSPARENT); //Bedrock blocks don't stop beacon beams

                if(!bedrock && blockstate.getLightBlock(level, currPos) >= 15) {
                    broke = true;
                    break;
                }

                currSegment.increaseHeight();

                if(bedrock)
                    continue;
            }

            boolean added = seenPositions.add(currPos);
            if(!added) {
                broke = true;
                break;
            }

        }

        if(horizontalMoves == 0 || currPos.getY() <= level.getMinBuildHeight())
            broke = true;

        final String tag = "better_beacons:redirected";

        if(!broke) {
            beacon.checkingBeamSections.add(currSegment);
            beacon.lastCheckY = targetHeight + 1;
        } else {
            beacon.getPersistentData().putBoolean(tag, false);

            beacon.checkingBeamSections.clear();
            beacon.lastCheckY = targetHeight;
        }

        if(!beacon.getPersistentData().getBoolean(tag) && didRedirection && !beacon.checkingBeamSections.isEmpty()) {
            beacon.getPersistentData().putBoolean(tag, true);

            for(ServerPlayer serverplayer : Objects.requireNonNull(beacon.getLevel()).getEntitiesOfClass(ServerPlayer.class, (new AABB(i, j, k, i, j - 4, k)).inflate(10.0D, 5.0D, 10.0D)))
                BBCriteriaTriggers.REDIRECT_BEACON.trigger(serverplayer);
        }

        return Integer.MAX_VALUE;
    }

    private static boolean isRedirectingBlock(Block block) {
        if (BBUtils.isModLoaded(BBConstants.QUARK))
            if (CorundumModule.staticEnabled)
                return block instanceof CorundumClusterBlock || block.defaultBlockState().is(BBConstants.BEACON_REDIRECT);

        return block.defaultBlockState().is(BBConstants.BEACON_REDIRECT);
    }

    private static float[] getTargetColor(Block block) {
        if (BBUtils.isModLoaded(BBConstants.QUARK))
            return block instanceof CorundumClusterBlock cc? cc.base.colorComponents : new float[] { 1F, 1F, 1F };

        return new float[] { 1F, 1F, 1F };
    }

    public static class ExtendedBeamSegment extends BeaconBlockEntity.BeaconBeamSection {

        public final Direction dir;
        public final Vec3i offset;
        public final float alpha;

        public ExtendedBeamSegment(Direction dir, Vec3i offset, float[] colorsIn, float alpha) {
            super(colorsIn);
            this.offset = offset;
            this.dir = dir;
            this.alpha = alpha;
        }

        @Override
        public void increaseHeight() { // increase visibility
            super.increaseHeight();
        }

        public void setHeight(int target) {
            height = target;
        }

    }
}
