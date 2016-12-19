angular.module("buginator.applicationService", [])
    .factory("applicationRestService", function ($http, baseRestService) {
        var service = angular.copy(baseRestService);

        service.baseObjectUrl = "/application/";

        return service;
    });