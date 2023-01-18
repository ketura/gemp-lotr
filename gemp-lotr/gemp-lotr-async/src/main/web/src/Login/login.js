
var comm = new GempLotrCommunication("/gemp-lotr-server", function () {
    alert("Unable to contact the server");
});

function register() {
    var login = $("#login").val();
    var password = $("#password").val();
    var password2 = $("#password2").val();
    if (password != password2) {
        $(".error").html("Password and Password repeated are different! Try again");
    } else {
        comm.register(login, password, function () {
                location.href = "/gemp-lotr/hall.html";
            },
            {
                "0": function () {
                    alert("Unable to connect to server, either server is down or there is a problem" +
                        " with your internet connection");
                },
                "400": function () {
                    $(".error").html("Login is invalid. Login must be between 2-10 characters long, and contain only<br/>" +
                        " english letters, numbers or _ (underscore) and - (dash) characters.");
                },
                "409": function () {
                    $(".error").html("User with this login already exists in the system. Try a different one.");
                },
                "503": function () {
                    $(".error").html("Server is down for maintenance. Please come at a later time.");
                }
            });
    }

}

function registrationScreen() {
    comm.getRegistrationForm(
        function (html) {
            $(".error").html();
            $(".interaction").html(html);
            $("#registerButton").button().click(register);
        });
}

function login() {
    var login = $("#login").val();
    var password = $("#password").val();
    comm.login(login, password, function () {
            location.href = "/gemp-lotr/hall.html";
        },
        {
            "0": function () {
                alert("Unable to connect to server, either server is down or there is a problem" +
                    " with your internet connection");
            },
            "401": function () {
                $(".error").html("Invalid username or password. Try again.");
                loginScreen();
            },
            "403": function () {
                $(".error").html("You have been permanently banned. If you think it was a mistake you can try sending a private message to Merrick_H on <a href='http://lotrtcgwiki.com/forums/'>TLHH forums</a>.");
                $(".interaction").html("");
            },
            "409": function () {
                $(".error").html("You have been temporarily banned. You can try logging in at a later time. If you think it was a mistake you can try sending a private message to Merrick_H on <a href='http://lotrtcgwiki.com/forums/'>TLHH forums</a>.");
                $(".interaction").html("");
            },
            "503": function () {
                $(".error").html("Server is down for maintenance. Please come at a later time.");
            }
        });
}

function loginScreen() {
    $(".interaction").html("");
    $(".interaction").append("Login below, or ");
    var registerButton = $("<div>Register</div>").button();
    registerButton.click(registrationScreen);

    $(".interaction").append(registerButton);
    $(".interaction").append("<br/>Login: <input id='login' type='text'><br/>Password: <input id='password' type='password'><br/>");

    var loginButton = $("<div>Login</div>").button();
    loginButton.click(login);

    $("#password").keypress(function (e) {
        if (e.which == 13) {
            login();
            e.preventDefault();
            return false;
        }
    });

    $(".interaction").append(loginButton);
}

$(document).ready(
    function () {
        comm.getStatus(
            function (html) {
                $(".status").append(html);
            });
        loginScreen();
    });