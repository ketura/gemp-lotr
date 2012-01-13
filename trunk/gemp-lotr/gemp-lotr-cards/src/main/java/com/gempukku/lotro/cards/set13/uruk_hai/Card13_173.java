package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PutCardFromPlayOnBottomOfDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Lurker. (Skirmishes involving lurker minions must be resolved after any others.) Each time your
 * [URUK-HAI] minion is about to be killed in a skirmish, you may place that minion on the bottom of your
 * draw deck instead.
 */
public class Card13_173 extends AbstractMinion {
    public Card13_173() {
        super(4, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Reserve");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.LURKER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingKilled(effect, game, Filters.owner(playerId), Culture.URUK_HAI, CardType.MINION, Filters.inSkirmish)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            KillEffect killEffect = (KillEffect) effect;
            final List<PhysicalCard> charactersToBeKilled = killEffect.getCharactersToBeKilled();
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose your URUK-HAI minion", Filters.in(charactersToBeKilled), Filters.owner(playerId), Culture.URUK_HAI, CardType.MINION, Filters.inSkirmish) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new PutCardFromPlayOnBottomOfDeckEffect(card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
