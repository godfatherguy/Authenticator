package org.godfather.authenticator.auth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.godfather.authenticator.auth.AuthManager;

public class MobEvents implements Listener {

    private final AuthManager authManager;

    public MobEvents(AuthManager authManager) {
        this.authManager = authManager;
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player))
            return;
        Player victim = (Player) event.getTarget();

        if (!authManager.isAuth(victim))
            return;

        if (!authManager.getInstance().getConfigManager().getConfigFile().getAuth().invisible() && !authManager.getInstance().getConfigManager().getConfigFile().getAuth().invincible())
            return;

        event.setCancelled(true);
    }
}
