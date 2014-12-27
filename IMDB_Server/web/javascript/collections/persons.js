define(
    ['underscore','backbone','models/person'],
    function( _, Backbone,LocalModel){

        var collection = Backbone.Collection.extend({
            url: 'http://localhost:8080/rest/Persons/GetAll/',
            model: LocalModel
        });

        return collection;
    }
);