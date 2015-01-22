define(
    ['underscore','backbone','tpl!templates/person','jquery','views/recommendations','collections/recommendations' ],
    function(_,Backbone,template,$,subview,subCollection) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            tpl:template,
            className: "modelDisplay",
            events:{

            },
            initialize: function() {
                this.model.on('change', this.render, this);//MODEL SEEM TO OBEY TO CHANGE
            },
            render: function() {
                var Result = this.model.toJSON();

                this.subCollection = new subCollection();

                var templateHTML = this.tpl({model: Result});
                this.$el.html(templateHTML);

                this.subView = new subview({collection:this.subCollection});

                this.subCollection.fetch({
                    reset: true,
                    type: 'GET',
                    data: {personId:Result.id}
                });

                this.$el.find("#recomended").html(this.subView.el);
                return this;
            },
            updateModel:function(id){
                var self = this;

                self.trigger('FetchStart');

                this.model.fetch({
                    reset: true,
                    type: 'GET',
                    data: {id: id},
                    success: function () {
                        self.trigger('FetchSuccess');
                        console.log("Got a Person");
                    },
                    error: function () {
                        self.trigger('FetchFail',"Fail to retrieve the Person!");
                        console.log("Fail to retrieve Person");
                    }
                });
            }
        });
        return View;
    }
);