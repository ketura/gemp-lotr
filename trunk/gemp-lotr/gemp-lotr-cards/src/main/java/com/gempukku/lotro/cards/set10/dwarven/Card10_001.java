package com.gempukku.lotro.cards.set10.dwarven;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Event • Response
 * Game Text: If an opponent plays a minion, exert a Dwarf who is damage +X to exert that minion X times.
 */
public class Card10_001 extends AbstractResponseEvent {
    public Card10_001() {
        super(Side.FREE_PEOPLE, 3, Culture.DWARVEN, "Great Day, Great Hour");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Race.DWARF, Keyword.DAMAGE);
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.MINION)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            final PhysicalCard playedMinion = playResult.getPlayedCard();
            final PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF, Keyword.DAMAGE) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            int damageCount = game.getModifiersQuerying().getKeywordCount(game.getGameState(), character, Keyword.DAMAGE);
                            for (int i = 0; i < damageCount; i++)
                                action.appendEffect(
                                        new ExertCharactersEffect(self, playedMinion));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
