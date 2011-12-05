package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RevealCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 7
 * Type: Minion • Troll
 * Strength: 15
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Fierce. To play, spot an [ORC] minion. When you play this minion, add a burden unless the Free
 * Peoples player reveals a Free Peoples event from his or her hand.
 */
public class Card11_135 extends AbstractMinion {
    public Card11_135() {
        super(7, 15, 3, 5, Race.TROLL, Culture.ORC, "Porter Troll");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new PreventableEffect(action,
                            new AddBurdenEffect(self, 1), game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(final SubAction subAction, final String playerId) {
                                    return new ChooseCardsFromHandEffect(playerId, 1, 1, Side.FREE_PEOPLE, CardType.EVENT) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Reveal a Free Peoples event from hand";
                                        }

                                        @Override
                                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                            subAction.insertEffect(
                                                    new RevealCardsFromHandEffect(self, playerId, selectedCards));
                                        }
                                    };
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
