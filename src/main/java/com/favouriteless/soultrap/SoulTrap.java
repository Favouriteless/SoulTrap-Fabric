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

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoulTrap implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("soultrap");

	public static final Block SOUL_TRAP_BLOCK = new SoulTrapBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).lightLevel((state) -> 3).noOcclusion().requiresCorrectToolForDrops());
	public static final Item SOUL_TRAP_ITEM = new BlockItem(SOUL_TRAP_BLOCK, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_REDSTONE));

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new ResourceLocation("soultrap", "soul_trap"), SOUL_TRAP_BLOCK);
		Registry.register(Registry.ITEM, new ResourceLocation("soultrap", "soul_trap"), SOUL_TRAP_ITEM);

		AutoConfig.register(SoulTrapConfig.class, Toml4jConfigSerializer::new);
	}
}
