angular.module("buginator.detailsAggregatorController", []).config(function ($stateProvider) {
    $stateProvider.state("detailsAggregator", {
        url: "/application/:appId/aggregator/:id",
        templateUrl: "html/aggregator/aggregator.html",
        controller: "detailsAggregatorController"
    });
}).controller("detailsAggregatorController", function ($scope, $state, $stateParams, aggregatorRestService, aggregatorWizardService) {

    $scope.appId = $stateParams.appId;

    $scope.id = $stateParams.id;

    $scope.service = aggregatorWizardService;

    $scope.editable = false;

    aggregatorRestService.get($scope.id,
        function (returnedData) {
            $scope.aggregatorData = returnedData.data;
        }
    );
});