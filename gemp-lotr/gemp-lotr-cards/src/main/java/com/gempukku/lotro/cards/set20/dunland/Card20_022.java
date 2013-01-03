package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 4
 * Saruman, Tribal Instigator
 * Dunland	Minion • Wizard
 * 8	4	4
 * Cunning.
 * While Saruman is not exhausted, each [Dunland] Man is strength +2.
 * Response: If a [Dunland] Man is about to take a wound, exert Saruman to prevent that wound.
 */
public class Card20_022 extends AbstractMinion {
    public Card20_022() {
        super(4, 8, 4, 4, Race.MAN, Culture.DUNLAND, "Saruman", "Tribal Instigator", true);
        addKeyword(Keyword.CUNNING);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.DUNLAND, Race.MAN), new SpotCondition(self, Filters.not(Filters.exhausted)), 2));
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.DUNLAND, Race.MAN)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose DUNLAND Man to prevent wound to", Culture.DUNLAND, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
