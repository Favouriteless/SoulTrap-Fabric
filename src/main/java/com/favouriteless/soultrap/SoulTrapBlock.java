/*
 * Copyright (c) 2021. Favouriteless
 * SoulTrap-Fabric, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of SoulTrap-Fabric.
 *
 *     SoulTrap-Fabric is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SoulTrap-Fabric is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SoulTrap-Fabric.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.soultrap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class SoulTrapBlock extends Block {

    public static final Random RANDOM = new Random();

    public static final EntityType<?>[] VALID_ENTITIES = new EntityType[] {
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.BLAZE,
            EntityType.MAGMA_CUBE,
            EntityType.SILVERFISH
    };

    public SoulTrapBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            if(checkValidCage(world, pos)) {
                List<Entity> entities = getTrapEntities(world, pos);
                if(!entities.isEmpty()) {
                    if(entities.size() == 1) {
                        Entity entity = entities.get(0);
                        if(checkValidEntity(entity)) {
                            destroyCage(world, pos);
                            createSpawner(world, pos, (LivingEntity)entity);
                            entity.remove(Entity.RemovalReason.DISCARDED);

                            world.playSound(null, pos, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.MASTER, 1f, 0.3f);
                        } else {
                            player.sendMessage(new LiteralText("Creature is not valid.").formatted(Formatting.RED), false);
                            world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.BLOCKS, 1f, 0.5f);
                        }
                    } else {
                        player.sendMessage(new LiteralText("Too many creatures.").formatted(Formatting.RED), false);
                        world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.BLOCKS, 1f, 0.5f);
                    }
                } else {
                    player.sendMessage(new LiteralText("Creature not found.").formatted(Formatting.RED), false);
                    world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.BLOCKS, 1f, 0.5f);
                }
            } else {
                player.sendMessage(new LiteralText("Cage is not valid").formatted(Formatting.RED), false);
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.BLOCKS, 1f, 0.5f);
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }

    public static boolean checkValidCage(World world, BlockPos pos) {
        BlockPos[] ironBlocks = new BlockPos[] {
                pos.offset(Direction.NORTH).offset(Direction.WEST), // NorthWest
                pos.offset(Direction.NORTH).offset(Direction.EAST), // NorthEast
                pos.offset(Direction.SOUTH).offset(Direction.WEST), // SouthWest
                pos.offset(Direction.SOUTH).offset(Direction.EAST), // SouthEast
                pos.offset(Direction.UP, 3) // Top
        };

        BlockPos[] airBlocks = new BlockPos[] {
                pos.offset(Direction.NORTH),
                pos.offset(Direction.EAST),
                pos.offset(Direction.SOUTH),
                pos.offset(Direction.WEST),
                pos.offset(Direction.UP),
                pos.offset(Direction.UP, 2)
        };

        for(BlockPos ironPos : ironBlocks) {
            if(world.getBlockState(ironPos).getBlock() != Blocks.IRON_BLOCK) {
                return false;
            }
        }

        for(BlockPos airPos : airBlocks) {
            if(!world.getBlockState(airPos).isAir()) {
                return false;
            }
        }


        BlockPos startPos = pos.add(-1, 1, -1);

        for(int x = 0; x < 3; x++) {
            for(int z = 0; z < 3; z++) {
                for(int y = 0; y < 3; y++) {
                    Block block = world.getBlockState(startPos.add(x, y, z)).getBlock();
                    if (!(x == 1 && z == 1)) {
                        if(block != Blocks.IRON_BARS) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public static boolean checkValidEntity(Entity entity) {
        for(EntityType<?> entityType : VALID_ENTITIES) {
            if(entity.getType() == entityType) {
                return true;
            }
        }
        return false;
    }

    public static List<Entity> getTrapEntities(World world, BlockPos pos) {
        List<Entity> entities =  world.getOtherEntities(null, new Box(pos.add(new BlockPos(-1, 1, -1)), pos.add(new BlockPos(1, 3, 1))));

        for(int i = entities.size() - 1; i >= 0; i--) {
            if(!(entities.get(i) instanceof LivingEntity)) {
                entities.remove(i);
            }
        }
        return entities;
    }

    public static void destroyCage(World world, BlockPos pos) {
        BlockPos startPos = pos.add(-1, 0, -1);

        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 4; y++) {
                for(int z = 0; z < 3; z++) {
                    world.breakBlock(startPos.add(x,y,z), false);
                    spawnParticles(world, startPos.add(x,y,z));
                }
            }
        }
    }

    public static void createSpawner(World world, BlockPos pos, LivingEntity entity) {
        world.setBlockState(pos, Blocks.SPAWNER.getDefaultState());
        MobSpawnerBlockEntity blockEntity = (MobSpawnerBlockEntity)world.getBlockEntity(pos);
        blockEntity.getLogic().setEntityId(entity.getType());
    }

    public static void spawnParticles(World world, BlockPos pos) {
        if(!world.isClient) {
            for (int i = 0; i < 3; i++) {
                ((ServerWorld)world).spawnParticles(ParticleTypes.LARGE_SMOKE,
                        pos.getX() + RANDOM.nextDouble(),
                        pos.getY() + RANDOM.nextDouble(),
                        pos.getZ() + RANDOM.nextDouble(),
                        1,
                        0D, 0D, 0D,
                        0f
                );

            }
        }
    }

}
