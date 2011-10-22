package com.gempukku.lotro.cards.set6.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. Each time a companion is killed in a skirmish involving a Nazgul, wound an ally twice
 * or exert a companion.
 */
public class Card6_122 extends AbstractMinion {
    public Card6_122() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, "The Witch-king", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.KILL
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL, Filters.inSkirmish) > 0) {
            KillResult killResult = (KillResult) effectResult;
            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
            for (PhysicalCard killedCompanion : Filters.filter(killResult.getKilledCards(), game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                List<Effect> possibleEffects = new LinkedList<Effect>();
                possibleEffects.add(
                        new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, 2, CardType.ALLY) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Wound an ally twice";
                            }
                        });
                possibleEffects.add(
                        new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, CardType.COMPANION) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Exert a companion";
                            }
                        });
                action.appendEffect(
                        new ChoiceEffect(action, self.getOwner(), possibleEffects));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
