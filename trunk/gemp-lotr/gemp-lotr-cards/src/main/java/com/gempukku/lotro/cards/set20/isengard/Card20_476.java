package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 0
 * Savagery to Match Their Numbers
 * Event • Skirmish
 * Make an Uruk-hai strength +2, or spot 5 companions to make an Uruk-hai strength +4 and fierce until the regroup phase.
 * http://lotrtcg.org/coreset/isengard/stmtn(r1).png
 */
public class Card20_476 extends AbstractEvent {
    public Card20_476() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Savagery to Match Their Numbers", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an Uruk-hai", Race.URUK_HAI) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard urukHai) {
                        if (PlayConditions.canSpot(game, 5, CardType.COMPANION)) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(urukHai), 4), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(urukHai), Keyword.FIERCE), Phase.REGROUP));
                        } else {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(urukHai), 2)));
                        }
                    }
                });
        return action;
    }
}
