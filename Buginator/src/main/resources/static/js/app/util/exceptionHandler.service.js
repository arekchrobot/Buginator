angular.module('buginator.exceptionHandler', [])
    .factory('exceptionHandler', function ($state, $rootScope, $window, $translate, ngDialog) {
        var service = {};

        service.handleRestError = function (error) {
            if (error.status !== undefined && error.status === 401) {
                var data = {};
                data.status = error.status;
                data.description = $translate.instant("UNAUTHORIZED_ACCESS");
                ngDialog.open({template: "html/error/error.html", data: data});
                if ($rootScope.user == null || $rootScope.user == undefined || $rootScope.user == "") {
                    if(!$window.location.href.includes("login")) {
                        console.log($window.location.href);
                        $rootScope.originUrl = $window.location.href;
                    }
                    $state.go("login");
                }
            } else if(error.status !== undefined && error.status === 403) {
                var data = {};
                data.status = error.data.status;
                data.description = $translate.instant("USER_NOT_PERMITTED");
                ngDialog.open({template: "html/error/error.html", data: data});
            }  else if (error.status !== undefined) {
                var data = {};
                data.status = error.data.status;
                data.description = error.data.message;
                ngDialog.open({template: "html/error/error.html", data: data});
            }
        };

        service.handleManageUsersError = function (error, isAdd) {
            var data = {};
            data.status = 409;
            if (isAdd) {
                data.description = $translate.instant("ADD_USERS_ERROR");
            } else {
                data.description = $translate.instant("REMOVE_USERS_ERROR");
            }
            ngDialog.open({template: "html/error/error.html", data: data});
        };

        service.handleCustomMessageError = function(httpStatus, message) {
            var data = {};
            data.status = httpStatus;
            data.description = message;
            ngDialog.open({template: "html/error/error.html", data: data});
        };

        return service;
    });