angular.module("buginator.detailsErrorController", []).config(function ($stateProvider) {
    $stateProvider.state("detailsError", {
        url: "/application/:appId/error/:id",
        templateUrl: "html/application/error/error.html",
        controller: "detailsErrorController"
    });
}).controller("detailsErrorController", function ($scope, $state, $stateParams, exceptionHandler, errorRestService) {

    $scope.appId = $stateParams.appId;

    errorRestService.get($stateParams.id,
        function (returnedData) {
            $scope.error = returnedData.data.error;
            $scope.errorStackTrace = returnedData.data.errorStackTrace;
        }
    );

    $scope.changeStatus = function (newStatus) {
        var oldStatus = $scope.error.status;
        $scope.error.status = newStatus;
        errorRestService.save($scope.error,
            function (returnedData) {
                $scope.error = returnedData.data;
            }, function (returnedData) {
                $scope.error.status = oldStatus;
                exceptionHandler.handleRestError(returnedData);
            }
        );
    }

});