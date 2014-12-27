define(
    ['underscore','backbone','models/genre'],
    function( _, Backbone,LocalModel){

        var collection = Backbone.Collection.extend({
            url: 'http://localhost:8080/rest/Genres/GetAll/',
            model: LocalModel
        });

        return collection;
    }
);