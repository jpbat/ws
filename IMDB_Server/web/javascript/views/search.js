define(
    ['underscore','backbone','tpl!templates/search','jquery'],
    function(_,Backbone,template,$) {
        return Backbone.View.extend({
            tagName: "div",
            id: "SearchResult",
            minChars: 3,
            limit:20,
            offset:0,
            //className: "col-md-2",
            tpl: template,
            initialize: function () {
                this.collection.on('reset', this.render, this);
            },
            render: function () {
                var Result = this.collection.toJSON();

                if(Result.length ==0){
                    return this;
                }

                var templateHTML = this.tpl({collection: Result});
                this.$el.append(templateHTML);

                return this;
            },
            updateSearch:function(data){
                var self = this;
                self.offset=0;
                this.$el.html(" ");
                if(data.length>=this.minChars){
                    this.data = data;
                    this.collection.fetch({
                        data: {limit:self.limit,offset:self.offset,query: data},
                        reset: true, type: 'GET',
                        success: function () {
                            self.offset+=self.limit;
                            console.log("Got it");
                        },
                        error: function () {
                            console.log("error");
                        }
                    });
                }else{
                    this.data="";
                }

            },
            events: {'scroll': 'checkScroll'},
            checkScroll: function () {
                var self = this;
                var triggerPoint = 100; // 100px from the bottom
                if( !this.isLoading && this.el.scrollTop + this.el.clientHeight + triggerPoint > this.el.scrollHeight ) {
                    this.isLoading = true;

                    self.collection.fetch({data:{limit:self.limit,offset:self.offset,query: self.data},
                        type: 'GET',
                        success: function () {

                            self.offset+=self.limit;
                            self.render();
                            self.isLoading = false;
                        },
                        error: function () {
                            var newElement = $('#alertContainer').find('div').clone();
                            $(newElement).find("p").html("Fail to retrieve query Results");
                            $('.alertContainer').append(newElement);
                            console.log("Fail to retrieve query Results");
                        }
                    });

                }
            }
        });
    }
);