define(
    ['underscore','backbone','tpl!templates/persons','jquery'],
    function(_,Backbone,template,$) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "PersonsCont",
            tpl:template,
            className: "col-md-12",

            initialize: function() {
                this.isLoading = false;
                this.limit = 5;
                this.offset = 0;
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

                this.ViewMovies.collection.fetch({data:{limit:self.limit,offset:self.offset},
                    reset: true, type: 'GET',
                    success: function () {
                        self.offset+=self.limit;

                        console.log("success");
                    },
                    error: function () {
                        var newElement = $('#alertContainer div').clone();
                        $(newElement).find("p").html("Fail to retrieve the Persons list!");
                        $('.alertContainer').append(newElement);
                        console.log("Fail to retrieve Persons");
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
                    console.log("get off:"+self.offset+" limit:"+self.limit);

                    self.collection.fetch({data:{limit:self.limit,offset:self.offset},
                        type: 'GET',
                        success: function () {

                            self.offset+=self.limit;
                            self.render();
                            self.isLoading = false;
                        },
                        error: function () {
                            var newElement = $('#alertContainer div').clone();
                            $(newElement).find("p").html("Fail to retrieve the persons list!");
                            $('.alertContainer').append(newElement);
                            console.log("Fail to retrieve persons");
                        }
                    });

                }
            }
        });
        return View;
    }
);