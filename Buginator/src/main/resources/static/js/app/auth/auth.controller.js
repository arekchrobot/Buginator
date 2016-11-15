angular.module("buginator.authController", []).config(function ($stateProvider) {
    $stateProvider.state("login", {
        url: "/login",
        templateUrl: "html/auth/login.html"
    });
}).controller("authController", function ($rootScope, $scope, $state, authService, registerService) {
    $scope.credentials = {};
    $rootScope.loginError = false;
    $scope.registerData = {};
    $scope.rePassword = null;
    $scope.registerError = false;

    $scope.authenticate = function (credentials) {
        authService.authenticate(credentials,
            function (returnedData) {
                $rootScope.user = returnedData;
                $rootScope.loginError = false;
                $rootScope.user.perms = authService.createPermissions($rootScope.user);
                $scope.credentials = {};
                //$state.go("home");
            }, function (returnedData) {
                $scope.credentials = {};
                $rootScope.loginError = true;
            });
    };

    $rootScope.$state = $state;

    $scope.login = function () {
        $scope.authenticate($scope.credentials);
    };

    var loggedError = function (returnedData) {
        $rootScope.user = null;
    };

    authService.isLogged(loggedError)
        .then(function (returnedData) {
            $rootScope.user = returnedData;
            $rootScope.user.perms = authService.createPermissions($rootScope.user);
        }, loggedError);

    $scope.logout = function () {
        authService.logout(
            function (returnedData) {
                $rootScope.user = null;
                $state.go("login");
            }, function (returnedData) {
                console.log(returnedData);
                $rootScope.user = null;
                $state.go("login");
            });
    };

    $scope.register = function () {
        registerService.register($scope.registerData,
            function (returnedData) {
                $scope.registerError = false;
                $("a[href=#login]").tab("show");
            }, function (returnedData) {
                $scope.registerError = true;
                $scope.registerErrorMsg = returnedData.data.message;
            });
    };

    $scope.resetPass = function () {
        authService.remindPassword($scope.credentials.username,
            function (returnedData) {

            }, function (returnedData) {

            });
    }
});