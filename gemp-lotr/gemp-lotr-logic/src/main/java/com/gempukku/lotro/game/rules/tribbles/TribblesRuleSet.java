package com.gempukku.lotro.game.rules.tribbles;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;
import com.gempukku.lotro.game.rules.ActivatePhaseActionsFromDiscardRule;
import com.gempukku.lotro.game.rules.ActivatePhaseActionsFromHandRule;
import com.gempukku.lotro.game.rules.ActivatePhaseActionsFromStackedRule;
import com.gempukku.lotro.game.rules.ActivatePhaseActionsRule;
import com.gempukku.lotro.game.rules.ActivateResponseAbilitiesRule;
import com.gempukku.lotro.game.rules.lotronly.ConcealedRule;
import com.gempukku.lotro.game.rules.DiscardedCardRule;
import com.gempukku.lotro.game.rules.lotronly.FollowerRule;
import com.gempukku.lotro.game.rules.OptionalTriggersFromHandRule;
import com.gempukku.lotro.game.rules.OptionalTriggersRule;
import com.gempukku.lotro.game.rules.PlayResponseEventRule;
import com.gempukku.lotro.game.rules.RequiredTriggersRule;
import com.gempukku.lotro.game.rules.RuleSet;
import com.gempukku.lotro.game.rules.StatModifiersRule;
import com.gempukku.lotro.game.rules.lotronly.SanctuaryRule;

public class TribblesRuleSet extends RuleSet {
    private final DefaultGame _game;
    private final DefaultActionsEnvironment _actionsEnvironment;
    private final ModifiersLogic _modifiersLogic;

    public TribblesRuleSet(DefaultGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic) {
        super(game, actionsEnvironment, modifiersLogic);
        _game = game;
        _actionsEnvironment = actionsEnvironment;
        _modifiersLogic = modifiersLogic;
    }

    public void applyRuleSet() {
        new DiscardedCardRule(_actionsEnvironment).applyRule();

        new StatModifiersRule(_modifiersLogic).applyRule();

        new FollowerRule(_actionsEnvironment).applyRule();

        new TribblesPlayCardRule(_actionsEnvironment).applyRule();
        new PlayResponseEventRule(_actionsEnvironment).applyRule();

        new SanctuaryRule(_actionsEnvironment, _modifiersLogic).applyRule();

        new ActivateResponseAbilitiesRule(_actionsEnvironment).applyRule();
        new ActivatePhaseActionsRule(_actionsEnvironment).applyRule();
        new ActivatePhaseActionsFromHandRule(_actionsEnvironment).applyRule();
        new ActivatePhaseActionsFromDiscardRule(_actionsEnvironment).applyRule();
        new ActivatePhaseActionsFromStackedRule(_actionsEnvironment).applyRule();

        new RequiredTriggersRule(_actionsEnvironment).applyRule();
        new OptionalTriggersRule(_actionsEnvironment).applyRule();
        new OptionalTriggersFromHandRule(_actionsEnvironment).applyRule();

        new ConcealedRule(_actionsEnvironment).applyRule();
    }
}
