angular.module("buginator.modifyRoleController", []).config(function ($stateProvider) {
    $stateProvider.state("modifyRole", {
        url: "/role/:id/edit",
        templateUrl: "html/role/role.html",
        controller: "modifyRoleController"
    });
}).controller("modifyRoleController", function ($scope, $stateParams, roleRestService, roleService, warningHandler) {

    $scope.id = $stateParams.id;

    $scope.editable = true;

    roleService.initScope($scope);

    roleRestService.get($scope.id,
        function (returnedData) {
            $scope.role = returnedData.data;
            roleRestService.getAllPerms(function (returnedData) {
                $scope.allPerms = returnedData.data;
                $scope.permsToAdd = roleService.getAllPermsNotInRole($scope.role.permissions, $scope.allPerms);
            });
        }
    );

    $scope.addPerm = function (permToAdd, index) {
        roleService.addRemovePermission($scope.selectedPermsToAdd, permToAdd, $scope.selectedAdd[index]);
    };

    $scope.removePerm = function (permToDelete, index) {
        roleService.addRemovePermission($scope.selectedPermsToDelete, permToDelete, $scope.selectedRemove[index]);
    };

    $scope.removeSelectedPerms = function () {
        warningHandler.handleWarning("REMOVE_PERMS_HEADER", "REMOVE_PERMS_DESCRIPTION", function () {
            var roleToUpdate = roleService.createRemovePermsCopy($scope);

            roleRestService.save(roleToUpdate, function (returnedData) {
                $scope.role = returnedData.data;
                $scope.permsToAdd = roleService.getAllPermsNotInRole($scope.role.permissions, $scope.allPerms);
                roleService.initScope($scope);
            });
        });
    };

    $scope.addSelectedPerms = function () {
        var roleToUpdate = roleService.createAddPermsCopy($scope);
        roleRestService.save(roleToUpdate, function (returnedData) {
            $scope.role = returnedData.data;
            $scope.permsToAdd = roleService.getAllPermsNotInRole($scope.role.permissions, $scope.allPerms);
            roleService.initScope($scope);
        });
    };
});