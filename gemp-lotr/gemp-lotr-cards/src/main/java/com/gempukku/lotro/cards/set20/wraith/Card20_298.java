package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 2
 * •The Pale Blade, Wraithblade
 * Possession • Hand Weapon
 * 3
 * Twilight. Bearer must be a Nazgul.
 * If bearer is The Witch-king, he is damage +1.
 * Each time bearer wins a skirmish, the Free Peoples player must exert the Ring-bearer or add a burden.
 * http://lotrtcg.org/coreset/ringwraith/palebladew(r2).jpg
 */
public class Card20_298 extends AbstractAttachable {
    public Card20_298() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, PossessionClass.HAND_WEAPON, "The Pale Blade", "Wraithblade", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.witchKing;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 3));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.witchKing)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            final String fpPlayer = game.getGameState().getCurrentPlayerId();
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, fpPlayer, 1, 1, Filters.ringBearer) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert the Ring-bearer";
                        }
                    });
            possibleEffects.add(
                    new AddBurdenEffect(fpPlayer, self, 1));

            action.appendEffect(
                    new ChoiceEffect(action, fpPlayer, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
