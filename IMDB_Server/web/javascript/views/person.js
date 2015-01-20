define(
    ['underscore','backbone','tpl!templates/person','jquery'],
    function(_,Backbone,template,$) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            tpl:template,
            className: "modelDisplay",
            events:{

            },
            initialize: function() {
                this.model.on('change', this.render, this);//MODEL SEEM TO OBEY TO CHANGE
            },
            render: function() {
                var Result = this.model.toJSON();
                console.log(Result);
                var templateHTML = this.tpl({model: Result});
                this.$el.html(templateHTML);

                return this;
            },
            updateModel:function(id){
                var self = this;

                self.trigger('FetchStart');

                this.model.fetch({
                    reset: true,
                    type: 'GET',
                    data: {id: id},
                    success: function () {
                        self.trigger('FetchSuccess');
                        console.log("Got a Person");
                    },
                    error: function () {
                        self.trigger('FetchFail',"Fail to retrieve the Person!");
                        console.log("Fail to retrieve Person");
                    }
                });
            }
        });
        return View;
    }
);