package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.rules.AmbushRule;
import com.gempukku.lotro.logic.timing.rules.CunningRule;
import com.gempukku.lotro.logic.timing.rules.DiscardedCardRule;
import com.gempukku.lotro.logic.timing.rules.EnduringRule;
import com.gempukku.lotro.logic.timing.rules.FrodoAndSamRule;
import com.gempukku.lotro.logic.timing.rules.HunterRule;
import com.gempukku.lotro.logic.timing.rules.KilledCardRule;
import com.gempukku.lotro.logic.timing.rules.MusterRule;
import com.gempukku.lotro.logic.timing.rules.ResolveSkirmishRule;
import com.gempukku.lotro.logic.timing.rules.RingBearerRule;
import com.gempukku.lotro.logic.timing.rules.RoamingRule;
import com.gempukku.lotro.logic.timing.rules.SanctuaryRule;
import com.gempukku.lotro.logic.timing.rules.ThreatRule;

public class RuleSet {
    private LotroGame _game;
    private DefaultActionsEnvironment _actionsEnvironment;
    private ModifiersLogic _modifiersLogic;

    public RuleSet(LotroGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic) {
        _game = game;
        _actionsEnvironment = actionsEnvironment;
        _modifiersLogic = modifiersLogic;
    }

    public void applyRuleSet() {
        new RoamingRule(_modifiersLogic).applyRule();
        new EnduringRule(_modifiersLogic).applyRule();
        new HunterRule(_modifiersLogic).applyRule();

        new MusterRule(_actionsEnvironment).applyRule();

        new AmbushRule(_actionsEnvironment).applyRule();

        new ResolveSkirmishRule(_game, _actionsEnvironment).applyRule();

        new SanctuaryRule(_actionsEnvironment, _modifiersLogic).applyRule();

        new DiscardedCardRule(_actionsEnvironment).applyRule();

        new KilledCardRule(_actionsEnvironment).applyRule();

        new FrodoAndSamRule(_modifiersLogic).applyRule();

        new RingBearerRule(_modifiersLogic).applyRule();

        new ThreatRule(_actionsEnvironment).applyRule();

        new CunningRule(_modifiersLogic).applyRule();
    }
}
