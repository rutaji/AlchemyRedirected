package dev.alchemyredirected.exp;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.entity.Player;

public class AuraUtil implements  ExpAdapter{
    public int getAlchemyLevel(Player player) {
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(player.getUniqueId());
        return user.getSkillLevel(Skills.ALCHEMY);
    }
    public void GiveAlchemyEXP(Player player,double amount){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(player.getUniqueId());
        user.addSkillXp(Skills.ALCHEMY,amount);
    }
}
