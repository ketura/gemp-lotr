package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ForEachBurdenYouSpotEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Spot X burdens to make a character skirmishing a [SAURON] Orc strength -X.
 */
public class Card1_246 extends AbstractOldEvent {
    public Card1_246() {
        super(Side.SHADOW, Culture.SAURON, "Enduring Evil", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish != null
                && Filters.filter(skirmish.getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC)).size() > 0
                && skirmish.getFellowshipCharacter() != null) {
            action.appendEffect(
                    new ForEachBurdenYouSpotEffect(playerId) {
                        @Override
                        protected void burdensSpotted(int burdensSpotted) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(skirmish.getFellowshipCharacter()), -burdensSpotted), Phase.SKIRMISH));
                        }
                    });
        }
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
