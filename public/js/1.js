var app = angular.module('myApp', ['ngRoute']);
app.controller('MyController', function($scope, $mdToast, $rootScope) {
    $rootScope.logout = function() {
        // window.location.replace("http://localhost:8080/login");
        // return false;

    }
})


app.config(function($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    let domain = "http://localhost:8080/admin/partial/"
    $routeProvider

        .when('/', {
            templateUrl: domain + 'user.html',
            controller: 'listUsersController'
        })
        .when('/listusers', {
            templateUrl: domain + 'user.html',
            controller: 'listUsersController'
        })
        .when('/adduser', {
            templateUrl: domain + 'add-user.html',
            controller: 'addUserController'
        })

    .otherwise({ redirectTo: '/' })
})

// trang listuser
app.controller('listUsersController', function($scope, $http, $rootScope) {
    $rootScope.title = "List Users";
    //lấy dữ liệu users
    var URLListUser = "http://localhost:8080/api/user?action=getuser";
    $http.get(URLListUser).then(function(response) {
        $scope.users = response.data.data;
    }, function(error) {
        console.log("error", error)
    })

    //"changeEdit()"
    $scope.changeEdit = function(item) { //item hay biến khác cũng được, không cần phải trùng tên với bên user
        item.show = !item.show;
    }

    $scope.saveDataEdit = function(item) { //item hay biến khác cũng được, không cần phải trùng tên với bên user
        item.show = !item.show;

        //lấy dữ liệu về
        var data = $.param({
            id: item.id,
            username: item.username,
            password: item.password,
            level: item.level
        });

        console.log(data);
        var config = {
            headers: {
                'content-type': 'application/x-www-form-urlencoded;charset=UTF-8'
            }
        }
        $http.post('http://localhost:8080/api/user?action=edit', data, config)
            .then(function(response) {
                console.log(response);
                showNotification("success", response.data.message);
            }, function(error) {
                console.log(error.data.message);
                showNotification("error", error.data.message);
            })
    }

    $scope.delete = function(item) {
        var data = $.param({
            id: item.id,
            username: item.username,
            password: item.password,
            level: item.level
        });
        var config = {
            headers: {
                'content-type': 'application/x-www-form-urlencoded;charset=UTF-8'
            }
        }
        $http.post('http://localhost:8080/api/user?action=delete', data, config)
            .then(function(response) {
                console.log(response);
                showNotification("success", response.data.message);
                window.location.replace("http://localhost:8080/admin");
            }, function(error) {
                console.log(error.data.message);
                showNotification("error", error.data.message);
            })
    }

})


// trang adduser
app.controller('addUserController', function($scope, $http, $rootScope) {
    $rootScope.title = "Add User"
        //Lấy thông tin người dùng nhập vào
        //dùng ng-model, lấy bằng cách sd $scope
    $scope.testthongbao = function() {
        if ($scope.username && $scope.password && $scope.level) {
            var data = $.param({
                username: $scope.username,
                password: $scope.password,
                level: $scope.level
            });

            console.log(data);
            var config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded;charset=UTF-8'
                }
            }
            $http.post('http://localhost:8080/api/user?action=add', data, config)
                .then(function(response) {
                    showNotification("success", response.data.message);
                    $scope.username = "";
                    $scope.password = "";
                    $scope.level = "";
                }, function(error) {
                    console.log(error.data.message);
                    showNotification("error", error.data.message);
                })
        } else {
            showNotification("error", "Thêm user thất bại");
        }
    }

})