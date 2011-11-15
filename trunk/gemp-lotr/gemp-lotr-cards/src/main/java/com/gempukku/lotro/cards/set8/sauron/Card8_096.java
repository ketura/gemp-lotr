package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: Besieger. Enduring. To play, spot a [SAURON] Orc. Maneuver: If you have initiative, exert this minion
 * twice to take control of a site. Maneuver: Remove a threat and exert this minion twice to take control of a site.
 */
public class Card8_096 extends AbstractMinion {
    public Card8_096() {
        super(5, 12, 3, 5, Race.ORC, Culture.SAURON, "Gorgoroth Berserker");
        addKeyword(Keyword.BESIEGER);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.SAURON, Race.ORC);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (PlayConditions.hasInitiative(game, Side.SHADOW)
                    && PlayConditions.canSelfExert(self, 2, game)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("If you have initiative, exert this minion twice to take control of a site");
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new TakeControlOfASiteEffect(self, playerId));
                actions.add(action);
            }
            if (PlayConditions.canRemoveThreat(game, self, 1)
                    && PlayConditions.canSelfExert(self, 2, game)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Remove a threat and exert this minion twice to take control of a site");
                action.appendCost(
                        new RemoveThreatsEffect(self, 1));
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new TakeControlOfASiteEffect(self, playerId));
                actions.add(action);
            }
            return actions;
        }

        return null;
    }
}
