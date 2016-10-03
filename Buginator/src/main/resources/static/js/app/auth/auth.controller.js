angular.module("buginator.authController", [])
    .controller("authController", function ($rootScope, $scope, $state, authService, $location) {
        $scope.credentials = {};
        $rootScope.loginError = false;

        $scope.authenticate = function (credentials) {
            authService.authenticate(credentials,
                function (returnedData) {
                    $rootScope.user = returnedData;
                    $rootScope.loginError = false;
                    $rootScope.user.perms = authService.createPermissions($rootScope.user);
                    $scope.credentials = {};
                }, function (returnedData) {
                    $scope.credentials = {};
                    $rootScope.loginError = true;
                });
        };

        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };

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
                    //$state.go("login");
                }, function (returnedData) {
                    $rootScope.user = null;
                    //$state.go("login");
                });
        };
    });