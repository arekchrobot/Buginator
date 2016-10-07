angular.module("buginator.baseRestService", [])
    .factory("baseRestService", function ($http, exceptionHandler) {
        var service = {};

        /*
         * This needs to be changed by inherited object to make REST work properly
         * It represents the url for object we want to fetch
         * Example: if we want about rest api then this value should be "about/"
         */
        service.baseObjectUrl = "/";

        service.get = function (id, successFunction) {
            $http.get(this.baseObjectUrl + id, {})
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

        service.delete = function (id, successFunction) {
            $http.delete(this.baseObjectUrl + id, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        return service;
    });