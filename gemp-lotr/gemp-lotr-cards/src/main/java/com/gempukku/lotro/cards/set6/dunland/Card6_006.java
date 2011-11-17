package com.gempukku.lotro.cards.set6.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 8
 * Type: Minion • Man
 * Strength: 17
 * Vitality: 2
 * Site: 3
 * Game Text: Each time this minion wins a skirmish, you may play a [DUNLAND] Man from hand. That Man is fierce
 * and damage +2 until the regroup phase.
 */
public class Card6_006 extends AbstractMinion {
    public Card6_006() {
        super(8, 17, 2, 3, Race.MAN, Culture.DUNLAND, "Hill Clan");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.DUNLAND, Race.MAN, Filters.playable(game)).size() > 0) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Filters.and(Culture.DUNLAND, Race.MAN)) {
                        @Override
                        protected void afterCardPlayed(PhysicalCard cardPlayed) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(cardPlayed), Keyword.FIERCE), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(cardPlayed), Keyword.DAMAGE, 2), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
