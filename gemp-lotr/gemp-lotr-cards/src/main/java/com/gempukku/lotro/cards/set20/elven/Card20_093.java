package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Lights of Caras Galadhon
 * Elven	Condition • Support Area
 * To play, spot an Elf.
 * Each time the fellowship moves to a Sanctuary, you may exert an Elf to remove a burden (or two burdens if that Elf is Galadriel).
 */
public class Card20_093 extends AbstractPermanent {
    public Card20_093() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Lights of Caras Galadhon", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.movesTo(game,  effectResult, Keyword.SANCTUARY)
            && PlayConditions.canExert(self, game, Race.ELF)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            int count = Filters.galadriel.accepts(game.getGameState(), game.getModifiersQuerying(), character)?2:1;
                            action.appendEffect(
                                    new RemoveBurdenEffect(playerId, self, count));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
