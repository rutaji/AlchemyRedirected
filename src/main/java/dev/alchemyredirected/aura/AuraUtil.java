package dev.alchemyredirected.aura;

import dev.alchemyredirected.AlchemyRedirected;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.entity.Player;

public class AuraUtil {
    public static int getAlchemyLevel(Player player) {
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(player.getUniqueId());
        return user.getSkillLevel(Skills.ALCHEMY);
    }
    public static void GiveAlchemyEXP(Player player,double amount){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(player.getUniqueId());
        user.addSkillXp(Skills.ALCHEMY,amount);
    }
}
