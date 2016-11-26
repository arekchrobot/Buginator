angular.module("buginator.authService", [])
    .factory("authRestService", function ($http, $cacheFactory, cacheService) {
        var service = {};

        var LOGGED_CACHE_TIME_MIN = 3;

        var cacheKeys = {
            logged: "LOGGED",
            lastSyncDate: "LOGGED_DATE",
            id: "LOGGED_ID"
        };

        service.cache = $cacheFactory(cacheKeys.id);

        service.authenticate = function (credentials, successFunction, failureFunction) {
            $http.post("/auth/login", credentials)
                .then(successFunction, failureFunction);
        };

        service.logout = function (successFunction, failureFunction) {
            $http.post("/auth/signout", {})
                .then(successFunction, failureFunction);
        };

        service.isLogged = function (failureFunction) {
            var cache = this.cache;
            var loggedUser = cache.get(cacheKeys.logged);
            var lastSyncDate = cache.get(cacheKeys.lastSyncDate);

            if (loggedUser == null || cacheService.shouldSynchronize(lastSyncDate, LOGGED_CACHE_TIME_MIN)) {
                loggedUser = $http.get("/auth/logged", {})
                    .then(function (response) {
                        cache.put(cacheKeys.lastSyncDate, new Date());
                        return response.data;
                    }, failureFunction);
            }
            cache.put(cacheKeys.logged, loggedUser);
            return loggedUser;
        };

        service.remindPassword = function (email, successFunction, failureFunction) {
            $http.put("/auth/reset?lg=" + email, {})
                .then(successFunction, failureFunction);
        };

        return service;
    }).factory("authService", function($translate){
        var service = {};

        service.createPermissions = function (user) {
            var permissions = {};
            if (user.perms != null || user.perms != undefined) {
                for (var i = 0; i < user.perms.length; i++) {
                    permissions[user.perms[i]] = true;
                }
            }
            return permissions;
        };

        service.initScope = function ($scope) {
            $scope.credentials = {};
            $scope.loginError = false;
            $scope.registerData = {};
            $scope.rePassword = null;
            $scope.registerError = false;
            $scope.forgotError = false;
            $scope.forgotSuccess = false;
            $scope.registerSuccess = false;
            $scope.forms = {};
        };

        service.registerClearFormWithSuccess = function ($scope) {
            $scope.forms.registerForm.$setPristine();
            $scope.forms.registerForm.$setUntouched();
            $scope.registerError = false;
            $scope.registerData = {};
            $scope.rePassword = "";
            $scope.rePassword = null;
            $scope.registerSuccess = true;
            $scope.registerSuccessMsg = $translate.instant("SIGNUP_SUCCESS_MSG");
        };

        service.forgotClearFormWithSuccess = function ($scope) {
            $scope.forms.forgotForm.$setPristine();
            $scope.forms.forgotForm.$setUntouched();
            $scope.credentials = {};
            $scope.forgotError = false;
            $scope.forgotSuccess = true;
            $scope.forgotSuccessMsg = $translate.instant("FORGOT_SUCCESS_MSG");
        };

        service.loginError = function($scope) {
            $scope.credentials = {};
            $scope.loginError = true;
            $scope.loginErrorMsg = $translate.instant("LOGIN_ERROR_MSG");
        };

        service.registerFormError = function($scope, returnedData) {
            $scope.registerError = true;
            $scope.registerSuccess = false;
            $scope.registerErrorMsg = returnedData.data.message;
        };

        service.forgotFormError = function($scope, returnedData) {
            $scope.forgotError = true;
            $scope.forgotSuccess = false;
            $scope.forgotErrorMsg = returnedData.data.message;
        };

        return service;
    });