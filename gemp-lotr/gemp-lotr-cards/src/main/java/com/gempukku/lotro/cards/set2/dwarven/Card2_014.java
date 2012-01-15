package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
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
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Tale. Plays to your support area. Each time a Dwarf companion moves from an underground site, you may
 * heal that Dwarf.
 */
public class Card2_014 extends AbstractPermanent {
    public Card2_014() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Till Durin Wakes Again");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, Keyword.UNDERGROUND)) {
            Collection<PhysicalCard> dwarfCompanions = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Race.DWARF, CardType.COMPANION);
            if (dwarfCompanions.size() > 0) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, 0, dwarfCompanions.size(), Filters.in(dwarfCompanions)));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
