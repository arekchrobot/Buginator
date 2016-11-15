angular.module("buginator.authService", [])
    .factory("authService", function ($http, $cacheFactory, cacheService) {
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

        service.createPermissions = function (user) {
            var permissions = {};
            if (user.perms != null || user.perms != undefined) {
                for (var i = 0; i < user.perms.length; i++) {
                    permissions[user.perms[i]] = true;
                }
            }
            return permissions;
        };

        service.remindPassword = function (email, successFunction, failureFunction) {
            $http.put("/auth/reset?lg=" + email, {})
                .then(successFunction, failureFunction);
        };

        return service;
    });