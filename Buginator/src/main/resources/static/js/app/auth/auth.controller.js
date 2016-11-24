angular.module("buginator.authController", []).config(function ($stateProvider) {
    $stateProvider.state("login", {
        url: "/login",
        templateUrl: "html/auth/login.html"
    });
}).controller("authController", function ($rootScope, $scope, $state, authService, registerService, $translate) {
    $scope.credentials = {};
    $scope.loginError = false;
    $scope.registerData = {};
    $scope.rePassword = null;
    $scope.registerError = false;
    $scope.forgotError = false;
    $scope.forgotSuccess = false;
    $scope.registerSuccess = false;

    $scope.forms = {};

    $scope.authenticate = function (credentials) {
        authService.authenticate(credentials,
            function (returnedData) {
                $rootScope.user = returnedData;
                $scope.loginError = false;
                $rootScope.user.perms = authService.createPermissions($rootScope.user);
                $scope.credentials = {};
                //$state.go("home");
            }, function (returnedData) {
                $scope.credentials = {};
                $scope.loginError = true;
                $scope.loginErrorMsg = $translate.instant("LOGIN_ERROR_MSG");
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
                $scope.forms.registerForm.$setPristine();
                $scope.forms.registerForm.$setUntouched();
                $scope.registerError = false;
                $scope.registerData = {};
                $scope.rePassword = "";
                $scope.rePassword = null;
                $scope.registerSuccess = true;
                $scope.registerSuccessMsg = $translate.instant("SIGNUP_SUCCESS_MSG");
                $("a[href=#login]").tab("show");
            }, function (returnedData) {
                $scope.registerError = true;
                $scope.registerSuccess = false;
                $scope.registerErrorMsg = returnedData.data.message;
            });
    };

    $scope.resetPass = function () {
        authService.remindPassword($scope.credentials.username,
            function (returnedData) {
                $scope.forms.forgotForm.$setPristine();
                $scope.forms.forgotForm.$setUntouched();
                $scope.credentials = {};
                $scope.forgotError = false;
                $scope.forgotSuccess = true;
                $scope.forgotSuccessMsg = $translate.instant("FORGOT_SUCCESS_MSG");
                $("a[href=#login]").tab("show");
            }, function (returnedData) {
                $scope.forgotError = true;
                $scope.forgotSuccess = false;
                $scope.forgotErrorMsg = returnedData.data.message;
            });
    }
});