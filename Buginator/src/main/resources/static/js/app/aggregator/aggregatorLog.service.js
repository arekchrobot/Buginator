angular.module("buginator.aggregatorLogServices", [])
    .factory("aggregatorLogRestService", function ($http, exceptionHandler) {
        var service = {};

        service.baseObjectUrl = "/aggregatorLog/";

        service.getAllByAggregator = function (id, successFunction) {
            $http.get(this.baseObjectUrl + id, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        return service;
    });