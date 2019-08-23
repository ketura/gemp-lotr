package com.gempukku.lotro.cards.set30.gundabad;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

 /**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 2
 * Site: 3
 * Game Text: When you play this minion, you may spot 4 [DWARVEN] followers to exhaust a [DWARVEN] companion.
 * Maneuver: Spot 7 companions and remove (2) to wound a companion (except Bilbo).
 */
public class Card30_038 extends AbstractMinion {
    public Card30_038() {
        super(3, 8, 2, 3, Race.ORC, Culture.GUNDABAD, "Orkish Marauder");
		addKeyword(Keyword.WARG_RIDER);
	}
	
	@Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, 4, Culture.DWARVEN, CardType.FOLLOWER)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion (except Bilbo)", CardType.COMPANION, Culture.DWARVEN) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new ExhaustCharacterEffect(self, action, card));
                        }
			});
            return Collections.singletonList(action);
        }
        return null;
	}


    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 2)
                && PlayConditions.canSpot(game, 7, CardType.COMPANION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.name("Bilbo"))));
            return Collections.singletonList(action);
        }
        return null;
    }
}