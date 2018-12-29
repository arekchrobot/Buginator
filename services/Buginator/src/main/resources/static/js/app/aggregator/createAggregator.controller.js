angular.module("buginator.createAggregatorController", []).config(function ($stateProvider) {
    $stateProvider.state("createAggregator", {
        url: "/application/:appId/aggregators/create",
        templateUrl: "html/aggregator/createAggregator.html",
        controller: "createAggregatorController"
    });
}).controller("createAggregatorController", function ($scope, $state, $stateParams, $location, aggregatorRestService, aggregatorService) {


    aggregatorService.initCreateScope($scope, $stateParams.appId);


    $scope.selected = function () {
        $scope.selectedType = true;
        aggregatorRestService.getEmpty($scope.type,
            function (returnedData) {
                $scope.aggregatorData = returnedData.data;
            }
        );
    };

    $scope.submit = function () {
        aggregatorService.fillAggregatorData($scope);
        aggregatorRestService.save($scope.aggregatorData,
            function (returnedData) {
                $location.path("/application/" + $scope.appId + "/aggregator");
            }
        );
    };
});