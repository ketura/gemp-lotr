package com.gempukku.lotro.cards.packs;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

import java.util.*;

public class LeagueBoosterBox {
    private Random _random = new Random();
    private LotroCardBlueprintLibrary _library;

    private List<String> _leagueRares;
    private List<String> _leagueSiteUncommons;
    private List<String> _leagueShadowUncommons;
    private List<String> _leagueFPUncommons;
    private List<String> _leagueShadowCommons;
    private List<String> _leagueFPCommons;

    private List<String> _foilRares;
    private List<String> _foilUncommons;
    private List<String> _foilCommons;

    public LeagueBoosterBox(LotroCardBlueprintLibrary library) {
        _library = library;
    }

    public synchronized List<String> getBooster(String setNo) {
        if (!setNo.equals("1"))
            throw new IllegalArgumentException("Invalid setNo: "+setNo);
        initializeFotR();

        boolean hasFoil = _random.nextInt(6) == 0;

        List<String> result = new LinkedList<String>();
        // Maybe add foil
        if (hasFoil) {
            int type = _random.nextInt(11);
            if (type == 0)
                result.add(_foilRares.get(_random.nextInt(_foilRares.size()))+"*");
            else if (type < 4)
                result.add(_foilUncommons.get(_random.nextInt(_foilUncommons.size()))+"*");
            else
                result.add(_foilCommons.get(_random.nextInt(_foilCommons.size()))+"*");
        }

        // Add rare
        result.add(_leagueRares.get(_random.nextInt(_leagueRares.size())));

        // Add uncommon site
        result.add(_leagueSiteUncommons.get(_random.nextInt(_leagueSiteUncommons.size())));

        result.add(_leagueShadowUncommons.get(_random.nextInt(_leagueShadowUncommons.size())));
        result.add(_leagueFPUncommons.get(_random.nextInt(_leagueFPUncommons.size())));

        Collections.shuffle(_leagueShadowCommons);
        Collections.shuffle(_leagueFPCommons);

        result.add(_leagueShadowCommons.get(0));
        result.add(_leagueFPCommons.get(0));

        result.add(_leagueShadowCommons.get(1));
        result.add(_leagueFPCommons.get(1));

        result.add(_leagueShadowCommons.get(2));
        result.add(_leagueFPCommons.get(2));

        if (!hasFoil) {
            if (_random.nextBoolean())
                result.add(_leagueShadowCommons.get(3));
            else
                result.add(_leagueFPCommons.get(3));
        }
        
        return result;
    }

    private void initializeFotR() {
        if (_foilRares == null) {
            SetRarity setRarity = new RarityReader().getSetRarity("1");

            List<String> promos = replaceRarity("P", setRarity.getCardsOfRarity("P"));
            List<String> rares = replaceRarity("R", setRarity.getCardsOfRarity("R"));
            List<String> uncommons = replaceRarity("U", setRarity.getCardsOfRarity("U"));
            List<String> commons = replaceRarity("C", setRarity.getCardsOfRarity("C"));

            _foilRares = new ArrayList<String>();
            _foilRares.addAll(rares);
            _foilRares.addAll(promos);

            _foilUncommons = new ArrayList<String>();
            _foilUncommons.addAll(uncommons);

            _foilCommons = new ArrayList<String>();
            _foilCommons.addAll(commons);

            _leagueRares = new ArrayList<String>();
            _leagueRares.addAll(rares);

            _leagueShadowUncommons = new ArrayList<String>();
            _leagueFPUncommons = new ArrayList<String>();
            _leagueSiteUncommons = new ArrayList<String>();

            for (String uncommon : uncommons) {
                LotroCardBlueprint blueprint = _library.getLotroCardBlueprint(uncommon);
                if (blueprint.getSide() == Side.FREE_PEOPLE)
                    _leagueFPUncommons.add(uncommon);
                else if (blueprint.getSide() == Side.SHADOW)
                    _leagueShadowUncommons.add(uncommon);
                else
                    _leagueSiteUncommons.add(uncommon);
            }


            _leagueShadowCommons = new ArrayList<String>();
            _leagueFPCommons = new ArrayList<String>();

            for (String common : commons) {
                LotroCardBlueprint blueprint = _library.getLotroCardBlueprint(common);
                if (blueprint.getSide() == Side.FREE_PEOPLE)
                    _leagueFPCommons.add(common);
                else if (blueprint.getSide() == Side.SHADOW)
                    _leagueShadowCommons.add(common);
            }
        }
    }

    private List<String> replaceRarity(String rarity, List<String> values) {
        List<String> result = new ArrayList<String>();
        for (String value : values)
            result.add(value.replace(rarity, "_"));
        return result;
    }
}
