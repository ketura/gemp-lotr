package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class StackCardFromHandEffect extends UnrespondableEffect {
    private PhysicalCard _card;
    private PhysicalCard _stackOn;

    public StackCardFromHandEffect(PhysicalCard card, PhysicalCard stackOn) {
        _card = card;
        _stackOn = stackOn;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (_card.getZone() == Zone.HAND) {
            game.getGameState().sendMessage(_card.getOwner() + " stacks " + _card.getBlueprint().getName() + " from hand on " + _stackOn.getBlueprint().getName());
            game.getGameState().removeCardFromZone(_card);
            game.getGameState().stackCard(_card, _stackOn);
        }
    }
}
