angular.module("buginator.authController", []).config(function ($stateProvider) {
    $stateProvider.state("login", {
        url: "/login",
        templateUrl: "html/auth/login.html"
    });
}).controller("authController", function ($rootScope, $scope, $window, $state, authRestService, authService, registerService, $timeout) {
    authService.initScope($scope);

    $scope.authenticate = function (credentials) {
        authRestService.authenticate(credentials,
            function (returnedData) {
                $rootScope.user = returnedData;
                $scope.loginError = false;
                $rootScope.user.perms = authService.createPermissions($rootScope.user);
                $scope.credentials = {};
                $state.go("dashboard")
                    .then(function(){
                        //TODO: implement better approach to refresh scope view
                        $timeout(function() {
                            window.location.reload();
                        }, 0);
                });
            }, function (returnedData) {
                authService.loginError($scope);
            });
    };

    $rootScope.$state = $state;

    $scope.login = function () {
        $scope.authenticate($scope.credentials);
    };

    var loggedError = function (returnedData) {
        $rootScope.user = null;
    };

    authRestService.isLogged(loggedError)
        .then(function (returnedData) {
            $rootScope.user = returnedData;
            $rootScope.user.perms = authService.createPermissions($rootScope.user);
            $rootScope.$broadcast('establishNotificationWebSocket');
        }, loggedError);

    $scope.logout = function () {
        authRestService.logout(
            function (returnedData) {
                $rootScope.user = null;
                $state.go("login");
            }, function (returnedData) {
                $rootScope.user = null;
                $state.go("login");
            });
    };

    $scope.register = function () {
        registerService.register($scope.registerData,
            function (returnedData) {
                authService.registerClearFormWithSuccess($scope);
                $("a[href=#login]").tab("show");
            }, function (returnedData) {
                authService.registerFormError($scope, returnedData);
            });
    };

    $scope.resetPass = function () {
        authRestService.remindPassword($scope.credentials.username,
            function (returnedData) {
                authService.forgotClearFormWithSuccess($scope);
                $("a[href=#login]").tab("show");
            }, function (returnedData) {
                authService.forgotFormError($scope, returnedData);
            });
    }
});