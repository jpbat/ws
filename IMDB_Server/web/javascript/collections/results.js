define(
    ['underscore','backbone','models/result'],
    function( _, Backbone,LocalModel){

        var collection = Backbone.Collection.extend({
            url: 'http://localhost:8080/rest/Util/Search/',
            model: LocalModel
        });

        return collection;
    }
);