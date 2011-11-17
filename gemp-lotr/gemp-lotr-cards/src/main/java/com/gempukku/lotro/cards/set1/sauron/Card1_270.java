package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 2
 * Site: 6
 * Game Text: Tracker. The roaming penalty for each [SAURON] minion you play is -1. Skirmish: Exert this minion to wound
 * a character it is skirmishing.
 */
public class Card1_270 extends AbstractMinion {
    public Card1_270() {
        super(3, 8, 2, 6, Race.ORC, Culture.SAURON, "Orc Scouting Band");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new RoamingPenaltyModifier(self, Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.SAURON), CardType.MINION), -1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.inSkirmishAgainst(Filters.sameCard(self))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
