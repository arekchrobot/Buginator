angular.module('buginator.exceptionHandler', [])
    .factory('exceptionHandler', function ($state, $rootScope, ngDialog) {
        var service = {};

        service.handleRestError = function (error) {
            if (error.status !== undefined && error.status === 401) {
                $rootScope.user = null;
                var data = {};
                data.status = error.status;
                data.description = "Unauthorized access";
                ngDialog.open({template: "html/error/error.html", data: data});
                $state.go("login");
            } else if (error.status !== undefined) {
                var data = {};
                data.status = error.status;
                data.description = error.description;
                ngDialog.open({template: "html/error/error.html", data: data});
            }
        };

        return service;
    });