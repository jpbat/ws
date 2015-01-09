define(
    ['underscore','backbone','tpl!templates/index','jquery','collections/results','views/search'],
    function(_,Backbone,template,$,Searchcollection,SearchView) {
        return Backbone.View.extend({
            tagName: "div",
            id: "",
            className: "",
            tpl: template,
            initialize: function () {
                this.collection.on('reset', this.render, this);
                this.searchView = new SearchView({collection: new Searchcollection()});
            },
            events: {
                'keyup input[name=search]': 'search'
            },
            search: function (event) {
                var caller = event.target || event.srcElement;
                this.searchView.updateSearch($(caller).val());
            },
            render: function () {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.html(templateHTML);
                this.$el.find("#searchCont").append(this.searchView.$el);
                return this;
            },

            emptyRender: function () {

                var templateHTML = this.tpl({collection: []});
                this.$el.html(templateHTML);

                return this;
            }

        });
    }
);