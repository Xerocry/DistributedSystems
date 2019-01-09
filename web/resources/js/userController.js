/**
 * Created by kivi on 13.12.17.
 */

function UserService($resource) {
    return $resource('rest/user/:login', { login: '@login' });
}

function MessageService($resource) {
    return $resource('rest/user/:login/messages', { login: '@login' });
}

function UserProjectService($resource) {
    return $resource('rest/user/:login/projects?type=:type', {login: '@login', type: '@type'});
}

function UserManReportsService($resource) {
    return $resource('rest/user/:login/managed_reports', {login: '@login'});
}

function UserAssReportsService($resource) {
    return $resource('rest/user/:login/assigned_reports', {login: '@login'});
}

function UserManTicketsService($resource) {
    return $resource('rest/user/:login/managed_tickets', {login: '@login'});
}

function UserAssTicketsService($resource) {
    return $resource('rest/user/:login/assigned_tickets', {login: '@login'});
}

function UserController($scope, $routeParams,
                        UserService,
                        MessageService,
                        UserProjectService,
                        UserManReportsService,
                        UserAssReportsService,
                        UserManTicketsService,
                        UserAssTicketsService,
                        InfoShareService) {
    var url = function () {
        return {login:$routeParams.login};
    };
    var project_url = function (proj_type) {
        return {login:$routeParams.login, type: proj_type};
    };
    if (InfoShareService.getUser().login) {
        this.authlogin = InfoShareService.getUser().login;
        this.isMe = false;
    } else {
        this.authlogin = $routeParams.login;
        this.isMe = true;
    }

    this.instance = UserService.get(url());
    this.messages = MessageService.query(url());
    this.managedProjects = UserProjectService.query(project_url("manager"));
    this.leadedProjects = UserProjectService.query(project_url("teamlead"));
    this.developedProjects = UserProjectService.query(project_url("dev"));
    this.testedProjects = UserProjectService.query(project_url("tester"));

    this.managedReports = UserManReportsService.query(url());
    this.assignedReports = UserAssReportsService.query(url());
    this.managedTickets = UserManTicketsService.query(url());
    this.assignedTickets = UserAssTicketsService.query(url());

    this.updateProjects = function () {
        this.managedProjects = UserProjectService.query(project_url("manager"));
    };

    this.createProject = function () {
        if ($scope.projectName) {
            var project = new UserProjectService(project_url("manager"));
            project.name = $scope.projectName;
            project.$save(url(), function () {
                $scope.projectName = "";
                this.updateProjects();
            }.bind(this), function (error) {
                alert(error.data.message);
            });
        } else {
            alert("Enter the name of the project");
        }
    }
}

angular
    .module('app')
    .factory('UserService', UserService)
    .factory('MessageService', MessageService)
    .factory('UserProjectService', UserProjectService)
    .factory('UserManReportsService', UserManReportsService)
    .factory('UserAssReportsService', UserAssReportsService)
    .factory('UserManTicketsService', UserManTicketsService)
    .factory('UserAssTicketsService', UserAssTicketsService)
    .controller('UserController', UserController);