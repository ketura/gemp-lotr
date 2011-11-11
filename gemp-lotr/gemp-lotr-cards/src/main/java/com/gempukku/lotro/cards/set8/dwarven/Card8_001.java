package com.gempukku.lotro.cards.set8.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you have initiative, each Dwarf is damage +1. Fellowship: Spot a Dwarf who is damage +X to draw
 * X cards. Discard this condition.
 */
public class Card8_001 extends AbstractPermanent {
    public Card8_001() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Aggression");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Race.DWARF, new InitiativeCondition(Side.FREE_PEOPLE), Keyword.DAMAGE, 1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSpot(game, Race.DWARF, Keyword.DAMAGE)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF, Keyword.DAMAGE) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int count = game.getModifiersQuerying().getKeywordCount(game.getGameState(), card, Keyword.DAMAGE);
                            action.insertEffect(
                                    new DrawCardEffect(playerId, count));
                        }
                    }
            );
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
