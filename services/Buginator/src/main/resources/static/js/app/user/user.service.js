angular.module("buginator.userServices", [])
    .factory("userRestService", function ($http, exceptionHandler) {
        var service = {};

        service.baseObjectUrl = "/user/";

        service.get = function (email, successFunction) {
            $http.get(this.baseObjectUrl + email, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.getAll = function (successFunction) {
            $http.get(this.baseObjectUrl, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.save = function (data, successFunction) {
            $http.post(this.baseObjectUrl, data)
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.put = function (email, active, successFunction) {
            $http.put(this.baseObjectUrl + email + "/" + active, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        return service;
    });