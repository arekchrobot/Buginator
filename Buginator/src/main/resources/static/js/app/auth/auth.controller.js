angular.module("buginator.authController", []).config(function ($stateProvider) {
    $stateProvider.state("login", {
        url: "/login",
        templateUrl: "html/auth/login.html"
    });
}).controller("authController", function ($rootScope, $scope, $state, authRestService, authService, registerService, $translate) {
    authService.initScope($scope);

    $scope.authenticate = function (credentials) {
        authRestService.authenticate(credentials,
            function (returnedData) {
                $rootScope.user = returnedData;
                $scope.loginError = false;
                $rootScope.user.perms = authService.createPermissions($rootScope.user);
                $scope.credentials = {};
                //$state.go("home");
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