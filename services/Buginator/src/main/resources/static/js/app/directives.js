angular.module("buginator.directives", [])
    .directive('ngRedirectTo', function($state) {
        var directive = {};

        directive.restrict = 'A'; /* restrict this directive to elements */

        directive.link =function(scope, element, attributes) {
            element.bind('click', function (event) {
                //assign ng-Redirect-To attribute value to location
                $state.go(attributes.ngRedirectTo);
            });
        };

        return directive;
    })
    .directive('ngRedirectToUrl', function($window, $state) {
        var directive = {};

        directive.restrict = 'A'; /* restrict this directive to elements */

        directive.link =function(scope, element, attributes) {
            element.bind('click', function (event) {
                //assign ng-Redirect-To attribute value to location
                var url = $window.location.href;
                var urlRedirect = url.substring(0, url.indexOf('#')+1);
                $window.location.href = urlRedirect + attributes.ngRedirectToUrl;
            });
        };

        return directive;
    }).directive('pwCheck', function () {
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {
                var firstPassword = '#' + attrs.pwCheck;
                elem.add(firstPassword).on('keyup', function () {
                    scope.$apply(function () {
                        var v = elem.val()===$(firstPassword).val();
                        ctrl.$setValidity('pwmatch', v);
                    });
                });
            }
        }
    });