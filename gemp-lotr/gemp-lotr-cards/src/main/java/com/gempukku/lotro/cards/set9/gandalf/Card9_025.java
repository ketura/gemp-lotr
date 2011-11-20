package com.gempukku.lotro.cards.set9.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Companion • Tree
 * Strength: 6
 * Vitality: 4
 * Resistance: 6
 * Game Text: Damage +1. This companion is strength +1 for each Ent you spot. Response: If this companion is about to
 * take a wound, exert an Ent to prevent that.
 */
public class Card9_025 extends AbstractCompanion {
    public Card9_025() {
        super(2, 6, 4, 6, Culture.GANDALF, Race.TREE, null, "Huorn");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, null, new CountSpottableEvaluator(Race.ENT)));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, self)
                && PlayConditions.canExert(self, game, Race.ENT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ENT));
            action.appendEffect(
                    new PreventCardEffect((WoundCharactersEffect) effect, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
