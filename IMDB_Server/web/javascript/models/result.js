define(
    ['underscore','backbone'],
    function( _, Backbone){
        var model = Backbone.Model.extend({
            defaults: {
                selected:false
            },
            initialize: function () {
                console.log('new Result');
            }
        });
        return model;
    }
);