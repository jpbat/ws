define(
    function(require) {

        var _ = require('underscore');
        var Backbone = require('backbone');
        var $ = require('jquery');
        var niceScroll = require('nicescroll');

        var BaseRouter = Backbone.Router.extend({
            routes: {
                '': 'index',
                'index': 'index',
                'movie/:id': 'movie',
                'person/:id': 'person',
                'persons': 'persons',
                'movies': 'movies'
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
                        $("#BackBoneContainer").html(self.ViewIndex.el);
                        console.log("success");
                    },
                    error: function () {
                        var newElement = $('#alertContainer div').clone();
                        $(newElement).find("p").html("Fail to retrieve the Top Movie list!");
                        $('.alertContainer').append(newElement);
                        console.log("Fail to retrieve Top Movies");
                        self.ViewIndex.emptyRender();
                        $("#BackBoneContainer").html(self.ViewIndex.el);
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
                        $("#BackBoneContainer").html(self.ViewMovie.$el);
                        console.log("success");
                    },
                    error: function () {
                        var newElement = $('#alertContainer div').clone();
                        $(newElement).find("p").html("Fail to retrieve movie!");
                        $('.alertContainer').append(newElement);
                        console.log("Fail to retrieve Movie");
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
                        $("#BackBoneContainer").html(self.ViewPerson.$el);
                        console.log("success");
                    },
                    error: function () {
                        var newElement = $('#alertContainer div').clone();
                        $(newElement).find("p").html("Fail to retrieve the Person!");
                        $('.alertContainer').append(newElement);
                        console.log("Fail to retrieve Person");
                        window.location.hash = '';
                    }
                });
            },
            persons: function(){

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                var self = this;
                if(!this.ViewPersons){
                    var viewClass = require('views/persons');
                    var collectionClass = require('collections/persons');

                    this.ViewPersons = new viewClass({collection:new collectionClass()});

                    this.ViewPersons.collection.fetch({data:{limit:self.ViewPersons.limit,offset:self.ViewPersons.offset},
                        reset: true, type: 'GET',
                        success: function () {
                            self.ViewPersons.offset+=self.ViewPersons.limit;
                            $("#BackBoneContainer").html(self.ViewPersons.$el);

                            $("#PersonsCont").niceScroll();

                            console.log("success");
                        },
                        error: function () {
                            var newElement = $('#alertContainer div').clone();
                            $(newElement).find("p").html("Fail to retrieve the persons list!");
                            $('.alertContainer').append(newElement);
                            console.log("Fail to retrieve Persons ");
                            window.location.hash = '';
                        }
                    });


                }else{
                    $("#BackBoneContainer").html(self.ViewPersons.$el);

                    $("#PersonsCont").niceScroll();
                }



            },
            movies: function () {

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                var self = this;
                if(!this.ViewMovies){
                    var viewClass = require('views/movies');
                    var collectionClass = require('collections/movies');

                    this.ViewMovies = new viewClass({collection:new collectionClass()});
                }

                if(!this.ViewGenres){
                    var viewClass = require('views/genres');
                    var collectionClass = require('collections/genres');

                    this.ViewGenres = new viewClass({collection:new collectionClass()});

                    this.ViewGenres.collection.fetch({reset: true, type: 'GET'});
                    this.ViewMovies.collection.fetch({data:{limit:self.ViewMovies.limit,offset:self.ViewMovies.offset,genres:self.ViewMovies.genres.join('|')},
                        reset: true, type: 'GET',
                        success: function () {
                            self.ViewMovies.offset+=self.ViewMovies.limit;
                            $("#BackBoneContainer").html(self.ViewMovies.$el);
                            $("#BackBoneContainer").append(self.ViewGenres.$el);

                            $("#MoviesCont").niceScroll();

                            console.log("success");
                        },
                        error: function () {
                            var newElement = $('#alertContainer div').clone();
                            $(newElement).find("p").html("Fail to retrieve the movie list!");
                            $('.alertContainer').append(newElement);
                            console.log("Fail to retrieve Movies");
                            window.location.hash = '';
                        }
                    });

                    this.ViewGenres.on('UpdateMovies', this.ViewMovies.ResetCollection,  this);

                }else{
                    $("#BackBoneContainer").html(self.ViewMovies.$el);
                    $("#BackBoneContainer").append(self.ViewGenres.$el);
                    $("#MoviesCont").niceScroll();
                }


            }
        });
        return BaseRouter;
    }
);