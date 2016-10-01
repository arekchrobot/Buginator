angular.module("buginator.filters", [])
.filter('isNotEmpty', function () {
    return function (obj) {
        return obj != undefined || obj != null;
    };
});