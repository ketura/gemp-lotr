package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Skirmish: Remove (2) to make a lurker strength +1. Skirmish: Remove (2) to make a lurker fierce until
 * the regroup phase. Skirmish: Remove (2) to make a lurker damage +1.
 */
public class Card11_068 extends AbstractMinion {
    public Card11_068() {
        super(4, 10, 3, 4, Race.MAN, Culture.MEN, "Armored Easterling");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 2)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();

            ActivateCardAction action1 = new ActivateCardAction(self);
            action1.setText("Make lurker strength +1");
            action1.appendCost(
                    new RemoveTwilightEffect(2));
            action1.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action1, self, playerId, 1, Keyword.LURKER));
            actions.add(action1);

            final ActivateCardAction action2 = new ActivateCardAction(self);
            action2.setText("Make lurker fierce");
            action2.appendCost(
                    new RemoveTwilightEffect(2));
            action2.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a lurker", Keyword.LURKER) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action2.insertEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
            actions.add(action2);

            final ActivateCardAction action3 = new ActivateCardAction(self);
            action3.setText("Make lurker damage +1");
            action3.appendCost(
                    new RemoveTwilightEffect(2));
            action3.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a lurker", Keyword.LURKER) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action3.insertEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.DAMAGE, 1), Phase.REGROUP));
                        }
                    });
            actions.add(action3);

            return actions;
        }
        return null;
    }
}
