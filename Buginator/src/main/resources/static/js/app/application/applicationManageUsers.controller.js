angular.module("buginator.applicationManageUsersController", []).config(function ($stateProvider) {
    $stateProvider.state("applicationManageUsers", {
        url: "/application/:appId/manageUsers",
        templateUrl: "html/application/applicationUsers.html",
        controller: "applicationManageUsersController"
    });
}).controller("applicationManageUsersController", function ($scope, $state, $stateParams, $translate, applicationRestService, exceptionHandler,
                                                            appManageUserRestService, appManageUserService) {

    $scope.appId = $stateParams.appId;

    appManageUserService.initScope($scope);

    $translate("YES").then(function (translatedValue) {
        $scope.switchButton.yes = translatedValue;
    });

    $translate("NO").then(function (translatedValue) {
        $scope.switchButton.no = translatedValue;
    });

    applicationRestService.get($stateParams.appId,
        function (returnedData) {
            $scope.application = returnedData.data;
        }
    );

    appManageUserRestService.getApplicationUsers($stateParams.appId,
        function (returnedData) {
            $scope.appUsers = returnedData.data;
            for (var i = 0; i < $scope.appUsers.length; i++) {
                $scope.selectedRemove[i] = false;
            }
        }
    );

    appManageUserRestService.getUsersNotBoundToApplication($stateParams.appId,
        function (returnedData) {
            $scope.usersNotBoundToApp = returnedData.data;
            for (var i = 0; i < $scope.usersNotBoundToApp.length; i++) {
                $scope.selectedAdd[i] = false;
            }
        }
    );

    $scope.addUser = function (userToAdd, index) {
        appManageUserService.addRemoveUser($scope.selectedUsersToAdd, userToAdd, $scope.selectedAdd[index]);
    };

    $scope.removeUser = function (userToRemove, index) {
        appManageUserService.addRemoveUser($scope.selectedUsersToDelete, userToRemove, $scope.selectedRemove[index]);
    };

    $scope.changeModifyForUser = function (appUser) {
        appManageUserRestService.updateUserPermissions(appUser, $scope.appId, function (returnedData) {
            if (returnedData.data.result !== "OK") {
                appUser.modify = !appUser.modify;
                exceptionHandler.handleCustomMessageError(409, returnedData.data.result);
            }
        });
    };

    $scope.addSelectedUsers = function () {
        appManageUserRestService.addUsersToApplication($scope.selectedUsersToAdd, $scope.appId, function (returnedData) {
            if (returnedData.data.result === "OK") {
                for (var i = 0; i < $scope.selectedUsersToAdd.length; i++) {
                    appManageUserService.addRemoveUser($scope.appUsers, $scope.selectedUsersToAdd[i], true);
                    appManageUserService.addRemoveUser($scope.usersNotBoundToApp, $scope.selectedUsersToAdd[i], false);
                }
                $scope.selectedUsersToDelete = [];
                $scope.selectedUsersToAdd = [];
                appManageUserService.clearCheckboxes($scope.appUsers, $scope.selectedRemove);
                appManageUserService.clearCheckboxes($scope.usersNotBoundToApp, $scope.selectedAdd);
            } else {
                exceptionHandler.handleManageUsersError(returnedData);
            }
        });

    };

    $scope.removeSelectedUsers = function () {
        appManageUserRestService.removeUsersFromApplication($scope.selectedUsersToDelete, $scope.appId, function (returnedData) {
            if (returnedData.data.result === "OK") {
                for (var i = 0; i < $scope.selectedUsersToDelete.length; i++) {
                    appManageUserService.addRemoveUser($scope.usersNotBoundToApp, $scope.selectedUsersToDelete[i], true);
                    appManageUserService.addRemoveUser($scope.appUsers, $scope.selectedUsersToDelete[i], false);
                }
                $scope.selectedUsersToDelete = [];
                $scope.selectedUsersToAdd = [];
                appManageUserService.clearCheckboxes($scope.appUsers, $scope.selectedRemove);
                appManageUserService.clearCheckboxes($scope.usersNotBoundToApp, $scope.selectedAdd);
            } else {
                exceptionHandler.handleManageUsersError(returnedData);
            }
        });
    };
});
