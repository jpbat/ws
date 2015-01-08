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
                'change input[name=search]': 'search'
            },
            search: function (event) {
                var caller = event.target || event.srcElement;
                //TODO change to trigger
                this.searchView.collection.fetch({
                    data: {query: $(caller).val()},
                    reset: true, type: 'GET',
                    success: function (data) {

                        console.log(data.toJSON());
                    },
                    error: function () {
                        console.log("error");
                    }
                });

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