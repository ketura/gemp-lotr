package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class StackCardFromDiscardEffect extends UnrespondableEffect {
    private PhysicalCard _card;
    private PhysicalCard _stackOn;

    public StackCardFromDiscardEffect(PhysicalCard card, PhysicalCard stackOn) {
        _card = card;
        _stackOn = stackOn;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (_card.getZone() == Zone.DISCARD) {
            game.getGameState().sendMessage(_card.getOwner() + " stacks " + GameUtils.getCardLink(_card) + " from discard on " + GameUtils.getCardLink(_stackOn));
            game.getGameState().removeCardFromZone(_card);
            game.getGameState().stackCard(_card, _stackOn);
        }
    }
}
