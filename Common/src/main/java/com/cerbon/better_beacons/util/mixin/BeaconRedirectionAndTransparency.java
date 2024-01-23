package com.cerbon.better_beacons.util.mixin;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.advancement.BBCriteriaTriggers;
import com.cerbon.better_beacons.mixin.accessor.BeaconBeamSectionAccessor;
import com.cerbon.better_beacons.mixin.accessor.BeaconBlockEntityAccessor;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.BBUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class BeaconRedirectionAndTransparency {
    public static int horizontalMoveLimit = BetterBeacons.config.beaconBeam.horizontalMoveLimit;
    public static boolean allowRedirecting = BetterBeacons.config.beaconBeam.allowRedirecting;
    public static boolean allowTintedGlassTransparency = BetterBeacons.config.beaconBeam.allowTransparency;

    // The value that comes out of this is fed onto a constant for the FOR loop that
    // computes the beacon segments, so we return 0 to run that code, or MAX_VALUE to not
    public static int tickBeacon(BeaconBlockEntity beacon) {
        BeaconBlockEntityAccessor beaconAccessor = ((BeaconBlockEntityAccessor) beacon);

        Level level = beacon.getLevel();
        BlockPos beaconPos = beacon.getBlockPos();
        BlockPos currPos = beaconPos;

        int i = beaconPos.getX();
        int j = beaconPos.getY();
        int k = beaconPos.getZ();

        int horizontalMoves = horizontalMoveLimit;
        int targetHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE, beaconPos.getX(), beaconPos.getZ());

        boolean broke = false;
        boolean didRedirection = false;

        beaconAccessor.checkingBeamSections().clear();

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
                    ((BeaconBeamSectionAccessor) currSegment).setHeight(heightmapVal + 1000);
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
            float[] targetColor = getBeaconColorMultiplier(blockstate);
            float targetAlpha = -1;

            if(allowTintedGlassTransparency) {
                if(block.defaultBlockState().is(BBConstants.BEACON_TRANSPARENCY)) {
                    targetAlpha = (alpha < 0.3F ? 0F : (alpha / 2F));

                    if (targetAlpha <= 0)
                        for(ServerPlayer serverplayer : BBUtils.getPlayersNearBeacon(beacon.getLevel(), i, j, k))
                            BBCriteriaTriggers.INVISIBLE_BEAM.trigger(serverplayer);
                }
            }

            if(isRedirectingBlock(block) && allowRedirecting) {
                Direction dir = blockstate.getValue(BlockStateProperties.FACING);
                if(dir == currSegment.dir)
                    currSegment.increaseHeight();
                else {
                    beaconAccessor.checkingBeamSections().add(currSegment);

                    targetColor = currColor;

                    currColor = new float[]{(currColor[0] + targetColor[0] * 3) / 4.0F, (currColor[1] + targetColor[1] * 3) / 4.0F, (currColor[2] + targetColor[2] * 3) / 4.0F};
                    alpha = 1F;
                    didRedirection = true;
                    lastDir = currSegment.dir;
                    currSegment = new ExtendedBeamSegment(dir, currPos.subtract(beaconPos), currColor, alpha);
                }
            } else if(targetColor != null || targetAlpha != -1) {
                if(Arrays.equals(targetColor, currColor) && targetAlpha == alpha)
                    currSegment.increaseHeight();
                else {
                    beaconAccessor.checkingBeamSections().add(currSegment);

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
            beaconAccessor.checkingBeamSections().add(currSegment);
            beaconAccessor.setLastCheckY(targetHeight + 1);
        } else {
            beacon.getUpdateTag().putBoolean(tag, false);

            beaconAccessor.checkingBeamSections().clear();
            beaconAccessor.setLastCheckY(targetHeight);
        }

        if(!beacon.getUpdateTag().getBoolean(tag) && didRedirection && !beaconAccessor.checkingBeamSections().isEmpty()) {
            beacon.getUpdateTag().putBoolean(tag, true);

            for(ServerPlayer serverplayer : BBUtils.getPlayersNearBeacon(beacon.getLevel(), i, j, k))
                BBCriteriaTriggers.REDIRECT_BEACON.trigger(serverplayer);
        }

        return Integer.MAX_VALUE;
    }

    private static boolean isRedirectingBlock(Block block) {
        return block.defaultBlockState().is(BBConstants.BEACON_REDIRECT);
    }

    private static float[] getBeaconColorMultiplier(BlockState blockState) {
        if (blockState.getBlock() instanceof BeaconBeamBlock beaconBeamBlock)
            return beaconBeamBlock.getColor().getTextureDiffuseColors();

        return null;
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
    }
}
