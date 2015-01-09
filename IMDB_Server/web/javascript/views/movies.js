define(
    ['underscore','backbone','tpl!templates/Movies','jquery'],
    function(_,Backbone,template,$) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "MoviesCont",
            tpl:template,
            className: "col-md-10",

            initialize: function() {
                this.isLoading = false;
                this.limit = 8;
                this.offset = 0;
                this.genres = [];
                this.collection.on('reset', this.render, this);

            },
            render: function() {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.append(templateHTML);

                return this;
            },
            //COLLECTION RELATED
            resetCollection:function(Genres){
                var self = this;
                self.offset = 0;
                self.genres = Genres;

                self.$el.html(" ");
                self.collection.fetch({
                    data:{  limit:self.limit,
                            offset:self.offset,
                            genres:self.genres.join('|')},
                    reset: true,
                    type: 'GET',
                    success: function () {
                        self.offset+=self.limit;
                        self.isLoading = false;
                        self.trigger('FetchSuccess');
                    },
                    error: function () {
                        self.isLoading = false;
                        self.trigger('FetchFail',"Fail to retrieve the movie list!");
                    }
                });
            },
            fetchNextCollection:function(){
                var self = this;
                self.collection.fetch({
                    data:{  limit:self.limit,
                        offset:self.offset,
                        genres:self.genres.join('|')},
                    type: 'GET',
                    reset: true,
                    success: function () {
                        self.offset+=self.limit;
                        self.isLoading = false;
                        self.trigger('FetchSuccess');
                    },
                    error: function () {
                        self.isLoading = false;
                        self.trigger('FetchFail',"Fail to retrieve the movie list!");
                    }
                });
            },
            //EVENTS RELATED
            events: {
                'scroll': 'checkScroll'
            },
            checkScroll: function () {
                var triggerPoint = 100; // 100px from the bottom
                if( !this.isLoading && this.el.scrollTop + this.el.clientHeight + triggerPoint > this.el.scrollHeight ) {
                    this.isLoading = true;
                    this.fetchNextCollection();
                }
            }
        });
        return View;
    }
);