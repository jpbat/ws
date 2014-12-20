define(
    ['underscore','backbone','routes/BaseRouter'],
    function(_,Backbone,BaseRouter){

        var PubSub = _.extend({},Backbone.Events);

        var Application = function(){ };

        _.extend(Application.prototype,{
            BaseRouter: new BaseRouter(),
            init:function(){
                Backbone.history.start();
            }
        });

        return Application;
    }
);