angular.module("buginator.chartServices", [])
    .factory("chartRestService", function ($http, exceptionHandler) {
        var service = {};

        service.baseObjectUrl = "/chart/";

        service.getLastWeekForApplication = function (appId, successFunction) {
            $http.get(this.baseObjectUrl + "applicationLastWeek/" + appId, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        return service;
    }).factory("chartService", function (chartRestService) {
        var service = {};

        service.generateChartDataForLastWeekApp = function ($scope, appId) {
            chartRestService.getLastWeekForApplication(appId,
                function (returnedData) {
                    var maxValue = Math.max.apply(Math,returnedData.data.data[0]);

                    $scope.errorsChart = {};

                    $scope.errorsChart.labels = returnedData.data.labels;
                    $scope.errorsChart.series = returnedData.data.series;
                    $scope.errorsChart.data = returnedData.data.data;

                    $scope.errorsChart.datasetOverride = [{yAxisID: 'y-axis-1'}];
                    $scope.errorsChart.options = {
                        legend: {
                            display: true
                        },
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        suggestedMin: 0,
                                        stepValue: 1,
                                        max: maxValue + 1
                                    },
                                    id: 'y-axis-1',
                                    type: 'linear',
                                    display: true,
                                    position: 'left'
                                }
                            ]
                        }
                    };
                }
            );
        };

        return service;
    });