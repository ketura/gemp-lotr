package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.MakeRingBearerEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Maneuver: Exert Sam twice to make him defender +1 until the regroup phase. Response: If Frodo dies, make
 * Sam the Ring-bearer (resistance 5).
 */
public class Card2_114 extends AbstractCompanion {
    public Card2_114() {
        super(2, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.GANDALF, "Sam", true);
    }

    @Override
    public int getResistance() {
        return 5;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), 2, Filters.sameCard(self))) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);
            action.appendCost(
                    new ExertCharactersCost(self, self));
            action.appendCost(
                    new ExertCharactersCost(self, self));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, Filters.sameCard(self), Keyword.DEFENDER), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.KILL) {
            if (Filters.filter(((KillEffect) effect).getCharactersToBeKilled(), game.getGameState(), game.getModifiersQuerying(), Filters.name("Frodo")).size() > 0) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(new MakeRingBearerEffect(self));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
