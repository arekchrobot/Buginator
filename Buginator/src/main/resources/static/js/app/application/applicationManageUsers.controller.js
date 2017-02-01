angular.module("buginator.applicationManageUsersController", []).config(function ($stateProvider) {
    $stateProvider.state("applicationManageUsers", {
        url: "/application/:appId/manageUsers",
        templateUrl: "html/application/applicationUsers.html",
        controller: "applicationManageUsersController"
    });
}).controller("applicationManageUsersController", function ($scope, $state, $stateParams, applicationRestService, exceptionHandler, applicationService) {

    $scope.appId = $stateParams.appId;

    applicationRestService.get($stateParams.appId,
        function (returnedData) {
            $scope.application = returnedData.data;
        }
    );

    $scope.selectedUsers = [];
    $scope.omgUsers = [];
});
