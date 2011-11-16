package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.RuleUtils;
import com.gempukku.lotro.logic.timing.results.NormalSkirmishResult;
import com.gempukku.lotro.logic.timing.results.OverwhelmSkirmishResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResolveSkirmishEffect extends AbstractEffect {
    public enum Result {
        FELLOWSHIP_OVERWHELMED, FELLOWSHIP_LOSES, SHADOW_LOSES, SHADOW_OVERWHELMED
    }

    @Override
    public String getText(LotroGame game) {
        return "Resolve skirmish";
    }

    @Override
    public Type getType() {
        return Type.BEFORE_SKIRMISH_RESOLVED;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    public Result getUpcomingResult(LotroGame game) {
        int fpStrength = RuleUtils.getFellowshipSkirmishStrength(game);
        int shadowStrength = RuleUtils.getShadowSkirmishStrength(game);

        final PhysicalCard fellowshipCharacter = game.getGameState().getSkirmish().getFellowshipCharacter();

        int multiplier = 2;
        if (fellowshipCharacter != null)
            multiplier = game.getModifiersQuerying().getOverwhelmMultiplier(game.getGameState(), fellowshipCharacter);

        if (fpStrength * multiplier <= shadowStrength && shadowStrength != 0) {
            return Result.FELLOWSHIP_OVERWHELMED;
        } else if (shadowStrength >= fpStrength) {
            return Result.FELLOWSHIP_LOSES;
        } else if (fpStrength >= 2 * shadowStrength) {
            return Result.SHADOW_OVERWHELMED;
        } else {
            return Result.SHADOW_LOSES;
        }
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        Skirmish skirmish = game.getGameState().getSkirmish();

        Result result = getUpcomingResult(game);
        Set<EffectResult> results = new HashSet<EffectResult>();

        if (result == Result.FELLOWSHIP_LOSES) {
            game.getGameState().sendMessage("Skirmish finishes with a normal win");
            results.add(new NormalSkirmishResult(skirmish.getShadowCharacters(), fpList(skirmish.getFellowshipCharacter()), skirmish.getRemovedFromSkirmish()));
        } else if (result == Result.SHADOW_LOSES) {
            game.getGameState().sendMessage("Skirmish finishes with a normal win");
            results.add(new NormalSkirmishResult(fpList(skirmish.getFellowshipCharacter()), skirmish.getShadowCharacters(), skirmish.getRemovedFromSkirmish()));
        } else if (result == Result.FELLOWSHIP_OVERWHELMED) {
            game.getGameState().sendMessage("Skirmish finishes with an overwhelm");
            results.add(new OverwhelmSkirmishResult(skirmish.getShadowCharacters(), fpList(skirmish.getFellowshipCharacter()), skirmish.getRemovedFromSkirmish()));
        } else {
            game.getGameState().sendMessage("Skirmish finishes with an overwhelm");
            results.add(new OverwhelmSkirmishResult(fpList(skirmish.getFellowshipCharacter()), skirmish.getShadowCharacters(), skirmish.getRemovedFromSkirmish()));
        }

        return new FullEffectResult(results, true, true);
    }

    private List<PhysicalCard> fpList(PhysicalCard card) {
        if (card != null)
            return Collections.singletonList(card);
        else
            return Collections.emptyList();
    }
}
