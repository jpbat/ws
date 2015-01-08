define(
    ['underscore','backbone','tpl!templates/index','jquery','collections/results'],
    function(_,Backbone,template,$,Searchcollection) {
        var View = Backbone.View.extend({
            searchDelay:100,
            searchMinSize:3,
            tagName: "div",
            id: "",
            className: "",
            tpl:template,
            initialize: function() {
                this.collection.on('reset', this.render, this);
            },
            events: {
                'change input[name=search]': 'search'
            },
            search:function(event) {
                var caller = event.target || event.srcElement;
                var val = $(caller).val();

                var collection= new Searchcollection();

                collection.fetch({data:{query:val},
                    reset: true, type: 'GET',
                    success: function (data) {

                        console.log(data.toJSON());
                    },
                    error: function () {
                        console.log("error");
                    }
                });

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