package com.gempukku.lotro.cards.set32.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be an Orc. If bearer is Azog, he is fierce. Shadow: Exert bearer twice to play
 * an Orc from your discard pile for each [DWARVEN] companion over 3.
 */
public class Card32_029 extends AbstractAttachable {
    public Card32_029() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.MORIA, PossessionClass.HAND_WEAPON, "Azog's Mace", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ORC;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.name("Azog")), Keyword.FIERCE));
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canExert(self, game, 2, self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(action, self, self.getAttachedTo()));
            action.appendCost(
                    new ExertCharactersEffect(action, self, self.getAttachedTo()));
            int dwarvenCompanions = Filters.countActive(game, Culture.DWARVEN, CardType.COMPANION);
            int minions = Math.max(0, dwarvenCompanions - 4);
            for (int i = 0; i < minions; i++) {
                action.appendEffect(
                        new OptionalEffect(action, playerId,
                                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.ORC) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Play an Orc from your discard pile";
                                    }
                                }));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
