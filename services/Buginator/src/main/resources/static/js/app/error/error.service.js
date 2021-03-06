angular.module("buginator.errorServices", [])
    .factory("errorRestService", function ($http, exceptionHandler) {
        var service = {};

        service.baseObjectUrl = "/error/";

        service.get = function (id, successFunction) {
            $http.get(this.baseObjectUrl + id, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.getAllByApplication = function (appId, successFunction) {
            $http.get(this.baseObjectUrl + "byApplication/" + appId, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.save = function (data, successFunction, errorFunction) {
            $http.post(this.baseObjectUrl, data)
                .then(successFunction, errorFunction);
        };

        return service;
    });