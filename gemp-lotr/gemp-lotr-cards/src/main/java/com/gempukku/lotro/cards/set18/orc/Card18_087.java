package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 4
 * Game Text: Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 * Shadow: Exert this minion to play an [URUK-HAI] hunter at twilight cost -2.
 */
public class Card18_087 extends AbstractMinion {
    public Card18_087() {
        super(4, 9, 3, 4, Race.ORC, Culture.ORC, "Orkish Breeder");
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromHand(playerId, game, -2, Culture.URUK_HAI, Keyword.HUNTER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Culture.URUK_HAI, Keyword.HUNTER));
            return Collections.singletonList(action);
        }
        return null;
    }
}
