define(
    ['underscore','backbone','tpl!templates/genres'],
    function(_,Backbone,template) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            className: "col-md-2",
            tpl:template,
            initialize: function() {
                this.collection.on('reset', this.render, this);
            },
            render: function() {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.html(templateHTML);

                return this;
            }

        });


        return View;
    }
);