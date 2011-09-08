package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Each time you play a [MORIA] weapon, add (1). Response: If a [MORIA] Orc is
 * about to take a wound, discard this condition to prevent that wound.
 */
public class Card1_173 extends AbstractLotroCardBlueprint {
    public Card1_173() {
        super(Side.SHADOW, CardType.CONDITION, Culture.MORIA, "Goblin Armory");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier);
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.MORIA), Filters.or(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON))))) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Add (1)");
            action.addEffect(new AddTwilightEffect(1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, final Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WOUND) {
            WoundResult woundResult = (WoundResult) effectResult;
            PhysicalCard woundedCard = woundResult.getWoundedCard();
            if (woundedCard.getBlueprint().getCulture() == Culture.MORIA && game.getModifiersQuerying().hasKeyword(game.getGameState(), woundedCard, Keyword.ORC)) {
                DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Prevent wound");
                action.addCost(
                        new DiscardCardFromPlayEffect(self, self));
                action.addEffect(
                        new UnrespondableEffect() {
                            @Override
                            public void playEffect(LotroGame game) {
                                WoundCharacterEffect woundEffect = (WoundCharacterEffect) effect;
                                woundEffect.prevent();
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }
        return null;
    }
}
