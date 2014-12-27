define(
    ['underscore','backbone','tpl!templates/index'],
    function(_,Backbone,template) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            className: "",
            tpl:template,
            initialize: function() {
                this.collection.on('reset', this.render, this);//MODEL SEEM TO OBEY TO CHANGE
            },
            render: function() {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.html(templateHTML);

                return this;
            },

            emptyRender: function() {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: [] });
                this.$el.html(templateHTML);

                return this;
            }

        });


        return View;
    }
);