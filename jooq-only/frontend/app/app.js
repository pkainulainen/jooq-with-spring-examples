'use strict';

var App = angular.module('app', [
    'ngLocale',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'pascalprecht.translate',
    'ui.bootstrap',
    'ui.router',
    'ui.utils',
    'angular-growl',
    'angularMoment',

    //Common
    'app.common.config', 'app.common.directives', 'app.common.services',

    //Todo
    'app.todo.controllers', 'app.todo.services'

]);

