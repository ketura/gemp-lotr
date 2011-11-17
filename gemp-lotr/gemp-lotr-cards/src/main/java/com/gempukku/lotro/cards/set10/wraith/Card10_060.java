package com.gempukku.lotro.cards.set10.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PutCardFromPlayOnTopOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [WRAITH] Orc. When you play this possession, you may discard a possession. Its owner may
 * place it on top of his or her draw deck instead. If bearer is Gorbag, he is fierce.
 */
public class Card10_060 extends AbstractAttachable {
    public Card10_060() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, PossessionClass.HAND_WEAPON, "Gorbag's Sword", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.WRAITH, Race.ORC);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.name("Gorbag"), Filters.hasAttached(self)), Keyword.FIERCE));
        return modifiers;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a possession", CardType.POSSESSION) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard card) {
                            action.appendEffect(
                                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                                            new MultipleChoiceAwaitingDecision(1, "Do you want to put " + GameUtils.getCardLink(card) + " on top of deck instead?", new String[]{"Yes", "No"}) {
                                                @Override
                                                protected void validDecisionMade(int index, String result) {
                                                    if (index == 0)
                                                        action.appendEffect(
                                                                new PutCardFromPlayOnTopOfDeckEffect(card));
                                                    else
                                                        action.appendEffect(
                                                                new DiscardCardsFromPlayEffect(self, card));
                                                }
                                            }));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
