define(
    ['underscore','backbone'],
    function( _, Backbone){
        var model = Backbone.Model.extend({
            defaults: {
                selected:false
            },
            initialize: function () {
                console.log('new Genre');
            }
            //,url: 'http://localhost:8080/rest/Genre/Get/'
        });
        return model;
    }
);