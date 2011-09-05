package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
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
 * Game Text: Spell. Weather. To play, exert a [ISENGARD] minion. Plays on a site. Limit 1 per site. Each Hobbit at
 * this site is strength -2. Discard this condition at the end of the turn.
 */
public class Card1_135 extends AbstractLotroCardBlueprint {
    public Card1_135() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Saruman's Frost");
        addKeyword(Keyword.SPELL);
        addKeyword(Keyword.WEATHER);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert())
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.SITE), Filters.not(Filters.hasAttached(Filters.name("Saruman's Frost"))))) {

            final AttachPermanentAction action = new AttachPermanentAction(game, self, Filters.and(Filters.type(CardType.SITE), Filters.not(Filters.hasAttached(Filters.name("Saruman's Frost")))), Collections.<Filter, Integer>emptyMap());
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose ISENGARD minion", Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert()) {
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
        return new StrengthModifier(self,
                Filters.and(
                        Filters.keyword(Keyword.HOBBIT),
                        Filters.type(CardType.COMPANION),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return gameState.getCurrentSite() == self.getAttachedTo();
                            }
                        }
                ), -2);
    }


    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN) {
            CostToEffectAction action = new CostToEffectAction(self, null, "Discard at the end of the turn");
            action.addEffect(new DiscardCardFromPlayEffect(self));

            return Collections.singletonList(action);
        }

        return null;
    }
}
