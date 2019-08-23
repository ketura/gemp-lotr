package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Tale. Skirmish: Exert an Elf at a river or forest to cancel a skirmish involving that Elf.
 */
public class Card3_028 extends AbstractEvent {
    public Card3_028() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Voice of Nimrodel", Phase.SKIRMISH);
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.ELF)
                && (game.getModifiersQuerying().hasKeyword(game, game.getGameState().getCurrentSite(), Keyword.RIVER)
                || game.getModifiersQuerying().hasKeyword(game, game.getGameState().getCurrentSite(), Keyword.FOREST));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF) {

                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard elf) {
                        Skirmish skirmish = game.getGameState().getSkirmish();
                        if (skirmish != null && skirmish.getFellowshipCharacter() == elf) {
                            action.appendEffect(
                                    new CancelSkirmishEffect());
                        }
                    }
                });
        return action;
    }
}
