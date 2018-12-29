angular.module("buginator.detailsApplicationController", []).config(function ($stateProvider) {
    $stateProvider.state("detailsApplication", {
        url: "/application/:id",
        templateUrl: "html/application/application.html",
        controller: "detailsApplicationController"
    });
}).controller("detailsApplicationController", function ($scope, $state, $stateParams, applicationRestService, exceptionHandler, applicationService,
                                                        chartService, errorRestService) {

    applicationService.initDetailsAppScope($scope);

    applicationRestService.get($stateParams.id,
        function (returnedData) {
            $scope.application = returnedData.data;
        }
    );

    errorRestService.getAllByApplication($stateParams.id,
        function (returnedData) {
            $scope.errors = returnedData.data;
        }
    );

    chartService.generateChartDataForLastWeekApp($scope, $stateParams.id);
});