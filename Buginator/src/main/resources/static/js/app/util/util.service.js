angular.module("buginator.utilService", [])
    .factory("utilService", function () {
        var service = {};

        service.normalizeHeader = function (header) {
            return header.replace(/([A-Z])/g, ' $1').replace(/^./, function (str) {
                return str.toUpperCase();
            });
        };

        service.getTranslateKey = function(header) {
            return header.replace(/([A-Z])/g, '_$1').toUpperCase();
        };

        service.isDate = function (predicate) {
            if(this.isNumber(predicate)) {
                return false;
            }
            return (new Date(predicate) !== "Invalid Date") && !isNaN(new Date(predicate));
        };

        service.isNumber = function (predicate) {
            return typeof predicate === 'number';
        };

        service.isNestedObject = function (predicate) {
            return typeof predicate === 'object';
        };

        service.isString = function(predicate) {
            return typeof predicate === 'string';
        };

        service.isBoolean = function (predicate) {
            if (predicate == "true" || predicate == "false") {
                return true;
            }
            return typeof predicate === 'boolean';
        };

        service.checkType = function(predicate) {
            if (this.isNestedObject(predicate)) {
                return "object";
            } else if(this.isBoolean(predicate)) {
                return "boolean";
            } else if (this.isNumber(predicate)) {
                return "number";
            } else if (this.isDate(predicate)) {
                return "date";
            } else {
                return "string";
            }
        };

        return service;
    });