angular.module("buginator.modifyUserController", []).config(function ($stateProvider) {
    $stateProvider.state("modifyUser", {
        url: "/user/:email/edit",
        templateUrl: "html/user/user.html",
        controller: "modifyUserController"
    });
}).controller("modifyUserController", function ($scope, $state, $stateParams, userRestService, roleRestService) {

    $scope.editable = true;
    $scope.forms = {};

    userRestService.get($stateParams.email, function (returnedData) {
        $scope.editUser = returnedData.data;
    });

    roleRestService.getAll(function(returnedData){
       $scope.roles = returnedData.data;
    });

    $scope.submit = function() {
        userRestService.save($scope.editUser, function (returnedData) {
            $state.go("users")
        });
    }
});