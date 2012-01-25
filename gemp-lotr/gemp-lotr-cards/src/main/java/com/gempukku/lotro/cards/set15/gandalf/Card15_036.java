package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RevealCardEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 5
 * Type: Companion • Ent
 * Strength: 7
 * Vitality: 4
 * Resistance: 6
 * Game Text: While you can spot 4 Ents, this companion is strength +3. Maneuver: Exert this companion to reveal
 * the bottom card of your draw deck. If it is a [GANDALF] character, you may play it.
 */
public class Card15_036 extends AbstractCompanion {
    public Card15_036() {
        super(5, 7, 4, 6, Culture.GANDALF, Race.ENT, null, "Shepherd of the Trees");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new SpotCondition(4, Race.ENT), 3);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        public void doPlayEffect(LotroGame game) {
                            List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                            if (deck.size() > 0) {
                                PhysicalCard bottomCard = deck.get(deck.size() - 1);
                                action.appendEffect(
                                        new RevealCardEffect(self, bottomCard));
                                if (Filters.and(Culture.GANDALF, Filters.character)
                                        .accepts(game.getGameState(), game.getModifiersQuerying(), bottomCard)
                                        && PlayConditions.canPlayFromDeck(playerId, game, bottomCard)) {
                                    action.appendEffect(
                                            new ChooseAndPlayCardFromDeckEffect(playerId, bottomCard));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
