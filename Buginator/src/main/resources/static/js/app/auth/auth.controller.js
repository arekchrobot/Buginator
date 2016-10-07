angular.module("buginator.authController", []).config(function ($stateProvider) {
    $stateProvider.state("login", {
        url: "/login",
        templateUrl: "html/auth/login.html"
    });
}).controller("authController", function ($rootScope, $scope, $state, authService) {
    $scope.credentials = {};
    $rootScope.loginError = false;

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

    authService.isLogged(
        function (returnedData) {
            $rootScope.user = returnedData;
            $rootScope.user.perms = authService.createPermissions($rootScope.user);
        }, function (returnedData) {
            $rootScope.user = null;
        });

    $scope.logout = function () {
        authService.logout(
            function (returnedData) {
                $rootScope.user = null;
                $state.go("login");
            }, function (returnedData) {
                $rootScope.user = null;
                $state.go("login");
            });
    };
});