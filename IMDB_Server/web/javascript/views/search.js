define(
    ['underscore','backbone','tpl!templates/search','jquery'],
    function(_,Backbone,template,$) {
        return Backbone.View.extend({
            tagName: "div",
            id: "SearchResult",
            //className: "col-md-2",
            tpl: template,
            initialize: function () {
                this.collection.on('reset', this.render, this);
            },
            render: function () {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.html(templateHTML);

                return this;
            },
            events: {}
        });
    }
);