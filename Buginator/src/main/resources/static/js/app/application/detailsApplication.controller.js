angular.module("buginator.detailsApplicationController", []).config(function ($stateProvider) {
    $stateProvider.state("detailsApplication", {
        url: "/application/:id",
        templateUrl: "html/application/application.html",
        controller: "detailsApplicationController"
    });
}).controller("detailsApplicationController", function ($scope, $state, $stateParams, applicationRestService, exceptionHandler, applicationService) {

    applicationService.initDetailsAppScope($scope);

    applicationRestService.get($stateParams.id,
        function (returnedData) {
            $scope.application = returnedData.data;
        }, exceptionHandler.handleRestError
    );

    $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
    $scope.series = ['Series A',
        //'Series B'
    ];
    $scope.data = [
        [65, 59, 80, 81, 56, 55, 40],
        //[28, 48, 40, 19, 86, 27, 90]
    ];
    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };
    $scope.datasetOverride = [{ yAxisID: 'y-axis-1' },
        //{ yAxisID: 'y-axis-2' }
    ];
    $scope.options = {
        legend: {
          display: true
        },
        scales: {
            yAxes: [
                {
                    id: 'y-axis-1',
                    type: 'linear',
                    display: true,
                    position: 'left'
                },
                //{
                //    id: 'y-axis-2',
                //    type: 'linear',
                //    display: true,
                //    position: 'right'
                //}
            ]
        }
    };
});