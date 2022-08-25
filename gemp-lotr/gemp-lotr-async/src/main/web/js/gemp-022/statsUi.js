var StatsUI = Class.extend({
    communication:null,
    paramsDiv:null,
    statsDiv:null,

    init:function (url, paramControl, statControl) {
        this.communication = new GempLotrCommunication(url,
            function (xhr, ajaxOptions, thrownError) {
            });

        this.paramsDiv = paramControl;
        this.statsDiv = statControl;

        var now = new Date();
        var d = now.getDate();
        now.setMonth(now.getMonth() - 1);
        if (now.getDate() != d) {
          now.setDate(0);
        }
        var nowStr = now.getFullYear() + "-" + (1 + now.getMonth()) + "-" + now.getDate();

        $(".startDay").val(nowStr);
        
        var that = this;

        $(".getStats", this.paramsDiv).click(
            function () {
                var startDay = $(".startDay", that.paramsDiv).prop("value");
                var period = $("option:selected", $(".period", that.paramsDiv)).prop("value");

                that.communication.getStats(startDay, period, that.loadedStats, {
                    "400":function () {
                        alert("Invalid parameter entered");
                    }
                })
            });
    },

    loadedStats:function (xml) {
        var that = this;
        log(xml);
        var root = xml.documentElement;
        if (root.tagName == 'stats') {
            $("#stats").html("");

            var stats = root;

            var activePlayers = stats.getAttribute("activePlayers");
            var gamesCount = stats.getAttribute("gamesCount");
            var start = stats.getAttribute("start");
            var end = stats.getAttribute("end");

            $("#stats").append("<div class='period'>Stats for " + start + " - " + end + "</div>");
            $("#stats").append("<div class='activePlayers'>Active players: " + activePlayers + "</div>");
            $("#stats").append("<div class='gamesCount'>All games count: " + gamesCount + "</div>");

            var formatStats = stats.getElementsByTagName("formatStat");
            if (formatStats.length > 0) {
                $("#stats").append("<div class='tableHeader'>Casual games per format</div>");

                var table = $("<table class='tables'></table>");
                table.append("<tr><th>Format name</th><th># of games</th><th>% of casual</th></tr>");
                for (var i = 0; i < formatStats.length; i++) {
                    var formatStat = formatStats[i];
                    table.append("<tr><td>" + formatStat.getAttribute("format") + "</td><td>" + formatStat.getAttribute("count") + "</td><td>" + formatStat.getAttribute("perc") + "</td></tr>");
                }

                $("#stats").append(table);
            }
        }
    }
});