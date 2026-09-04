package dev.alchemyredirected.exp;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.EXPSource;
import net.Indyuce.mmocore.experience.Profession;
import org.bukkit.entity.Player;

public class MnoCoreUtil implements ExpAdapter {

    @Override
    public int getAlchemyLevel(Player player) {
        return PlayerData.get(player).getCollectionSkills().getLevel("alchemy");
    }

    @Override
    public void GiveAlchemyEXP(Player player, double amount) {
        PlayerData playerData = PlayerData.get(player);
        Profession alchemy = MMOCore.plugin.professionManager.get("alchemy");
        playerData.getCollectionSkills().giveExperience(alchemy, amount, EXPSource.OTHER);
    }
}
