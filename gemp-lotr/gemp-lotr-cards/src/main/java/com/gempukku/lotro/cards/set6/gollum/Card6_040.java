package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Minion
 * Strength: 5
 * Vitality: 4
 * Site: 3
 * Game Text: Skirmish: Discard a card from hand to make Gollum strength +1 (or +2 if skirmishing
 * a Ring-Bound companion).
 */
public class Card6_040 extends AbstractMinion {
    public Card6_040() {
        super(2, 5, 4, 3, null, Culture.GOLLUM, "Gollum", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && game.getGameState().getHand(playerId).size() >= 1) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false));
            int bonus = (Filters.inSkirmishAgainst(CardType.COMPANION, Keyword.RING_BOUND).accepts(game.getGameState(), game.getModifiersQuerying(), self)) ? 2 : 1;
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, null, bonus), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
