angular.module("buginator.createApplicationController", []).config(function ($stateProvider) {
    $stateProvider.state("createApplication", {
        url: "/application/create",
        templateUrl: "html/application/application.html",
        controller: "createApplicationController"
    });
}).controller("createApplicationController", function ($scope, $state, applicationRestService, exceptionHandler) {

    $scope.editable = true;
    $scope.application = {};
    $scope.forms = {};

    $scope.submit = function () {
        applicationRestService.save($scope.application,
            function (returnedData) {
                $state.go("applications")
            }, exceptionHandler.handleRestError
        );
    };
});