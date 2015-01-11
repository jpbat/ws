define(
    ['underscore','backbone','tpl!templates/results','jquery'],
    function(_,Backbone,template,$) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "ResultsCont",
            tpl:template,
            className: "col-md-12",
            isLoading: false,
            limit :8,
            offset :0,
            initialize: function() {
                this.collection.on('reset', this.render, this);
            },
            render: function() {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.append(templateHTML);

                return this;
            },
            //COLLECTION RELATED
            resetCollection:function(data){
                var self = this;
                this.offset = 0;

                this.$el.html(" ");

                self.data = data;

                self.trigger('FetchStart');

                this.collection.fetch({
                    data: {limit:self.limit,offset:self.offset,query:self.data },
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

                self.collection.fetch({
                    data:{limit:self.limit,offset:self.offset,query: self.data},
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
            //EVENTS RELATED
            events: {
                'scroll': 'checkScroll',
                'click .item':'openSelected'
            },
            checkScroll: function () {
                var triggerPoint = 100; // 100px from the bottom
                if( !this.isLoading && this.el.scrollTop + this.el.clientHeight + triggerPoint > this.el.scrollHeight ) {
                    this.isLoading = true;
                    this.fetchNextCollection();
                }
            },
            openSelected:function(event){
                var caller = event.target || event.srcElement;

                if(!($(caller).hasClass("item"))){
                    caller = $(caller).closest("div[data-uri]");
                }
                window.location.hash=$(caller).attr("data-uri").replace("http://www.movierecomendation.pt/","#").toLowerCase();
            }
        });
        return View;
    }
);