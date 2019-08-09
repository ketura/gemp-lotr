package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ForEachBurdenYouSpotEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Title: Enduring Evil
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1R215
 * Game Text: Spot X burdens to make a character skirmishing a [SAURON] Orc strength -X.
 */
public class Card40_215 extends AbstractEvent {
    public Card40_215() {
        super(Side.SHADOW, 0, Culture.SAURON, "Enduring Evil", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish != null
                && Filters.filter(skirmish.getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Culture.SAURON, Race.ORC).size() > 0
                && skirmish.getFellowshipCharacter() != null) {
            action.appendEffect(
                    new ForEachBurdenYouSpotEffect(playerId) {
                        @Override
                        protected void burdensSpotted(int burdensSpotted) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(skirmish.getFellowshipCharacter()), -burdensSpotted)));
                        }
                    });
        }
        return action;
    }
}
