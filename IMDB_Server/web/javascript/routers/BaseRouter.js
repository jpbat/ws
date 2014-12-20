define(
    function(require) {

        var _ = require('underscore');
        var Backbone = require('backbone');
        var $ = require('jquery');

        var BaseRouter = Backbone.Router.extend({
            routes: {
                '': 'index',
                'index': 'index',
                'movie/:id': 'movie',
                'person/:id': 'person',
                'search': 'search'
            },

            index: function () {
                var self = this;

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                if(!this.ViewIndex){
                    var viewClass = require('views/main');
                    var collectionClass = require('collections/movies');

                    this.ViewIndex = new viewClass({collection:new collectionClass()});
                    this.ViewIndex.collection.url= "http://localhost:8080/rest/Movies/GetRecent/";
                }

                this.ViewIndex.collection.fetch({
                    reset: true, type: 'GET',
                    success: function () {
                        $("#BackBoneContainer").html(self.ViewIndex.$el.html());
                        console.log("success");
                    },
                    error: function () {
                        //TODO ADICIONAR UMA ERROR VIEW
                        console.log("fail");
                        window.location.hash = '';
                    }
                });
            },

            movie: function (id) {
                var self = this;

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                if(!this.ViewMovie){
                    var viewClass = require('views/movie');
                    var modelClass = require('models/movie');

                    this.ViewMovie = new viewClass({model:new modelClass()});

                }
                console.log("movie: " + id);
                this.ViewMovie.model.fetch({reset: true, type: 'GET', data: {id: id},
                    success: function () {
                        $("#BackBoneContainer").html(self.ViewMovie.$el.html());
                        console.log("success");
                    },
                    error: function () {
                        //TODO ADICIONAR UMA ERROR VIEW
                        console.log("fail");
                        window.location.hash = '';
                    }
                });
            },

            person: function (id) {
                var self = this;

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                if(!this.ViewPerson){
                    var viewClass = require('views/person');
                    var modelClass = require('models/person');

                    this.ViewPerson = new viewClass({model:new modelClass()});

                }
                console.log("Person: " + id);
                this.ViewPerson.model.fetch({reset: true, type: 'GET', data: {id: id},
                    success: function () {
                        $("#BackBoneContainer").html(self.ViewPerson.$el.html());
                        console.log("success");
                    },
                    error: function () {
                        //TODO ADICIONAR UMA ERROR VIEW
                        console.log("fail");
                        window.location.hash = '';
                    }
                });
            },
            search: function () {

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                var self = this;
                if(!this.ViewMovies){
                    var viewClass = require('views/movies');
                    var collectionClass = require('collections/movies');

                    this.ViewMovies = new viewClass({collection:new collectionClass()});
                }

                this.ViewMovies.collection.fetch({data:{limit:self.ViewMovies.limit,offset:self.ViewMovies.offset},
                    reset: true, type: 'GET',
                    success: function () {
                        self.ViewMovies.offset+=self.ViewMovies.limit;
                        $("#BackBoneContainer").html(self.ViewMovies.$el.html());
                        console.log("success");
                    },
                    error: function () {
                        //TODO ADICIONAR UMA ERROR VIEW
                        console.log("fail");
                        window.location.hash = '';
                    }
                });

            }
        });
        return BaseRouter;
    }
);