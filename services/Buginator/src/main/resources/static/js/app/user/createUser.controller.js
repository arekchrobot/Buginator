angular.module("buginator.createUserController", []).config(function ($stateProvider) {
    $stateProvider.state("createUser", {
        url: "/user/create",
        templateUrl: "html/user/user.html",
        controller: "createUserController"
    });
}).controller("createUserController", function ($scope, $state, userRestService, roleRestService) {

    $scope.editable = false;
    $scope.forms = {};
    $scope.createUser = {};

    roleRestService.getAll(function(returnedData){
        $scope.roles = returnedData.data;
    });

    $scope.submit = function() {
        console.log($scope.createUser);
        userRestService.save($scope.createUser, function (returnedData) {
            $state.go("users")
        });
    }
});