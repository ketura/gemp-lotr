package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot a [RAIDER] Man, each Free Peoples event that spots
 * or exerts a ranger gains this cost: 'wound a companion.' Skirmish: Discard this condition to make a [RAIDER]
 * Man strength +2.
 */
public class Card4_261 extends AbstractPermanent {
    public Card4_261() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.RAIDER, Zone.SUPPORT, "Wrath of Harad");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, null, null, ModifierEffect.ACTION_MODIFIER) {
                    @Override
                    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                        final PhysicalCard actionSource = action.getActionSource();

                        if (actionSource != null
                                && actionSource.getBlueprint().getCardType() == CardType.EVENT) {
                            PlayEventAction playEventAction = (PlayEventAction) action;
                            if (Filters.canSpot(gameState, modifiersQuerying, Culture.RAIDER, Race.MAN)
                                    && playEventAction.isRequiresRanger())
                                playEventAction.appendCost(
                                        new ChooseAndWoundCharactersEffect(playEventAction, performingPlayer, 1, 1, CardType.COMPANION));
                        }

                        return true;
                    }
                });
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose RAIDER Man", Culture.RAIDER, Race.MAN) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), 2), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
