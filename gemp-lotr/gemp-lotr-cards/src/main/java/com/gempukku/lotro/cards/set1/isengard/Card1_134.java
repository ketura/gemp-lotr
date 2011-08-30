package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Spell. Weather. To play, exert a [ISENGARD] minion. Plays on a site. Limit 1 per site. Each Hobbit who
 * moves from this site must exert. Discard this condition at the end of the turn.
 */
public class Card1_134 extends AbstractLotroCardBlueprint {
    public Card1_134() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Saruman's Chill", "1_134");
        addKeyword(Keyword.SPELL);
        addKeyword(Keyword.WEATHER);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game.getGameState(), game.getModifiersQuerying(), Phase.SHADOW, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert())
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.SITE), Filters.not(Filters.attached(Filters.name("Saruman's Chill"))))) {

            final AttachPermanentAction action = new AttachPermanentAction(self, Filters.and(Filters.type(CardType.SITE), Filters.not(Filters.attached(Filters.name("Saruman's Chill")))), Collections.<Filter, Integer>emptyMap());
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose ISENGARD minion", Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert()) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard isengardMinion) {
                            action.addCost(new ExertCharacterEffect(isengardMinion));
                        }
                    }
            );

            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_FROM
                && game.getGameState().getCurrentSite() == self.getAttachedTo()) {

            CostToEffectAction action = new CostToEffectAction(self, "Exert each Hobbit who moves from this site");
            List<PhysicalCard> hobbits = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION), Filters.keyword(Keyword.HOBBIT));
            for (PhysicalCard hobbit : hobbits)
                action.addEffect(new ExertCharacterEffect(hobbit));

            return Collections.singletonList(action);
        }

        if (effectResult.getType() == EffectResult.Type.END_OF_TURN) {
            CostToEffectAction action = new CostToEffectAction(self, "Discard at the end of the turn");
            action.addEffect(new DiscardCardFromPlayEffect(self));

            return Collections.singletonList(action);
        }

        return null;
    }
}
