package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: To play, exert a [ISENGARD] minion. Plays on a site. While the fellowship is at this site, skip the
 * archery phase. Discard this condition at the end of the turn.
 */
public class Card1_140 extends AbstractLotroCardBlueprint {
    public Card1_140() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Spies of Saruman");
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert())) {
            final AttachPermanentAction action = new AttachPermanentAction(game, self, Filters.type(CardType.SITE), Collections.<Filter, Integer>emptyMap());
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an ISENGARD minion", Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard isengardMinion) {
                            action.addCost(new ExertCharacterEffect(isengardMinion));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new AbstractModifier(self, "Skip archery phase", null) {
            @Override
            public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, boolean result) {
                if (phase == Phase.ARCHERY
                        && gameState.getCurrentSite() == self.getAttachedTo())
                    return true;
                return result;
            }
        };
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN) {
            CostToEffectAction action = new CostToEffectAction(self, null, "Discard at the end of the turn");
            action.addEffect(new DiscardCardFromPlayEffect(self));

            return Collections.singletonList(action);
        }

        return null;
    }
}
