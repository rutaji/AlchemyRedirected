package dev.alchemyredirected.exp;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.entity.Player;

public  interface ExpAdapter {
    public  int getAlchemyLevel(Player player);

    public  void GiveAlchemyEXP(Player player,double amount);
}
