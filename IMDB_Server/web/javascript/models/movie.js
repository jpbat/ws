define(
    ['underscore','backbone'],
    function( _, Backbone){
        var model = Backbone.Model.extend({
            defaults: {},
            initialize: function () {
                console.log('new movie');
            },
            url: 'http://localhost:8080/rest/Movies/Get/'
        });
        return model;
    }
);