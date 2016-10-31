angular.module("buginator.registerService", [])
    .factory("registerService", function ($http) {
        var service = {};

        service.register = function (registerData, successFunction, failureFunction) {
            $http.post("/auth/register", registerData)
                .then(successFunction, failureFunction);
        };

        service.validateInput = function(registerData) {

        };

        return service;
    });