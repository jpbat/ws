define(
    ['underscore','backbone','tpl!templates/search','jquery'],
    function(_,Backbone,template,$) {
        return Backbone.View.extend({
            tagName: "div",
            id: "SearchResult",
            minChars: 3,
            searchTimeout:1000,
            limit:20,
            offset:0,
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
                var self=this;
                window.clearTimeout(this.timeoutFunc);
                if(data.length>=this.minChars){
                    this.data = data;
                    window.clearTimeout(this.timeoutFunc);
                    this.timeoutFunc=window.setTimeout(function(){self.resetCollection(self.data);},this.searchTimeout);
                }else{
                    this.data="";
                    this.$el.html(" ");
                }

            },
            //COLLECTION RELATED
            resetCollection:function(data){
                var self = this;
                this.offset = 0;

                this.$el.html(" ");

                self.trigger('FetchStart');

                this.collection.fetch({
                    data: {limit:self.limit,offset:self.offset,query: data},
                    reset: true,
                    type: 'GET',
                    success: function () {
                        self.offset+=self.limit;
                        self.isLoading = false;
                        self.trigger('FetchSuccess');
                    },
                    error: function () {
                        self.isLoading = false;
                        self.trigger('FetchFail',"Fail to retrieve the Result list!");
                    }
                });
            },
            fetchNextCollection:function(){
                var self = this;

                self.trigger('FetchStart');

                self.collection.fetch({data:{limit:self.limit,offset:self.offset,query: self.data},
                    type: 'GET',
                    reset: true,
                    success: function () {
                        self.offset+=self.limit;
                        self.isLoading = false;
                        self.trigger('FetchSuccess');
                    },
                    error: function () {
                        self.isLoading = false;
                        self.trigger('FetchFail',"Fail to retrieve query Results!");
                    }
                });
            },
            //EVENTS RELATED
            events: {   'scroll': 'checkScroll',
                        'click li':'openSelected'
            },
            checkScroll: function () {
                var self = this;
                var triggerPoint = 100; // 100px from the bottom
                if( !this.isLoading && this.el.scrollTop + this.el.clientHeight + triggerPoint > this.el.scrollHeight ) {
                    this.isLoading = true;
                    self.fetchNextCollection();
                }
            },
            openSelected:function(){
                var caller = event.target || event.srcElement;
                window.location.hash=$(caller).attr("data-uri").replace("http://www.movierecomendation.pt/","#").toLowerCase();
            }
        });
    }
);