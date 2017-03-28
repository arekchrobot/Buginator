angular.module("buginator.detailsRoleController", []).config(function ($stateProvider) {
    $stateProvider.state("detailsRole", {
        url: "/role/:id",
        templateUrl: "html/role/role.html",
        controller: "detailsRoleController"
    });
}).controller("detailsRoleController", function ($scope, $stateParams, roleRestService, roleService) {

    $scope.id = $stateParams.id;

    $scope.editable = false;

    roleRestService.get($scope.id,
        function(returnedData){
            $scope.role = returnedData.data;
        }
    );
});