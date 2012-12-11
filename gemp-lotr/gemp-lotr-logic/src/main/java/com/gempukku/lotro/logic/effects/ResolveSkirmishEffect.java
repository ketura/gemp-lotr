package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.RuleUtils;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;
import com.gempukku.lotro.logic.timing.results.NormalSkirmishResult;
import com.gempukku.lotro.logic.timing.results.OverwhelmSkirmishResult;

import java.util.Collections;
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

        if (fpStrength == 0 && shadowStrength == 0)
            return Result.FELLOWSHIP_LOSES;
        else if (fpStrength * multiplier <= shadowStrength) {
            return Result.FELLOWSHIP_OVERWHELMED;
        } else if (fpStrength <= shadowStrength) {
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

        if (result == Result.FELLOWSHIP_LOSES) {
            game.getGameState().sendMessage("Skirmish resolved with a normal win");
            game.getActionsEnvironment().emitEffectResult(new NormalSkirmishResult(skirmish.getShadowCharacters(), fpList(skirmish.getFellowshipCharacter()), skirmish.getRemovedFromSkirmish()));

            for (PhysicalCard minion : skirmish.getShadowCharacters())
                game.getActionsEnvironment().emitEffectResult(new CharacterWonSkirmishResult(CharacterWonSkirmishResult.SkirmishType.NORMAL, minion, skirmish.getFellowshipCharacter()));
            if (skirmish.getFellowshipCharacter() != null)
                game.getActionsEnvironment().emitEffectResult(new CharacterLostSkirmishResult(CharacterLostSkirmishResult.SkirmishType.NORMAL, skirmish.getFellowshipCharacter(), skirmish.getShadowCharacters()));
            for (PhysicalCard removedMinion : skirmish.getRemovedFromSkirmish())
                game.getActionsEnvironment().emitEffectResult(new CharacterLostSkirmishResult(CharacterLostSkirmishResult.SkirmishType.NORMAL, removedMinion, skirmish.getFellowshipCharacter()));
        } else if (result == Result.SHADOW_LOSES) {
            game.getGameState().sendMessage("Skirmish resolved with a normal win");
            game.getActionsEnvironment().emitEffectResult(new NormalSkirmishResult(fpList(skirmish.getFellowshipCharacter()), skirmish.getShadowCharacters(), skirmish.getRemovedFromSkirmish()));

            for (PhysicalCard minion : skirmish.getShadowCharacters())
                game.getActionsEnvironment().emitEffectResult(new CharacterLostSkirmishResult(CharacterLostSkirmishResult.SkirmishType.NORMAL, minion, skirmish.getFellowshipCharacter()));
            if (skirmish.getFellowshipCharacter() != null)
                game.getActionsEnvironment().emitEffectResult(new CharacterWonSkirmishResult(CharacterWonSkirmishResult.SkirmishType.NORMAL, skirmish.getFellowshipCharacter(), skirmish.getShadowCharacters()));
            for (PhysicalCard removedMinion : skirmish.getRemovedFromSkirmish())
                game.getActionsEnvironment().emitEffectResult(new CharacterLostSkirmishResult(CharacterLostSkirmishResult.SkirmishType.NORMAL, removedMinion, skirmish.getFellowshipCharacter()));
        } else if (result == Result.FELLOWSHIP_OVERWHELMED) {
            game.getGameState().sendMessage("Skirmish resolved with an overwhelm");
            game.getActionsEnvironment().emitEffectResult(new OverwhelmSkirmishResult(skirmish.getShadowCharacters(), fpList(skirmish.getFellowshipCharacter()), skirmish.getRemovedFromSkirmish()));

            for (PhysicalCard minion : skirmish.getShadowCharacters())
                game.getActionsEnvironment().emitEffectResult(new CharacterWonSkirmishResult(CharacterWonSkirmishResult.SkirmishType.OVERWHELM, minion, skirmish.getFellowshipCharacter()));
            if (skirmish.getFellowshipCharacter() != null)
                game.getActionsEnvironment().emitEffectResult(new CharacterLostSkirmishResult(CharacterLostSkirmishResult.SkirmishType.OVERWHELM, skirmish.getFellowshipCharacter(), skirmish.getShadowCharacters()));
            for (PhysicalCard removedMinion : skirmish.getRemovedFromSkirmish())
                game.getActionsEnvironment().emitEffectResult(new CharacterLostSkirmishResult(CharacterLostSkirmishResult.SkirmishType.OVERWHELM, removedMinion, skirmish.getFellowshipCharacter()));
        } else {
            game.getGameState().sendMessage("Skirmish resolved with an overwhelm");
            game.getActionsEnvironment().emitEffectResult(new OverwhelmSkirmishResult(fpList(skirmish.getFellowshipCharacter()), skirmish.getShadowCharacters(), skirmish.getRemovedFromSkirmish()));

            for (PhysicalCard minion : skirmish.getShadowCharacters())
                game.getActionsEnvironment().emitEffectResult(new CharacterLostSkirmishResult(CharacterLostSkirmishResult.SkirmishType.OVERWHELM, minion, skirmish.getFellowshipCharacter()));
            if (skirmish.getFellowshipCharacter() != null)
                game.getActionsEnvironment().emitEffectResult(new CharacterWonSkirmishResult(CharacterWonSkirmishResult.SkirmishType.OVERWHELM, skirmish.getFellowshipCharacter(), skirmish.getShadowCharacters()));
            for (PhysicalCard removedMinion : skirmish.getRemovedFromSkirmish())
                game.getActionsEnvironment().emitEffectResult(new CharacterLostSkirmishResult(CharacterLostSkirmishResult.SkirmishType.OVERWHELM, removedMinion, skirmish.getFellowshipCharacter()));
        }

        return new FullEffectResult(true);
    }

    private Set<PhysicalCard> fpList(PhysicalCard card) {
        if (card != null)
            return Collections.singleton(card);
        else
            return Collections.emptySet();
    }
}
