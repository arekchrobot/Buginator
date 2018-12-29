angular.module("buginator.createRoleController", []).config(function ($stateProvider) {
    $stateProvider.state("createRole", {
        url: "/roles/create",
        templateUrl: "html/role/createRole.html",
        controller: "createRoleController"
    });
}).controller("createRoleController", function ($scope, $state, roleRestService, roleService) {

    $scope.selectedPermsToAdd = [];
    $scope.selectedAdd = [];
    $scope.forms = {};

    $scope.role = {};

    roleRestService.getAllPerms(function (returnedData) {
        $scope.allPerms = returnedData.data;
    });

    $scope.addPerm = function (permToAdd, index) {
        roleService.addRemovePermission($scope.selectedPermsToAdd, permToAdd, $scope.selectedAdd[index]);
    };

    $scope.submit = function () {
        $scope.role.permissions = $scope.selectedPermsToAdd;
        roleRestService.save($scope.role, function (returnedData) {
            $state.go("roles");
        });
    };
});