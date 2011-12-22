package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 3
 * Type: Site
 * Game Text: Dwelling. River. When your fellowship moves to this site, you may heal each Hobbit companion.
 */
public class Card11_261 extends AbstractNewSite {
    public Card11_261() {
        super("Valley of the Silverlode", 3, Direction.RIGHT);
        addKeyword(Keyword.DWELLING);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && playerId.equals(game.getGameState().getCurrentPlayerId())) {
            Collection<PhysicalCard> hobbitCompanions = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Race.HOBBIT, CardType.COMPANION);
            if (hobbitCompanions.size() > 0) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, 0, hobbitCompanions.size(), Filters.in(hobbitCompanions)));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
