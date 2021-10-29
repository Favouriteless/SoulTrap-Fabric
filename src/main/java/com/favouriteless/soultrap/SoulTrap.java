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

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoulTrap implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("soultrap");

	public static final Block SOUL_TRAP_BLOCK = new SoulTrapBlock(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3.5F).luminance((state) -> 3).nonOpaque());
	public static final Item SOUL_TRAP_ITEM = new BlockItem(SOUL_TRAP_BLOCK, new Item.Settings().maxCount(1).group(ItemGroup.REDSTONE));

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier("soultrap", "soul_trap"), SOUL_TRAP_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("soultrap", "soul_trap"), SOUL_TRAP_ITEM);
	}
}
