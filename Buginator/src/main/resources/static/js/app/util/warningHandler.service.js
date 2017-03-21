angular.module('buginator.warningHandler', [])
    .factory('warningHandler', function ($translate, ngDialog) {
        var service = {};

        service.handleWarning = function (header, description, func) {
            var data = {};
            data.header = $translate.instant(header);
            data.description = $translate.instant(description);
            data.func = func;
            ngDialog.open({template: "html/warning/warning.html", data: data});
        };

        return service;
    });