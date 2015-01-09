define(
    ['underscore','backbone','tpl!templates/Movie','jquery'],
    function(_,Backbone,template,$) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            tpl:template,
            className: "",
            initialize: function() {
                this.model.on('change', this.render, this);
            },
            render: function() {
                var Result = this.model.toJSON();

                var templateHTML = this.tpl({model:Result});

                this.$el.html(templateHTML);
                return this;
            },
            updateModel:function(id){
                var self = this;
                this.model.fetch({
                    reset: true,
                    type: 'GET',
                    data: {id: id},
                    success: function () {
                        self.trigger('FetchSuccess');
                    },
                    error: function () {
                        self.trigger('FetchFail',"Fail to retrieve movie!");
                    }
                });
            }

        });
        return View;
    }
);