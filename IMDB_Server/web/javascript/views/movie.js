define(
    ['underscore','backbone','tpl!templates/Movie','jquery','views/recommendations','collections/recommendations' ],
    function(_,Backbone,template,$,subview,subCollection) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "",
            tpl:template,
            className: "modelDisplay",
            initialize: function() {
                this.model.on('change', this.render, this);
            },
            render: function() {
                var Result = this.model.toJSON();

                this.subCollection = new subCollection();

                var dat = {};

                if(Result.Actors || Result.Actors.length!=0){
                    var aux = Result.Actors;
                    var temp = new Array();
                    for(var i = 0; i< aux.length ; i++){
                        temp[i]= aux[i].id;
                    }

                    dat.actors = temp.join('|');
                }

                if(Result.Directors || Result.Directors.length!=0){
                    var aux = Result.Directors;
                    var temp = new Array();
                    for(var i = 0; i<aux.length ; i++){
                        temp[i]= aux[i].id;
                    }

                    dat.director = temp.join('|');
                }

                if(Result.Genres || Result.Genres.length!=0){

                    var aux = Result.Genres;
                    var temp = new Array();
                    for(var i = 0; i<aux.length ; i++){
                        temp[i]= aux[i].name;
                    }

                    dat.genres = temp.join('|');
                }

                if(Result.Studios || Result.Studios.length!=0){

                    var aux = Result.Studios;
                    var temp = new Array();
                    for(var i = 0; i<aux.length ; i++){
                        temp[i]= aux[i].id;
                    }

                    dat.studios = temp.join('|');
                }
                this.subView = new subview({collection:this.subCollection});

                this.subCollection.fetch({
                    reset: true,
                    type: 'GET',
                    data: dat
                });

                var templateHTML = this.tpl({model:Result});

                this.$el.html(templateHTML);
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
                    },
                    error: function () {
                        self.trigger('FetchFail',"Fail to retrieve movie!");
                    }
                });
            }

        });
        return View;
    }
);