define(
    ['underscore','backbone','tpl!templates/Movies'],
    function(_,Backbone,template) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            tpl:template,
            className: "Container",
            events:{

            },
            initialize: function() {
                this.collection.on('reset', this.render, this);//MODEL SEEM TO OBEY TO CHANGE
            },
            render: function() {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.html(templateHTML);
                console.log(templateHTML);
                return this;
            }
        });
        return View;
    }
);