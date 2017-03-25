angular.module("buginator.createAggregatorController", []).config(function ($stateProvider) {
    $stateProvider.state("createAggregator", {
        url: "/application/:appId/aggregators/create",
        templateUrl: "html/aggregator/createAggregator.html",
        controller: "createAggregatorController"
    });
}).controller("createAggregatorController", function ($scope, $state, $stateParams, $location, aggregatorRestService, aggregatorWizardService) {

    $scope.appId = $stateParams.appId;

    $scope.service = aggregatorWizardService;

    $scope.forms = {};

    $scope.selectedType = false;

    $scope.availableTypes = ["Email"];


    $scope.selected = function () {
        $scope.selectedType = true;
        aggregatorRestService.getEmpty($scope.type,
            function (returnedData) {
                $scope.aggregatorData = returnedData.data;
            }
        );
    };

    $scope.submit = function () {
        $scope.aggregatorData.aggregator.application = {};
        $scope.aggregatorData.aggregator.application.id = $scope.appId;
        $scope.aggregatorData.aggregator.aggregatorClass = $scope.type + "Aggregator";
        aggregatorRestService.save($scope.aggregatorData,
            function (returnedData) {
                $location.path("/application/" + $scope.appId + "/aggregator");
            }
        );
    };
});