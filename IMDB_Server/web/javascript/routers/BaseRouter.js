define(
    function(require) {

        var _ = require('underscore');
        var Backbone = require('backbone');
        var $ = require('jquery');


        return Backbone.Router.extend({
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

                if (!this.ViewIndex) {
                    var viewClass = require('views/main');
                    var collectionClass = require('collections/movies');

                    this.ViewIndex = new viewClass({collection: new collectionClass()});
                    this.ViewIndex.collection.url = "http://localhost:8080/rest/Movies/GetRecent/";
                }

                this.ViewIndex.resetCollection();

                $("#BackBoneContainer").html(self.ViewIndex.el);
                self.ViewIndex.delegateEvents();

                $("#homeButton").addClass("disabled");
                $("#backButton").removeClass("disabled");
                $("#moviesButton").removeClass("disabled");
                $("#personsButton").removeClass("disabled");
            },

            movie: function (id) {
                var self = this;

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                if (!this.ViewMovie) {
                    var viewClass = require('views/movie');
                    var modelClass = require('models/movie');

                    this.ViewMovie = new viewClass({model: new modelClass()});

                    this.ViewMovie.on('FetchStart', this.showSpinner, this);
                    this.ViewMovie.on('FetchSuccess', this.hideSpinner, this);
                    this.ViewMovie.on('FetchFail', this.reRoute, this);

                }
                this.ViewMovie.updateModel(id);

                $("#BackBoneContainer").html(self.ViewMovie.$el);

                $("#homeButton").removeClass("disabled");
                $("#backButton").removeClass("disabled");
                $("#moviesButton").removeClass("disabled");
                $("#personsButton").removeClass("disabled");
            },
            person: function (id) {
                var self = this;

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                if (!this.ViewPerson) {

                    var viewClass = require('views/person');
                    var modelClass = require('models/person');

                    this.ViewPerson = new viewClass({model: new modelClass()});

                    this.ViewPerson.on('FetchStart', this.showSpinner, this);
                    this.ViewPerson.on('FetchSuccess', this.hideSpinner, this);
                    this.ViewPerson.on('FetchFail', this.reRoute, this);

                }

                this.ViewPerson.updateModel(id);

                $("#BackBoneContainer").html(self.ViewPerson.$el);

                $("#homeButton").removeClass("disabled");
                $("#backButton").removeClass("disabled");
                $("#moviesButton").removeClass("disabled");
                $("#personsButton").removeClass("disabled");
            },

            persons: function () {

                $("#BackBoneContainer").html($("#SpinnerContainer").html());
                $("#PersonsCont").niceScroll();
                var self = this;
                if (!this.ViewPersons) {
                    var viewClass = require('views/persons');
                    var collectionClass = require('collections/persons');

                    this.ViewPersons = new viewClass({collection: new collectionClass()});

                    this.ViewPersons.on('FetchStart', this.showSpinner, this);
                    this.ViewPersons.on('FetchSuccess', this.hideSpinner, this);
                    this.ViewPersons.on('FetchFail', this.reRoute, this);

                    this.ViewPersons.resetCollection();
                }

                $("#BackBoneContainer").html(self.ViewPersons.$el);
                self.ViewPersons.$el.niceScroll();

                $("#homeButton").removeClass("disabled");
                $("#backButton").removeClass("disabled");
                $("#moviesButton").removeClass("disabled");
                $("#personsButton").addClass("disabled");

            },
            movies: function () {

                $("#BackBoneContainer").html($("#SpinnerContainer").html());

                var self = this;
                if (!this.ViewMovies) {
                    var viewClass = require('views/movies');
                    var collectionClass = require('collections/movies');

                    this.ViewMovies = new viewClass({collection: new collectionClass()});

                    this.ViewMovies.on('FetchStart', this.showSpinner, this);
                    this.ViewMovies.on('FetchSuccess', this.hideSpinner, this);
                    this.ViewMovies.on('FetchFail', this.reRoute, this);

                    this.ViewMovies.resetCollection([]);
                }

                if (!this.ViewGenres) {
                    var viewClass = require('views/genres');
                    var collectionClass = require('collections/genres');

                    this.ViewGenres = new viewClass({collection: new collectionClass()});

                    this.ViewGenres.on('FetchStart', this.showSpinner, this);
                    this.ViewGenres.on('FetchSuccess', this.hideSpinner, this);
                    this.ViewGenres.on('FetchFail', this.reRoute, this);

                    this.ViewGenres.resetCollection();

                    this.ViewGenres.on('UpdateMovies', this.ViewMovies.resetCollection, this.ViewMovies);
                }

                $("#BackBoneContainer").html(self.ViewMovies.$el);
                self.ViewMovies.$el.niceScroll();

                $("#BackBoneContainer").append(self.ViewGenres.$el);
                self.ViewMovies.delegateEvents();
                self.ViewGenres.delegateEvents();

                $("#homeButton").removeClass("disabled");
                $("#backButton").removeClass("disabled");
                $("#moviesButton").addClass("disabled");
                $("#personsButton").removeClass("disabled");
            },
            showSpinner: function () {
                $("#SpinnerContainer").removeClass("hidden");
                console.log("Show Spinner");
            },
            hideSpinner: function () {
                $("#SpinnerContainer").addClass("hidden");
                console.log("Hide Spinner");
            },
            addError: function (err) {
                var newElement = $('#alertContainer').find('div').clone();
                $(newElement).find("p").html(err);
                $('.alertContainer').append(newElement);
            },
            reRoute: function (err) {
                if (err) {
                    this.addError(err);
                }
                console.log("Rerouting");
                window.location.hash = '';
            }
        });
    }
);