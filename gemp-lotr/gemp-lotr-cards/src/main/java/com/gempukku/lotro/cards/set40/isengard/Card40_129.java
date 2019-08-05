package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Title: Now Perfected
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1R129
 * Game Text: Make an Uruk-hai strength +2 or spot 6 companions to make an Uruk-hai strength +4 and fierce until the regroup phase.
 */
public class Card40_129 extends AbstractEvent {
    public Card40_129() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Now Perfected", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        if (PlayConditions.canSpot(game, 6, CardType.COMPANION))
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an Uruk-hai", Race.URUK_HAI) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, 4), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
        else
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.URUK_HAI));
        return action;
    }
}
