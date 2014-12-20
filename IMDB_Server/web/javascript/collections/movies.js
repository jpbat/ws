define(
    ['underscore','backbone','models/movie'],
    function( _, Backbone,LocalModel){

        var collection = Backbone.Collection.extend({
            url: 'http://localhost:8080/rest/Movies/GetAll/',
            model: LocalModel
        });

        return collection;
    }
);