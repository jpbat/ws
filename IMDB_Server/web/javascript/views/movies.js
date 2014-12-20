define(
    ['underscore','backbone','tpl!templates/Movies'],
    function(_,Backbone,template) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            tpl:template,
            className: "Container",


            initialize: function() {
                this.isLoading = false;
                this.limit = 5;
                this.offset = 0;
                this.collection.on('reset', this.clear, this);
                this.collection.on('add change', this.render, this);

            },

            clear:function(){
                this.limit = 5;
                this.offset = 0;
                this.$el.html(" ");
                console.log("clear");
                this.render();
            },

            render: function() {
                var self = this;
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.append(templateHTML);

                return this;
            },
            events: {
                'scroll': 'checkScroll'
            },
            checkScroll: function () {
                console.log("scroll");
                var triggerPoint = 100; // 100px from the bottom
                if( !this.isLoading && this.el.scrollTop + this.el.clientHeight + triggerPoint > this.el.scrollHeight ) {
                    this.isLoading = true;
                    console.log("!scroll");
                }
            }
        });
        return View;
    }
);