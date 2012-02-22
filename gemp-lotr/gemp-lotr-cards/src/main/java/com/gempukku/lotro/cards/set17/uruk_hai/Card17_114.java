package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make an Uruk-hai strength +2 (or spot a site you control to make that Uruk-hai strength +3 instead).
 * You may then liberate a site to make that Uruk-hai strength +2 and damage +1.
 */
public class Card17_114 extends AbstractEvent {
    public Card17_114() {
        super(Side.SHADOW, 2, Culture.URUK_HAI, "Land Had Changed", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an Uruk-hai", Race.URUK_HAI) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard urukHai) {
                        List<Effect> possibleEffects = new LinkedList<Effect>();
                        possibleEffects.add(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, urukHai, 2), Phase.SKIRMISH) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Make that Uruk-hai strength +2";
                                    }
                                });
                        if (PlayConditions.canSpot(game, Filters.siteControlled(playerId)))
                            possibleEffects.add(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, urukHai, 3), Phase.SKIRMISH) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Make that Uruk-hai strength +3";
                                        }
                                    });
                        action.appendEffect(
                                new ChoiceEffect(action, playerId, possibleEffects));

                        SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                        subAction.appendCost(
                                new LiberateASiteEffect(self));
                        subAction.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, urukHai, 2), Phase.SKIRMISH));
                        subAction.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, urukHai, Keyword.DAMAGE, 1), Phase.SKIRMISH));

                        action.appendEffect(
                                new OptionalEffect(action, playerId,
                                        new StackActionEffect(subAction) {
                                            @Override
                                            public String getText(LotroGame game) {
                                                return "Liberate a site to make that Uruk-hai strength +2 and damage +1";
                                            }
                                        }));
                    }
                });
        return action;
    }
}
