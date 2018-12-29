angular.module("buginator.dashboardController", []).config(function ($stateProvider) {
    $stateProvider.state("dashboard", {
        url: "/dashboard",
        templateUrl: "html/dashboard/dashboard.html"
    });
}).controller("dashboardController", function ($rootScope, $scope, $state) {

});