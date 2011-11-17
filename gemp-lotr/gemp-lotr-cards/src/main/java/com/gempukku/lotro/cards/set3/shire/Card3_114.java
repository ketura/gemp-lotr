package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.PreventEffect;
import com.gempukku.lotro.logic.timing.Effect;

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
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isAddingTwilight(effect, game, Side.SHADOW)
                && PlayConditions.canExert(self, game, Race.HOBBIT, CardType.ALLY)) {
            final AddTwilightEffect addTwilightEffect = (AddTwilightEffect) effect;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, CardType.ALLY));
            action.appendEffect(
                    new PreventEffect(addTwilightEffect));
            return Collections.singletonList(action);
        }
        return null;
    }
}
