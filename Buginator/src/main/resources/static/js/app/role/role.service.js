angular.module("buginator.roleServices", [])
    .factory("roleRestService", function ($http, baseRestService, exceptionHandler) {
        var service = angular.copy(baseRestService);

        service.baseObjectUrl = "/role/";

        service.getAllPerms = function (successFunction) {
            $http.get(this.baseObjectUrl + "perms/all", {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        return service;
    }).factory("roleService", function () {
        var service = {};

        service.getAllPermsNotInRole = function (rolePerms, allPerms) {
            var permsNotInRole = [];

            var lookup = {};
            for (var i = 0; i < rolePerms.length; i++) {
                lookup[rolePerms[i].id] = rolePerms[i];
            }

            for (var y = 0; y < allPerms.length; y++) {
                if (lookup[allPerms[y].id] == null || lookup[allPerms[y].id] == undefined) {
                    permsNotInRole.push(allPerms[y]);
                }
            }

            return permsNotInRole;
        };

        service.createCustomRolesBooleanArray = function (roles) {
            var customRoles = [];
            for (var i = 0; i < roles.length; i++) {
                if (roles[i].company) {
                    customRoles[i] = true;
                } else {
                    customRoles[i] = false;
                }
            }
            return customRoles;
        };

        service.addRemovePermission = function (collection, item, isAdd) {
            if (isAdd) {
                collection.push(item);
            } else {
                var itemIndex = collection.indexOf(item);
                if (itemIndex > -1) {
                    collection.splice(itemIndex, 1);
                }
            }
        };

        service.initScope = function($scope) {
            $scope.selectedPermsToAdd = [];
            $scope.selectedPermsToDelete = [];
            $scope.selectedRemove = [];
            $scope.selectedAdd = [];
        };

        service.createRemovePermsCopy = function($scope) {
            var roleToUpdate = angular.copy($scope.role);

            var lookup = {};
            for (var i = 0; i < roleToUpdate.permissions.length; i++) {
                lookup[roleToUpdate.permissions[i].id] = i;
            }

            for (var y = 0; y < $scope.selectedPermsToDelete.length; y++) {
                if (lookup[$scope.selectedPermsToDelete[y].id] !== null || lookup[$scope.selectedPermsToDelete[y].id] !== undefined) {
                    roleToUpdate.permissions.splice(lookup[$scope.selectedPermsToDelete[y].id], 1);
                }
            }

            return roleToUpdate;
        };

        service.createAddPermsCopy = function($scope) {
            var roleToUpdate = angular.copy($scope.role);
            roleToUpdate.permissions = roleToUpdate.permissions.concat($scope.selectedPermsToAdd);

            return roleToUpdate;
        };

        return service;
    });