angular.module("buginator.applicationsController", []).config(function ($stateProvider) {
    $stateProvider.state("applications", {
        url: "/application",
        templateUrl: "html/application/applications.html",
        controller: "applicationsController"
    });
}).controller("applicationsController", function ($scope, $state, applicationRestService, exceptionHandler) {

    applicationRestService.getAll(
        function(returnedData){
            $scope.applications = returnedData.data;
        }, exceptionHandler.handleRestError
    );
});