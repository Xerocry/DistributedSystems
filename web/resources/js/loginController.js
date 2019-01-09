/**
 * Created by kivi on 15.12.17.
 */

function InfoShareService() {
    var user = {};
    return {
        setUser: function (value) {
            user = value;
        },
        getUser: function () {
            return user;
        }
    };
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function LoginController($scope, $http, UserService, InfoShareService) {
    this.isRegister = false;
    this.signIn = function () {
        if (!$scope.login) {
            alert("Enter login");
        } else if (!$scope.passwd) {
            alert("Enter password");
        } else {
            $http.get('rest/user/' + $scope.login + '/authenticate?passwd=' + $scope.passwd)
                .then(function (responce) {
                    if (responce.data.toString() == "true") {
                        InfoShareService.setUser(UserService.get({login:$scope.login}));
                        window.location.href = '#/user/' + $scope.login;
                    } else {
                        alert("Incorrect login or password");
                        $scope.login = "";
                        $scope.passwd = "";
                    }
                }, function (error) {
                    alert("Incorrect login or password");
                    $scope.login = "";
                    $scope.passwd = "";
                });
        }
    };

    this.registerUser = function () {
        if (isEmpty($scope.loginReg)) {
            console.log($scope.loginReg);
            alert("Enter the login");
        } else if (isEmpty($scope.name)) {
            alert("Enter the name");
        } else if (isEmpty($scope.password1)) {
            alert("Enter the password");
        } else if ($scope.password1 != $scope.password2) {
            alert("Passwords should be equal");
        } else {
            var user = new UserService();
            user.login = $scope.loginReg;
            user.name = $scope.name;
            user.password = $scope.password1;
            user.$save({login:$scope.loginReg}, function () {
                $scope.loginReg = "";
                $scope.name = "";
                $scope.password1 = "";
                $scope.password2 = "";
                this.isRegister = false;
            }.bind(this), function (error) {
                alert(error.data.message);
            });
        }
    }.bind(this);
}

app
    .service('InfoShareService', InfoShareService)
    .controller('LoginController', LoginController);

