angular.module("buginator.rolesController", []).config(function ($stateProvider) {
    $stateProvider.state("roles", {
        url: "/role",
        templateUrl: "html/role/roles.html",
        controller: "rolesController"
    });
}).controller("rolesController", function ($scope, $state, roleRestService, roleService, warningHandler) {

    roleRestService.getAll(
        function (returnedData) {
            $scope.roles = returnedData.data;
            $scope.customRoles = roleService.createCustomRolesBooleanArray($scope.roles);
        }
    );

    $scope.remove = function (role) {
        warningHandler.handleWarning("REMOVE_ROLE_HEADER", "REMOVE_ROLE_DESCRIPTION", function () {
            roleRestService.delete(role.id, function(returnedData){
                var index = $scope.roles.indexOf(role);
                if (index > -1) {
                    $scope.roles.splice(index, 1);
                }
            });
        });
    };
});