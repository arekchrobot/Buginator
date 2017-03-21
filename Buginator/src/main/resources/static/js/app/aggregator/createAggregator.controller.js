angular.module("buginator.createAggregatorController", []).config(function ($stateProvider) {
    $stateProvider.state("createAggregator", {
        url: "/application/:appId/aggregators/create",
        templateUrl: "html/aggregator/createAggregator.html",
        controller: "createAggregatorController"
    });
}).controller("createAggregatorController", function ($scope, $state, $stateParams, $location, aggregatorRestService,
                                                      applicationRestService, aggregatorWizardService) {

    $scope.appId = $stateParams.appId;

    $scope.service = aggregatorWizardService;

    $scope.forms = {};

    $scope.selectedType = false;

    $scope.availableTypes = ["Email"];

    applicationRestService.get($scope.appId,
        function (returnedData) {
            $scope.application = returnedData.data;
        }
    );

    $scope.selected = function () {
        $scope.selectedType = true;
        aggregatorRestService.getEmpty($scope.type,
            function (returnedData) {
                $scope.aggregatorData = returnedData.data;
            }
        );
    };

    $scope.submit = function () {
        $scope.aggregatorData.aggregator.application = $scope.application;
        $scope.aggregatorData.aggregator.company = $scope.application.company;
        $scope.aggregatorData.aggregator.aggregatorClass = $scope.type + "Aggregator";
        aggregatorRestService.save($scope.aggregatorData,
            function (returnedData) {
                $location.path("/application/" + $scope.appId + "/aggregator");
            }
        );
    };
});