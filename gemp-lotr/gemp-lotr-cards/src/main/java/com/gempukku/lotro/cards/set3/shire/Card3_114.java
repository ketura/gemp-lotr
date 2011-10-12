package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Tale. Plays to your support area. Response: If a Shadow card is about to add any number of twilight
 * tokens, exert a Hobbit ally to prevent this.
 */
public class Card3_114 extends AbstractPermanent {
    public Card3_114() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Three Monstrous Trolls");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.ADD_TWILIGHT
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT), Filters.type(CardType.ALLY))) {
            final AddTwilightEffect addTwilightEffect = (AddTwilightEffect) effect;
            PhysicalCard source = addTwilightEffect.getSource();
            if (!addTwilightEffect.isFullyPrevented() && source != null && source.getBlueprint().getSide() == Side.SHADOW) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.HOBBIT), Filters.type(CardType.ALLY)));
                action.appendEffect(
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                addTwilightEffect.preventAll();
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
