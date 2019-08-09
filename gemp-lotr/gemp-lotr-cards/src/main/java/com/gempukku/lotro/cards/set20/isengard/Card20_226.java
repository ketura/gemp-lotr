package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 7
 * •Lurtz, Champion of the White Hand
 * Isengard	Minion • Uruk-hai
 * 13	3	5
 * Damage +1. Archer.
 * The current site is a battleground.
 * Each time Lurtz wins a skirmish, the Free People’s Player must exert a companion for each Hobbit you can spot.
 */
public class Card20_226 extends AbstractMinion {
    public Card20_226() {
        super(7, 13, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Lurtz", "Champion of the White Hand", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new KeywordModifier(self, Filters.currentSite, Keyword.BATTLEGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countSpottable(game, Race.HOBBIT, CardType.COMPANION);
            if (count > 0)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), count, count, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
