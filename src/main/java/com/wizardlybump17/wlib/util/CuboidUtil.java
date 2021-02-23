package com.wizardlybump17.wlib.util;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;
import com.wizardlybump17.wlib.item.MaterialId;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@AllArgsConstructor
public class CuboidUtil {

    private final Location location;

    public Set<Block> getBlocks(Location to) {
        return getBlocks(to, block -> true);
    }

    public Set<Block> getBlocks(Location to, Predicate<Block> predicate) {
        Set<Block> blocks = new HashSet<>(getArea(to));

        int x1 = Math.min(location.getBlockX(), to.getBlockX());
        int y1 = Math.min(location.getBlockY(), to.getBlockY());
        int z1 = Math.min(location.getBlockZ(), to.getBlockZ());

        int x2 = Math.max(location.getBlockX(), to.getBlockX());
        int y2 = Math.max(location.getBlockY(), to.getBlockY());
        int z2 = Math.max(location.getBlockZ(), to.getBlockZ());

        for(int xPoint = x1; xPoint <= x2; xPoint++) {
            for (int yPoint = y1; yPoint <= y2; yPoint++) {
                for (int zPoint = z1; zPoint <= z2; zPoint++) {
                    Block blockAt = location.getWorld().getBlockAt(xPoint, yPoint, zPoint);
                    if (!predicate.test(blockAt)) continue;
                    blocks.add(blockAt);
                }
            }
        }
        return blocks;
    }

    public Set<Block> generate(Location to, MaterialId block) {
        return generate(to, block, block1 -> true);
    }

    public Set<Block> generate(Location to, MaterialId block, Predicate<Block> predicate) {
        Set<Block> result = new HashSet<>(getArea(to));
        long runtime = System.currentTimeMillis();

        FaweQueue faweQueue = FaweAPI.createQueue(FaweAPI.getWorld(location.getWorld().getName()), false);
        for (Block iBlock : getBlocks(to, predicate)) {
            faweQueue.setBlock(iBlock.getX(), iBlock.getY(), iBlock.getZ(), block.getId(), block.getData());
            result.add(iBlock);
        }
        faweQueue.flush();
        runtime = System.currentTimeMillis() - runtime;
        System.out.println("changed " + result.size() + " blocks in " + runtime + "ms");
        return result;
    }

    @SuppressWarnings("deprecated")
    public Set<Block> generate(Location to, Map<MaterialId, Double> blocksMap) {
        return generate(to, blocksMap, block -> true);
    }

    public Set<Block> generate(Location to, Map<MaterialId, Double> blocksMap, Predicate<Block> predicate) {
        Set<Block> result = new HashSet<>(getArea(to));

        long runtime = System.currentTimeMillis();
        FaweQueue faweQueue = FaweAPI.createQueue(FaweAPI.getWorld(location.getWorld().getName()), false);
        for (Block block : getBlocks(to, predicate)) {
            MaterialId material = null;
            while (material == null) {
                for (Map.Entry<MaterialId, Double> entry : blocksMap.entrySet())
                    if (RandomUtil.checkPercentage(entry.getValue())) {
                        material = entry.getKey();
                        break;
                    }
            }
            faweQueue.setBlock(block.getX(), block.getY(), block.getZ(), material.getId(), material.getData());
            result.add(block);
        }
        faweQueue.flush();
        runtime = System.currentTimeMillis() - runtime;
        System.out.println("changed " + result.size() + " blocks in " + runtime + "ms");
        return result;
    }

    public int getArea(Location to) {
        int xLength = Math.max(location.getBlockX(), to.getBlockX())
                - Math.min(location.getBlockX(), to.getBlockX()) + 1;
        int yLength = Math.max(location.getBlockY(), to.getBlockY())
                - Math.min(location.getBlockY(), to.getBlockY()) + 1;
        int zLength = Math.max(location.getBlockZ(), to.getBlockZ())
                - Math.min(location.getBlockZ(), to.getBlockZ()) + 1;
        return xLength * yLength * zLength;
    }
}
