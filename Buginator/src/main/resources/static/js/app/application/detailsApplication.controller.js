angular.module("buginator.detailsApplicationController", []).config(function ($stateProvider) {
    $stateProvider.state("detailsApplication", {
        url: "/application/:id",
        templateUrl: "html/application/application.html",
        controller: "detailsApplicationController"
    });
}).controller("detailsApplicationController", function ($scope, $state, $stateParams, applicationRestService, exceptionHandler, applicationService, chartService) {

    applicationService.initDetailsAppScope($scope);

    applicationRestService.get($stateParams.id,
        function (returnedData) {
            $scope.application = returnedData.data;
        }
    );

    chartService.generateChartDataForLastWeekApp($scope, $stateParams.id);
});