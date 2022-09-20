package org.godfather.authenticator.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AuthPlayer {

    private final Player player;
    private final Map<Integer, ItemStack> inventoryContent = new HashMap<>();
    private final ItemStack[] armorContent;
    private final int expLevel;
    private final float expAmount;

    public AuthPlayer(Player player) {
        this.player = player;
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            inventoryContent.put(i, player.getInventory().getContents()[i]);
        }
        armorContent = player.getInventory().getArmorContents();
        expLevel = player.getLevel();
        expAmount = player.getExp();
    }

    public void hideInventory() {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.setLevel(0);
        player.setExp(0);
    }

    public void showInventory() {
        for (int i : inventoryContent.keySet()) {
            player.getInventory().setItem(i, inventoryContent.get(i));
        }
        player.getInventory().setArmorContents(armorContent);
        player.setLevel(expLevel);
        player.setExp(expAmount);
    }
}
