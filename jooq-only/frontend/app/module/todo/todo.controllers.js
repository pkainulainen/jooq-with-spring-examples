'use strict';

angular.module('app.todo.controllers', [])
    .constant('paginationConfig', {
        pageSize: 5
    })
    .config(['$stateProvider',
        function ($stateProvider) {
            $stateProvider
                .state('todo', {
                    url: '/',
                    abstract: true,
                    template: '<ui-view/>'
                } )
                .state('todo.index', {
                    url: '',
                    controller: 'TodoListController',
                    templateUrl: 'frontend/partials/todo/todo-list.html',
                    resolve: {
                        todos: ['Todos', function(Todos) {
                            return Todos.query();
                        }]
                    }
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
                                return Todos.get($stateParams.todoId);
                            }
                            return null;
                        }]
                    }
                })
                .state('todo.search', {
                    url: 'todo/search/:searchTerm/page/:pageNumber/size/:pageSize',
                    controller: 'SearchResultController',
                    templateUrl: 'frontend/partials/search/search-results.html',
                    resolve: {
                        searchTerm: ['$stateParams', function($stateParams) {
                            return $stateParams.searchTerm;
                        }],
                        searchResults: ['Search', '$stateParams', function(Search, $stateParams) {
                            if ($stateParams.searchTerm) {
                                return Search.findBySearchTerm($stateParams.searchTerm, $stateParams.pageNumber - 1, $stateParams.pageSize);
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
                                return Todos.get($stateParams.todoId);
                            }
                            return null;
                        }]
                    }
                });
        }
    ])

    .controller('TodoListController', ['$scope', '$state', 'todos',
        function ($scope, $state, todos) {
            console.log('Rendering todo entry list page.');
            $scope.todos = todos;

            $scope.addTodo = function() {
                $state.go('todo.add');
            };
        }])
    .controller('AddTodoController', ['$scope', '$state', 'Todos',
        function($scope, $state, Todos) {
            console.log('Rendering add todo entry page.');
            $scope.todo = {};

            $scope.saveTodo = function() {
                if ($scope.todoForm.$valid) {
                    var onSuccess = function(added) {
                        $state.go('todo.view', {todoId: added.id}, { reload: true, inherit: true, notify: true });
                    };

                    Todos.save($scope.todo, onSuccess);
                }
            };
        }])
    .controller('DeleteTodoController', ['$scope', '$modalInstance', '$state', 'Todos', 'deletedTodo',
        function($scope, $modalInstance, $state, Todos, deletedTodo) {
            $scope.todo = deletedTodo;

            $scope.cancel = function() {
                $modalInstance.dismiss('cancel');
            };

            $scope.delete = function() {
                var onSuccess = function() {
                    $modalInstance.close();
                    $state.go('todo.index', {}, { reload: true, inherit: true, notify: true });
                };
                Todos.delete($scope.todo, onSuccess);
            };
        }])
    .controller('EditTodoController', ['$scope', '$state', 'updatedTodo', 'Todos',
        function($scope, $state, updatedTodo, Todos) {
            console.log('Rendering edit todo entry page.');
            $scope.todo = updatedTodo;

            $scope.saveTodo = function() {
                if ($scope.todoForm.$valid) {
                    var onSuccess = function(updated) {
                        $state.go('todo.view', {todoId: updated.id}, { reload: true, inherit: true, notify: true });
                    };

                    Todos.update($scope.todo, onSuccess);
                }
            };
        }])
    .controller('SearchController', ['$scope', '$state', 'paginationConfig',
        function ($scope, $state, paginationConfig) {

            var userWritingSearchTerm = false;
            var minimumSearchTermLength = 3;

            $scope.missingChars = minimumSearchTermLength;
            $scope.searchTerm = "";

            $scope.searchFieldBlur = function() {
                userWritingSearchTerm = false;
            };

            $scope.searchFieldFocus = function() {
                userWritingSearchTerm = true;
            };

            $scope.showMissingCharacterText = function() {
                if (userWritingSearchTerm) {
                    if ($scope.searchTerm.length < minimumSearchTermLength) {
                        return true;
                    }
                }

                return false;
            };

            $scope.search = function() {
                if ($scope.searchTerm.length < minimumSearchTermLength) {
                    $scope.missingChars = minimumSearchTermLength - $scope.searchTerm.length;
                }
                else {
                    $scope.missingChars = 0;
                    $state.go('todo.search',
                        {searchTerm: $scope.searchTerm, pageNumber: 1, pageSize: paginationConfig.pageSize},
                        {reload: true, inherit: true, notify: true}
                    );
                }
            };

        }])
    .controller('SearchResultController', ['$scope', '$state', 'paginationConfig', 'searchTerm', 'searchResults',
        function($scope, $state, paginationConfig, searchTerm, searchResults) {
            console.log('Rendering search results page.');
            $scope.todos = searchResults.content;

            $scope.pagination = {
                currentPage: searchResults.number + 1,
                itemsPerPage: paginationConfig.pageSize,
                totalItems: searchResults.totalElements
            };

            $scope.pageChanged = function(newPageNumber) {
                $state.go('todo.search',
                    {searchTerm: searchTerm, pageNumber: newPageNumber, pageSize: paginationConfig.pageSize},
                    {reload: true, inherit: true, notify: true}
                );
            };
        }])
    .controller('ViewTodoController', ['$scope', '$state', '$modal', 'viewedTodo',
        function($scope, $state, $modal, viewedTodo) {
            console.log('Rendering view todo entry page.');
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