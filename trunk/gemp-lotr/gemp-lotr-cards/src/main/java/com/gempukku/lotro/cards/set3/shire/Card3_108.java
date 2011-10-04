package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be a Hobbit. Skirmish: Exert bearer to wound an Orc he or she is skirmishing.
 */
public class Card3_108 extends AbstractAttachableFPPossession {
    public Card3_108() {
        super(0, 1, 0, Culture.SHIRE, Keyword.HAND_WEAPON, "Frying Pan");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.HOBBIT);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.hasAttached(self)));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.race(Race.ORC), Filters.inSkirmishAgainst(Filters.hasAttached(self))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
