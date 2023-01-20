$(document).ready(
    function () {
        var ui = new GempLotrMerchantUI($("#cardList"), $("#cardFilter"));

        $(window).resize(function () {
            layoutUi(ui);
        });

        layoutUi(ui);
    });

function layoutUi(ui) {
var width = $(window).width();
var height = $(window).height();
if (width < 800)
    width = 800;
if (height < 400)
    height = 400;

var padding = 5;
var filterHeight = 160;

$("#cardFilter").css({position:"absolute", left:padding, top:padding, width:width - padding * 2, height:filterHeight});
$("#cardList").css({position:"absolute", left:padding, top:2 * padding + filterHeight, width:width - padding * 2, height:height - filterHeight - padding * 3});
ui.layoutUI();
}