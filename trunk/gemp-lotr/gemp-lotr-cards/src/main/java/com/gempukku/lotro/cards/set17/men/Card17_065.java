package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 15
 * Vitality: 3
 * Site: 4
 * Game Text: You may play this minion any time you could play a maneuver event. When you play this minion in
 * the maneuver phase, you may discard X [MEN] minions from play to add (X).
 */
public class Card17_065 extends AbstractMinion {
    public Card17_065() {
        super(6, 15, 3, 4, Race.MAN, Culture.MEN, "Vengeful Primitive");
    }

    @Override
    protected Phase getExtraPlayableInPhase(LotroGame game) {
        return Phase.MANEUVER;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.isPhase(game, Phase.MANEUVER)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, Integer.MAX_VALUE, Culture.MEN, CardType.MINION) {
                        @Override
                        protected void cardsToBeDiscardedCallback(Collection<PhysicalCard> cards) {
                            action.appendEffect(
                                    new AddTwilightEffect(self, cards.size()));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
