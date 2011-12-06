package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ShuffleCardsFromDiscardIntoDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: Each time a companion loses a fierce skirmish involving a [WRAITH] minion, you may shuffle a [WRAITH] card
 * from your discard pile into your draw deck.
 */
public class Card11_210 extends AbstractPermanent {
    public Card11_210() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Hatred Stirred", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.losesSkirmishInvolving(game, effectResult, CardType.COMPANION, Culture.WRAITH)
                && game.getGameState().isFierceSkirmishes()) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseCardsFromDiscardEffect(playerId, 1, 1, Culture.WRAITH) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            action.appendEffect(
                                    new ShuffleCardsFromDiscardIntoDeckEffect(self, playerId, cards));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
