define(
    ['underscore','backbone','tpl!templates/person'],
    function(_,Backbone,template) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            tpl:template,
            className: "Container",
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
            }
        });
        return View;
    }
);