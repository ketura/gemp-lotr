package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Dimple Boffin, Bounder of the Shire
 * Ally • Hobbit • Shire
 * 2	2
 * To play, spot 2 Hobbit companions.
 * Skirmish: Exert this ally to prevent a Hobbit from being overwhelmed unless that Hobbit's strength is tripled.
 * http://lotrtcg.org/coreset/shire/dimpleboffinbots(r1).png
 */
public class Card20_477 extends AbstractAlly {
    public Card20_477() {
        super(1, null, 0, 2, 2, Race.HOBBIT, Culture.SHIRE, "Dimple Boffin", "Bounder of the Shire", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, CardType.COMPANION, Race.HOBBIT);
    }

    @Override
    protected List<ActivateCardAction> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action= new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OverwhelmedByMultiplierModifier(self, card, 3)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
