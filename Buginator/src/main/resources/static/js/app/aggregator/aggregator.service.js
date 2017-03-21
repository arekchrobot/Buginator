angular.module("buginator.aggregatorServices", [])
    .factory("aggregatorRestService", function ($http, exceptionHandler) {
        var service = {};

        service.baseObjectUrl = "/aggregator/";

        service.get = function (id, successFunction) {
            $http.get(this.baseObjectUrl + id, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.getAllByApplication = function (id, successFunction) {
            $http.get(this.baseObjectUrl + "byApplication/" + id, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.getEmpty = function (type, successFunction) {
            $http.get(this.baseObjectUrl + "empty/" + type, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.save = function (data, successFunction) {
            $http.post(this.baseObjectUrl, data)
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.update = function (data, successFunction) {
            $http.put(this.baseObjectUrl, data)
                .then(successFunction, exceptionHandler.handleRestError);
        };

        service.delete = function (id, successFunction) {
            $http.delete(this.baseObjectUrl + id, {})
                .then(successFunction, exceptionHandler.handleRestError);
        };

        return service;
    }).factory("aggregatorWizardService", function(utilService, $translate) {
        var service = {};

        service.disabledFields = [
            "aggregatorClass",
            "application",
            "company",
            "new",
            "id",
            "version"
        ];

        service.hiddenFields = {
          "Email": ["login", "password"]
        };

        service.specialTypes = {
            "errorSeverity": ["WARNING", "ERROR", "CRITICAL"],
            "language": $translate.getAvailableLanguageKeys()
        };

        service.getType = function(field, key) {
            if(service.specialTypes[key] !== null && service.specialTypes[key] !== undefined) {
                return "selectBox";
            }
            return utilService.checkType(field);
        };

        service.checkFieldAllowed = function(field, key, type) {
            if (this.disabledFields.indexOf(key) !== -1) {
                return false;
            }

            if(service.hiddenFields[type] !== null && service.hiddenFields[type] !== undefined) {
                if(service.hiddenFields[type].indexOf(key) !== -1) {
                    return false;
                }
            }

            if(field === null || field === undefined) {
                return true;
            }

            return !Array.isArray(field);
        };

        service.getHeaderValue = function(header) {
            var translateKey = "AGGREGATOR_" + utilService.getTranslateKey(header);

            var translation = $translate.instant(translateKey);

            if(translation == translateKey) {
                return utilService.normalizeHeader(header);
            }

            return translation;
        };

        return service;
});