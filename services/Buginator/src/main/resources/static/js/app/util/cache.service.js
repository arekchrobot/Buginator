angular.module("buginator.cacheService", [])
    .factory('cacheService', function () {

        var service = {};
        service.shouldSynchronize = function (lastSynchronizationDate, cacheTimeMin) {
            if (lastSynchronizationDate != null) {
                var currentDate = new Date();

                var millisecondsDiff = currentDate - lastSynchronizationDate;
                var minutesDiff = millisecondsDiff / (1000 * 60);
                return minutesDiff >= cacheTimeMin;
            }
            return true;
        };

        return service;
    });