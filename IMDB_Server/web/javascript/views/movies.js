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
                this.limit = 5;
                this.offset = 0;
                this.genres = [];
                this.collection.on('reset', this.clear, this);

            },

            clear:function(){
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

            ResetCollection:function(Genres){
                console.log("reset collection trigger");
                var self = this;
                this.limit = 5;
                this.offset = 0;

                this.genres = Genres;
                this.ViewMovies.collection.fetch({data:{limit:self.limit,offset:self.offset,genres:self.genres.join('|')},
                    reset: true, type: 'GET',
                    success: function () {
                        self.offset+=self.limit;

                        console.log("success");
                    },
                    error: function () {
                        var newElement = $('#alertContainer div').clone();
                        $(newElement).find("p").html("Fail to retrieve the movie list!");
                        $('.alertContainer').append(newElement);
                        console.log("Fail to retrieve Movies");
                        window.location.hash = '';
                    }
                });
            },
            events: {
                'scroll': 'checkScroll'
            },
            checkScroll: function () {
                var self = this;
                var triggerPoint = 100; // 100px from the bottom
                if( !this.isLoading && this.el.scrollTop + this.el.clientHeight + triggerPoint > this.el.scrollHeight ) {
                    this.isLoading = true;

                    self.collection.fetch({data:{limit:self.limit,offset:self.offset,genres:self.genres.join('|')},
                        type: 'GET',
                        success: function () {

                            self.offset+=self.limit;
                            self.render();
                            self.isLoading = false;
                        },
                        error: function () {
                            var newElement = $('#alertContainer div').clone();
                            $(newElement).find("p").html("Fail to retrieve the movie list!");
                            $('.alertContainer').append(newElement);
                            console.log("Fail to retrieve Movies");
                        }
                    });

                }
            }
        });
        return View;
    }
);