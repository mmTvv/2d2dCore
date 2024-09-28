package org.ggupp.eglow.data;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ggupp.eglow.EGlowSection;

@Getter
@Setter
@RequiredArgsConstructor
public class EGlowPlayer {
    private final Player player;
    private EGlowEffect glowEffect;
    private final EGlowSection main;

    public void setEffect(EGlowEffect effect){
        if(getGlowEffect() != null){
            getGlowEffect().removePlayer(this);
        }
        this.glowEffect = effect;
        if(effect != null) {
            effect.addPlayer(this);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (this.getPlayer().getPlayer().isListed(player)) {
                    try {
                        main.glowingEntities().setGlowing(this.getPlayer(), player, effect.getEffectColors().get(0));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void disableGlow() {
        if (getGlowEffect() != null)
            getGlowEffect().removePlayer(this);

        setGlowEffect(null);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.getPlayer().getPlayer().isListed(player)) {
                try {
                    main.glowingEntities().unsetGlowing(this.getPlayer(), player);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void activateGlow(){
        if (getGlowEffect() != null) {
            setEffect(getGlowEffect());
        }
    }
}
