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
            hideSpinner:function(){
                console.log("Hide Spinner");
            },
            addError:function(err){
                var newElement = $('#alertContainer div').clone();
                $(newElement).find("p").html(err);
                $('.alertContainer').append(newElement);
            },
            reRoute:function(err){
                if(err){
                    this.addError(err);
                }
                console.log("Rerouting");
                window.location.hash = '';
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

                this.ViewIndex.resetCollection();

                $("#BackBoneContainer").html(self.ViewIndex.el);


                $("#homeButton").attr("href", "/");
                $("#backButton").attr("href", "javascript:history.back()");
                $("#moviesButton").attr("href", "#movies");
                $("#personsButton").attr("href", "#persons");
            },

            movie: function (id) {
                var self = this;

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                if(!this.ViewMovie){
                    var viewClass = require('views/movie');
                    var modelClass = require('models/movie');

                    this.ViewMovie = new viewClass({model:new modelClass()});

                    this.ViewMovie.on('FetchSuccess', this.hideSpinner ,  this);
                    this.ViewMovie.on('FetchFail', this.reRoute ,  this);

                }
                this.ViewMovie.updateModel(id);

                $("#BackBoneContainer").html(self.ViewMovie.$el);

                $("#homeButton").attr("href", "/");
                $("#backButton").attr("href", "javascript:history.back()");
                $("#moviesButton").attr("href", "#movies");
                $("#personsButton").attr("href", "#persons");
            },
            person: function (id) {
                var self = this;

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                if(!this.ViewPerson){

                    var viewClass = require('views/person');
                    var modelClass = require('models/person');

                    this.ViewPerson = new viewClass({model:new modelClass()});

                }

                this.ViewMovie.updateModel(id);

                $("#BackBoneContainer").html(self.ViewPerson.$el);

                $("#homeButton").attr("href", "/");
                $("#backButton").attr("href", "javascript:history.back()");
                $("#moviesButton").attr("href", "#movies");
                $("#personsButton").attr("href", "#persons");
            },

            persons: function(){

                $("#BackBoneContainer").html($("#SpinnerContainer").html());
                $("#PersonsCont").niceScroll();
                var self = this;
                if(!this.ViewPersons){
                    var viewClass = require('views/persons');
                    var collectionClass = require('collections/persons');

                    this.ViewPersons = new viewClass({collection:new collectionClass()});

                    this.ViewPersons.resetCollection();
                }

                $("#BackBoneContainer").html(self.ViewPersons.$el);

                $("#homeButton").attr("href", "/");
                $("#backButton").attr("href", "javascript:history.back()");
                $("#moviesButton").attr("href", "#movies");
                $("#personsButton").removeAttr("href");

            },
            movies: function () {

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                var self = this;
                if(!this.ViewMovies){
                    var viewClass = require('views/movies');
                    var collectionClass = require('collections/movies');

                    this.ViewMovies = new viewClass({collection:new collectionClass()});
                    this.ViewMovies.resetCollection([]);
                }

                if(!this.ViewGenres){
                    var viewClass = require('views/genres');
                    var collectionClass = require('collections/genres');

                    this.ViewGenres = new viewClass({collection:new collectionClass()});
                    this.ViewGenres.resetCollection();

                    this.ViewGenres.on('UpdateMovies', this.ViewMovies.resetCollection,  this.ViewMovies);
                }

                $("#BackBoneContainer").html(self.ViewMovies.$el);
                $("#BackBoneContainer").append(self.ViewGenres.$el);
                self.ViewMovies.delegateEvents();
                self.ViewGenres.delegateEvents();
                //$("#MoviesCont").niceScroll();

                $("#homeButton").attr("href", "/");
                $("#backButton").attr("href", "javascript:history.back()");
                $("#moviesButton").removeAttr("href");
                $("#personsButton").attr("href", "#persons");

            }
        });
        return BaseRouter;
    }
);