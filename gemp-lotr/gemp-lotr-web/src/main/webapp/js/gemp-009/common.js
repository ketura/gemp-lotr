var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

function formatToTwoDigits(no) {
    if (no < 10)
        return "0" + no;
    else
        return no;
}

function formatDate(date) {
    return monthNames[date.getMonth()] + " " + date.getDate() + " " + formatToTwoDigits(date.getHours()) + ":" + formatToTwoDigits(date.getMinutes()) + ":" + formatToTwoDigits(date.getSeconds());
}

function formatPrice(price) {
    var silver = (price % 100);
    return Math.floor(price / 100) + "<img src='images/gold.png'/> " + ((silver < 10) ? ("0" + silver) : silver) + "<img src='images/silver.png'/>";
}

function getDateString(date) {
    return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
}
