package com.gempukku.lotro.cards.set6.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 13
 * Vitality: 3
 * Site: 4
 * Game Text: Easterling. Fierce. Skirmish: Spot 2 burdens and exert this minion to make an Easterling strength +1.
 * Skirmish: Spot 4 burdens and exert this minion to make an Easterling strength +2.
 * Skirmish: Spot 6 burdens and exert this minion to make an Easterling strength +3.
 */
public class Card6_078 extends AbstractMinion {
    public Card6_078() {
        super(6, 13, 3, 4, Race.MAN, Culture.RAIDER, "Easterling Army");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (game.getGameState().getBurdens() >= 2) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make an Easterling Str +1");
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Keyword.EASTERLING));
                actions.add(action);
            }
            if (game.getGameState().getBurdens() >= 4) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make an Easterling Str +2");
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Keyword.EASTERLING));
                actions.add(action);
            }
            if (game.getGameState().getBurdens() >= 6) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make an Easterling Str +3");
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, Keyword.EASTERLING));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
