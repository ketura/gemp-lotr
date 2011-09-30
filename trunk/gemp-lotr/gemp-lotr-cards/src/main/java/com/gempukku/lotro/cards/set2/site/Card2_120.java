package com.gempukku.lotro.cards.set2.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.LinkedList;
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
        super("Valley of the Silverlode", 6, 3, Direction.LEFT);
        addKeyword(Keyword.RIVER);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && game.getGameState().getCurrentSite() == self) {
            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();

            Collection<PhysicalCard> hobbitCompanions = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT), Filters.type(CardType.COMPANION));
            for (PhysicalCard hobbitCompanion : hobbitCompanions) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new HealCharacterEffect(playerId, hobbitCompanion));
                actions.add(action);
            }

            return actions;
        }
        return null;
    }
}
