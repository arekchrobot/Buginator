angular.module("buginator.applicationManageUserServices", [])
    .factory("appManageUserRestService", function ($http, exceptionHandler) {
        var service = {};

        service.baseObjectUrl = "/manageUser/";

        service.getApplicationUsers = function (appId, successFunction) {
            $http.get(this.baseObjectUrl + "byApplication/" + appId, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.getUsersNotBoundToApplication = function (appId, successFunction) {
            $http.get(this.baseObjectUrl + "byApplicationNot/" + appId, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.addUsersToApplication = function (userList, appId, successFunction) {
            $http.post(this.baseObjectUrl + appId, userList)
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.removeUsersFromApplication = function (userList, appId, successFunction) {
            $http({
                url: this.baseObjectUrl + appId,
                method: "DELETE",
                data: userList,
                headers: {"Content-Type": "application/json;charset=utf-8"}
            }).then(successFunction, exceptionHandler.handleRestError);
        };

        service.updateUserPermissions = function(userData, appId, successFunction) {
            $http.put(this.baseObjectUrl + appId, userData)
                .then(successFunction, exceptionHandler.handleRestError);
        };

        return service;
    }).factory("appManageUserService", function () {
        var service = {};

        service.initScope = function ($scope) {
            $scope.selectedUsersToAdd = [];
            $scope.selectedUsersToDelete = [];
            $scope.selectedRemove = [];
            $scope.selectedAdd = [];
            $scope.usersNotBoundToApp = [];
            $scope.appUsers = [];
            $scope.switchButton = {
                yes: "YES",
                no: "NO"
            };
        };

        service.addRemoveUser = function (collection, item, isAdd) {
            if (isAdd) {
                collection.push(item);
            } else {
                var itemIndex = collection.indexOf(item);
                if (itemIndex > -1) {
                    collection.splice(itemIndex, 1);
                }
            }
        };

        service.clearCheckboxes = function (collection, checkboxes) {
            for (var i = 0; i < collection.length; i++) {
                checkboxes[i] = false;
            }
        };

        return service;
    });