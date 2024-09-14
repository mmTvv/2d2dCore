package org.ggupp.kitstart.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.ggupp.Main;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {
    public static void copyRegion(Location start, Location end, Player player) {
        int centerX = (start.getBlockX() + end.getBlockX()) / 2;
        int centerZ = (start.getBlockZ() + end.getBlockZ()) / 2;

        List<Block> blockList = getBlocks(start, end);
        setBlocks(blockList, start, centerX, centerZ, player);

        processContainers(blockList, start, centerX, centerZ, player);
    }

    private static void setBlocks(List<Block> blockList, Location start, int centerX, int centerZ, Player player) {
        for (Block newBlock : blockList) {
            int newX = newBlock.getX() - centerX;
            int newY = newBlock.getY() - start.getBlockY();
            int newZ = newBlock.getZ() - centerZ;

            Location pos = player.getLocation().add(new Vector(newX, newY, newZ));
            Bukkit.getRegionScheduler().run(Main.getInstance(), pos, (o) ->{
                pos.getBlock().setType(newBlock.getType());
            });
        }
    }

    private static void processContainers(List<Block> blockList, Location start, int centerX, int centerZ, Player player) {
        for (Block newBlock : blockList) {
            Bukkit.getRegionScheduler().run(Main.getInstance(), newBlock.getLocation(), (o) -> {
                if (newBlock.getType().toString().contains("SHULKER_BOX")) {
                    copyItemShulker(newBlock, start, centerX, centerZ, player);
                }
            });
        }
    }

    private static void copyItemShulker(Block newBlock, Location start, int centerX, int centerZ, Player player) {
        Bukkit.getRegionScheduler().run(Main.getInstance(), newBlock.getLocation(), (o) -> {
            BlockState state = newBlock.getState();

            if (state instanceof ShulkerBox shulkerState) {
                ItemStack[] toAdd = shulkerState.getInventory().getContents();

                World world = Bukkit.getWorld(start.getWorld().getName());

                Block newShulker = world.getBlockAt(player.getLocation().add(new Vector(
                        newBlock.getX() - centerX,
                        newBlock.getY() - start.getBlockY(),
                        newBlock.getZ() - centerZ
                )));

                Bukkit.getRegionScheduler().run(Main.getInstance(), newShulker.getLocation(), (p) -> {
                    BlockState newState = newShulker.getState();
                    if (newState instanceof ShulkerBox newShulkerBox) {
                        newShulkerBox.setCustomName("Nakars??");
                        newShulkerBox.update();
                        ((ShulkerBox) newShulker.getState()).getInventory().setContents(toAdd);
                    }
                });
            }
        });
    }


    private static List<Block> getBlocks(Location start, Location end) {
        List<Block> rectangle = new ArrayList<>();
        int minX = Math.min(start.getBlockX(), end.getBlockX());
        int minY = Math.min(start.getBlockY(), end.getBlockY());
        int minZ = Math.min(start.getBlockZ(), end.getBlockZ());
        int maxX = Math.max(start.getBlockX(), end.getBlockX());
        int maxY = Math.max(start.getBlockY(), end.getBlockY());
        int maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        World world = Bukkit.getWorld(start.getWorld().getName());
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = world.getBlockAt(x, y, z);
                    rectangle.add(b);
                }
            }
        }
        return rectangle;
    }
}