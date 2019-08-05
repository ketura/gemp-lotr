package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PlayNextSiteEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Into the Wild
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition - Support Area
 * Card Number: 1U110
 * Game Text: While you can spot a ranger, the site number of each minion in play is +1.
 * Response: If a ranger wins a skirmish, exert that ranger and discard this condition to play the fellowship's
 * next site (replacing opponent's site if necessary).
 */
public class Card40_110 extends AbstractPermanent {
    public Card40_110() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Into the Wild", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        MinionSiteNumberModifier modifier = new MinionSiteNumberModifier(self,
                Filters.and(CardType.MINION, Filters.inPlay()), new SpotCondition(Keyword.RANGER), 1);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Keyword.RANGER)) {
            CharacterWonSkirmishResult result = (CharacterWonSkirmishResult) effectResult;
            final PhysicalCard winner = result.getWinner();
            if (PlayConditions.canExert(self, game, winner)
                    && PlayConditions.canSelfDiscard(self, game)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ExertCharactersEffect(action, self, winner));
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new PlayNextSiteEffect(action, playerId));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
