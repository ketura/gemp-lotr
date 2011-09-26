package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Hobbit
 * Strength: 1
 * Vitality: 2
 * Site: 1
 * Game Text: Sam is strength +1. Fellowship: Exert Rosie Cotton to heal Sam.
 */
public class Card1_309 extends AbstractAlly {
    public Card1_309() {
        super(1, 1, 1, 2, Race.HOBBIT, Culture.SHIRE, "Rosie Cotton", true);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.name("Sam"), 1);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP, "Exert Rosie Cotton to heal Sam");
            action.appendCost(new ExertCharactersCost(playerId, self));
            PhysicalCard sam = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Sam"));
            if (sam != null) {
                action.appendEffect(new CardAffectsCardEffect(self, sam));
                action.appendEffect(new HealCharacterEffect(playerId, sam));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
