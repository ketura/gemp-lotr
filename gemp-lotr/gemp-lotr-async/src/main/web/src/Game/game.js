var ui;
var communication;

$(document).ready(
    function () {
        var replay = getUrlParam("replayId");

        ui = new GempLotrGameUI("/gemp-lotr-server", replay != null);

        $(window).resize(function () {
            ui.windowResized();
        });

        ui.layoutUI(true);

        if (replay == null)
            ui.startGameSession();
        else
            ui.startReplaySession(replay);
    });