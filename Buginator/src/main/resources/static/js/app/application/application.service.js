angular.module("buginator.applicationService", [])
    .factory("applicationRestService", function ($http, baseRestService) {
        var service = angular.copy(baseRestService);

        service.baseObjectUrl = "/application/";

        return service;
    }).factory("applicationService", function() {
        var service = {};

        service.initCreateAppScope = function($scope) {
            $scope.editable = true;
            $scope.application = {};
            $scope.forms = {};
        };

        service.initDetailsAppScope = function($scope) {
            $scope.editable = false;
        };

        return service;
    });