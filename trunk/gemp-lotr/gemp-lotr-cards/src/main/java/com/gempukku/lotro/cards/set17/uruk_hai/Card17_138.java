package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Each time you take control of a site, you may spot 3 [URUK-HAI] minions to exert an unbound companion.
 * Skirmish: Liberate a site to make an [URUK-HAI] minion damage +1.
 */
public class Card17_138 extends AbstractPermanent {
    public Card17_138() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "You Do Not Know Pain");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.takenControlOfASite(effectResult, playerId)
                && PlayConditions.canSpot(game, 3, Culture.URUK_HAI, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canLiberateASite(game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new LiberateASiteEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an URUK-HAI minion", Culture.URUK_HAI, CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.DAMAGE, 1), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
