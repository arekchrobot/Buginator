angular.module("buginator.aggregatorsController", []).config(function ($stateProvider) {
    $stateProvider.state("aggregators", {
        url: "/application/:appId/aggregator",
        templateUrl: "html/aggregator/aggregators.html",
        controller: "aggregatorsController"
    });
}).controller("aggregatorsController", function ($scope, $state, $stateParams, exceptionHandler, aggregatorRestService, warningHandler) {

    $scope.appId = $stateParams.appId;

    aggregatorRestService.getAllByApplication($scope.appId,
        function (returnedData) {
            $scope.aggregators = returnedData.data;
        }
    );

    $scope.removeItem = function (aggregatorData) {
        warningHandler.handleWarning("REMOVE_AGGREGATOR_HEADER", "REMOVE_AGGREGATOR_DESCRIPTION",
            function () {
                aggregatorRestService.delete(aggregatorData.aggregator.id,
                    function (returnedData) {
                        aggregatorRestService.getAllByApplication($scope.appId,
                            function (returnedData) {
                                $scope.aggregators = returnedData.data;
                            }
                        );
                    }
                )
            }
        );
    }
});