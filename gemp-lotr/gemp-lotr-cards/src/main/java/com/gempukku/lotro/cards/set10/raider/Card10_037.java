package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: Corsair. To play, spot a [RAIDER] Man. Shadow: Exert this minion to play a [RAIDER] support area
 * possession from your draw deck.
 */
public class Card10_037 extends AbstractMinion {
    public Card10_037() {
        super(2, 6, 2, 4, Race.MAN, Culture.RAIDER, "Corsair Boatswain");
        addKeyword(Keyword.CORSAIR);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.RAIDER, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.RAIDER, CardType.POSSESSION, Keyword.SUPPORT_AREA));
            return Collections.singletonList(action);
        }
        return null;
    }
}
