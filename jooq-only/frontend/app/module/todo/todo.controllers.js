'use strict';

angular.module('app.todo.controllers', [])
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo', {
                    url: '/',
                    controller: 'TodoListController',
                    templateUrl: 'frontend/partials/todo/todo-list.html'
                })
                .state('todo.add', {
                    url: 'todo/add',
                    controller: 'AddTodoController',
                    templateUrl: 'frontend/partials/todo/add-todo.html'
                })
                .state('todo.edit', {
                    url: 'todo/:todoId/edit',
                    controller: 'EditTodoController',
                    templateUrl: 'frontend/partials/todo/edit-todo.html',
                    resolve: {
                        updatedTodo: ['Todos', '$stateParams', function(Todos, $stateParams) {
                            if ($stateParams.todoId) {
                                return Todos.get({id: $stateParams.todoId}).$promise;
                            }
                            return null;
                        }]
                    }
                })
                .state('todo.view', {
                    url: 'todo/:todoId',
                    controller: 'ViewTodoController',
                    templateUrl: 'frontend/partials/todo/view-todo.html',
                    resolve: {
                        viewedTodo: ['Todos', '$stateParams', function(Todos, $stateParams) {
                            if ($stateParams.todoId) {
                                return Todos.get({id: $stateParams.todoId}).$promise;
                            }
                            return null;
                        }]
                    }
                });
        }
    ])

    .controller('TodoListController', ['$scope', '$state', 'Todos',
        function ($scope, $state, Todos) {
            $scope.todos = Todos.query();

            $scope.addTodo = function() {
                $state.go('todo.add');
            };
        }])
    .controller('AddTodoController', ['$scope', '$state', 'Todos', 'NotificationService',
        function($scope, $state, Todos, NotificationService) {
            $scope.todo = {};

            $scope.saveTodo = function() {
                if ($scope.todoForm.$valid) {
                    var onSuccess = function() {
                        NotificationService.flashMessage('todo.notifications.add.success', 'success');
                        $state.go('todo.view', {todoId: $scope.todo.id}, { reload: true, inherit: true, notify: true });
                    };

                    Todos.save($scope.todo, onSuccess);
                }
            };
        }])
    .controller('DeleteTodoController', ['$scope', '$modalInstance', '$state', 'Todos', 'NotificationService', 'deletedTodo',
        function($scope, $modalInstance, $state, Todos, NotificationService, deletedTodo) {
            $scope.todo = deletedTodo;

            $scope.cancel = function() {
                $modalInstance.dismiss('cancel');
            }

            $scope.delete = function() {
                var onSuccess = function() {
                    $modalInstance.close();
                    NotificationService.flashMessage('todo.notifications.delete.success', 'success');
                    $state.go('todo', {}, { reload: true, inherit: true, notify: true });
                }
                Todos.delete($scope.todo, onSuccess);
            }
        }])
    .controller('EditTodoController', ['$scope', '$state', 'updatedTodo', 'Todos', 'NotificationService',
        function($scope, $state, updatedTodo, Todos, NotificationService) {
            $scope.todo = updatedTodo;

            $scope.saveTodo = function() {
                if ($scope.todoForm.$valid) {
                    var onSuccess = function() {
                        NotificationService.flashMessage('todo.notifications.update.success', 'success');
                        $state.go('todo.view', {todoId: $scope.todo.id}, { reload: true, inherit: true, notify: true });
                    };

                    Todos.update($scope.todo, onSuccess);
                }
            };
        }])
    .controller('ViewTodoController', ['$scope', '$state', '$modal', 'viewedTodo',
        function($scope, $state, $modal, viewedTodo) {
            $scope.todo = viewedTodo;

            $scope.showEditPage = function() {
                $state.go("todo.edit", {todoId: $scope.todo.id}, { reload: true, inherit: true, notify: true });
            };

            $scope.showDeleteDialog = function() {
                $modal.open({
                    templateUrl: 'frontend/partials/todo/delete-todo-modal.html',
                    controller: 'DeleteTodoController',
                    resolve: {
                        deletedTodo: function () {
                            return $scope.todo;
                        }
                    }
                });
            };
        }]);