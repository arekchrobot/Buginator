angular.module("buginator.registerService", [])
    .factory("authService", function ($http) {
        var service = {};



        service.register = function (credentials, successFunction, failureFunction) {
            $http.post("/auth/login", credentials)
                .then(successFunction, failureFunction);
        };

        return service;
    });