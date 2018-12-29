angular.module("buginator.editAggregatorController", []).config(function ($stateProvider) {
    $stateProvider.state("editAggregator", {
        url: "/application/:appId/aggregator/:id/edit",
        templateUrl: "html/aggregator/aggregator.html",
        controller: "editAggregatorController"
    });
}).controller("editAggregatorController", function ($scope, $state, $stateParams, $location, aggregatorRestService, aggregatorWizardService) {

    $scope.appId = $stateParams.appId;

    $scope.id = $stateParams.id;

    $scope.service = aggregatorWizardService;

    $scope.editable = true;

    $scope.forms = {};

    aggregatorRestService.get($scope.id,
        function (returnedData) {
            $scope.aggregatorData = returnedData.data;
        }
    );

    $scope.submit = function () {
        aggregatorRestService.update($scope.aggregatorData,
            function (returnedData) {
                $location.path("/application/"+ $scope.appId + "/aggregator/" + $scope.id);
            }
        );
    };
});