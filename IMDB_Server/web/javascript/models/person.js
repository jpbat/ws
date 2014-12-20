define(
    ['underscore','backbone'],
    function( _, Backbone){
        var model = Backbone.Model.extend({
            defaults: {},
            initialize: function () {
                console.log('new person');
            },
            url: 'http://localhost:8080/rest/Person/Get/'
        });
        return model;
    }
);