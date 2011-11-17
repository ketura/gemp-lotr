package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Strength: -1
 * Game Text: Fortification. Skirmish: Exert 2 [GONDOR] Men or spot 3 knights to transfer this condition from your
 * support area to a minion skirmishing a [GONDOR] Man. Exhaust that minion.
 */
public class Card8_035 extends AbstractPermanent {
    public Card8_035() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Fourth Level");
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), -1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && self.getZone() == Zone.SUPPORT
                && (
                PlayConditions.canExertMultiple(self, game, 1, 2, Culture.GONDOR, Race.MAN)
                        || PlayConditions.canSpot(game, 3, Keyword.KNIGHT))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Culture.GONDOR, Race.MAN) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert 2 GONDOR Men";
                        }
                    });
            possibleCosts.add(
                    new SpotEffect(3, Keyword.KNIGHT) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Spot 3 Knights";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION, Filters.inSkirmishAgainst(Culture.GONDOR, Race.MAN)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new TransferPermanentEffect(self, card));
                            action.appendEffect(
                                    new ExhaustCharacterEffect(self, action, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

}
