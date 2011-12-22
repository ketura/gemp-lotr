package com.gempukku.lotro.cards.set2.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Block;
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
 * Set: Mines of Moria
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: River. Sanctuary. When the fellowship moves to Valley of the Silverlode, each Hobbit companion may heal.
 */
public class Card2_120 extends AbstractSite {
    public Card2_120() {
        super("Valley of the Silverlode", Block.FELLOWSHIP, 6, 3, Direction.LEFT);
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
