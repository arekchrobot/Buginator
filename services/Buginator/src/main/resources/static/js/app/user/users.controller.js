angular.module("buginator.usersController", []).config(function ($stateProvider) {
    $stateProvider.state("users", {
        url: "/user",
        templateUrl: "html/user/users.html",
        controller: "usersController"
    });
}).controller("usersController", function ($scope, userRestService, warningHandler) {

    userRestService.getAll(
        function (returnedData) {
            $scope.users = returnedData.data;
        }
    );

    $scope.activateDeactivate = function (displayedUser) {
        var description = displayedUser.active ? "DEACTIVATE_DESCRIPTION" : "ACTIVATE_DESCRIPTION";
        warningHandler.handleWarning("USER_ACTIVATE_DEACTIVATE_HEADER", description, function () {
            userRestService.put(displayedUser.email, !displayedUser.active,
                function (returnedData) {
                    displayedUser.active = !displayedUser.active;
                }
            );
        });
    }
});