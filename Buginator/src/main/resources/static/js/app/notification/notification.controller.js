angular.module("buginator.notificationController", [])
    .controller("notificationController", function ($rootScope, $scope, $state, notificationWebSocketService, notificationRestService) {

        $rootScope.$on('establishNotificationWebSocket', function () {
            notificationWebSocketService.initialize($rootScope.user.token);
        });

        notificationWebSocketService.receive().then(null, null, function (data) {
            $scope.notifications = data;
        });

        $scope.removeNotification = function (notificationIndex, notification) {

            notificationRestService.markNotificationSeen(notification.id,
                function () {
                    $scope.notifications.splice(notificationIndex, 1);
                }
            );
        };

        $scope.removeAllNotifications = function () {

            notificationRestService.markAllNotificationsSeen($scope.notifications,
                function () {
                    $scope.notifications = [];
                }
            );
        }
    });