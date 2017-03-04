angular.module("buginator.notificationController", [])
    .controller("notificationController", function ($rootScope, $scope, $state, notificationWebSocketService) {

        $rootScope.$on('establishNotificationWebSocket', function () {
            notificationWebSocketService.initialize($rootScope.user.token);
        });

        notificationWebSocketService.receive().then(null, null, function (data) {
            $scope.notifications = data;
        });

        $scope.removeNotification = function(notificationIndex, notification) {

            $scope.notifications.splice(notificationIndex, 1);
        };
    });