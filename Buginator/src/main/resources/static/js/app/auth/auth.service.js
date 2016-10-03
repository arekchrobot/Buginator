angular.module("buginator.authService", [])
    .factory("authService", function ($http) {
        var service = {};

        service.authenticate = function (credentials, successFunction, failureFunction) {
            $http.post("/auth/login", credentials)
                .then(successFunction, failureFunction);
        };

        service.logout = function (successFunction, failureFunction) {
            $http.post("/auth/logout", {})
                .then(successFunction, failureFunction);
        };

        service.isLogged = function (successFunction, failureFunction) {
            $http.get("/auth/logged", {})
                .then(successFunction, failureFunction);
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

        return service;
    });