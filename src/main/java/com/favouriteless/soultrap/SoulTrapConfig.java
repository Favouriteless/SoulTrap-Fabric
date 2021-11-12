package com.favouriteless.soultrap;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.Arrays;
import java.util.List;

@Config(name = "soultrap-common")
public class SoulTrapConfig implements ConfigData {

    public boolean isBlacklist = false;
    public List<String> mobList = Arrays.asList("minecraft:zombie", "minecraft:skeleton", "minecraft:spider", "minecraft:cave_spider", "minecraft:blaze", "minecraft:magma_cube", "minecraft:silverfish");

}