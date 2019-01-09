/**
 * Created by kivi on 18.12.17.
 */

function MilestoneService($resource) {
    return $resource('rest/milestone/:id', { id: '@id' });
}

function MilestonePermService($resource) {
    return $resource('rest/milestone/:id/permissions?user=:login', {id: '@id', login: '@login'});
}

function MilestoneProjectService($resource) {
    return $resource('rest/milestone/:id/project', { id: '@id' });
}

function MilestoneTicketService($resource) {
    return $resource('rest/milestone/:id/tickets?user=:login', { id: '@id', login:'@login' });
}

function MilestoneController($routeParams, $scope, $http,
                             InfoShareService,
                             MilestoneService,
                             MilestonePermService,
                             MilestoneProjectService,
                             MilestoneTicketService) {
    function url() {
        return {id: $routeParams.id};
    }

    function url_with_login(login) {
        return {id: $routeParams.id, login:login}
    }

    this.user = InfoShareService.getUser();
    this.permissions = MilestonePermService.get(url_with_login(this.user.login));
    this.project = MilestoneProjectService.get(url());
    this.instance = MilestoneService.get(url());
    this.tickets = MilestoneTicketService.query(url());

    this.addTicket = function () {
        if (isEmpty($scope.ticketTask)) {
            alert("Enter new ticket task");
        } else {
            var ticket = new MilestoneTicketService();
            ticket.task = $scope.ticketTask;
            ticket.$save(url_with_login(this.user.login), function () {
                $scope.ticketTask = "";
                this.updateTickets();
            }.bind(this), function (error) {
                alert(error.data.message);
            });
        }
    };

    this.changeStatus = function (status) {
        $http.put('rest/milestone/' + this.instance.id + '?user=' + this.user.login, status)
            .then(function () {
                this.updateInstance();
            }.bind(this), function (error) {
                alert(error.data.message);
            });
    };

    this.updateInstance = function () {
        this.instance = MilestoneService.get(url());
    };

    this.updateTickets = function () {
        this.tickets = MilestoneTicketService.query(url());
    };
}

app
    .factory('MilestoneService', MilestoneService)
    .factory('MilestonePermService', MilestonePermService)
    .factory('MilestoneProjectService', MilestoneProjectService)
    .factory('MilestoneTicketService', MilestoneTicketService)
    .controller('MilestoneController', MilestoneController);