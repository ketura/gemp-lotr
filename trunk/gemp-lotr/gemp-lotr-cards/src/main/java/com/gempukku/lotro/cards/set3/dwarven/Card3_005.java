package com.gempukku.lotro.cards.set3.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Tale. To play, exert a Dwarf. Plays to your support area. The twilight cost of each [MORIA] weapon is +2.
 * Fellowship: Discard this condition to discard up to 2 [MORIA] conditions.
 */
public class Card3_005 extends AbstractPermanent {
    public Card3_005() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Song of Durin");
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction permanentAction = super.getPlayCardAction(playerId, game, self, twilightModifier);
        permanentAction.appendCost(
                new ChooseAndExertCharactersEffect(permanentAction, playerId, 1, 1, Filters.race(Race.DWARF)));
        return permanentAction;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, Filters.and(Filters.culture(Culture.MORIA), Filters.weapon()), 2));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, 2, Filters.culture(Culture.MORIA), Filters.type(CardType.CONDITION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
