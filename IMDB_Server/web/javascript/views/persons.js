define(
    ['underscore','backbone','tpl!templates/persons','jquery'],
    function(_,Backbone,template,$) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "PersonsCont",
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
            resetCollection:function(){
                var self = this;
                this.offset = 0;

                this.$el.html(" ");

                self.trigger('FetchStart');

                this.collection.fetch({
                    data:{  limit:self.limit,
                            offset:self.offset},
                    reset: true,
                    type: 'GET',
                    success: function () {
                        self.offset+=self.limit;
                        self.isLoading = false;
                        self.trigger('FetchSuccess');
                    },
                    error: function () {
                        self.isLoading = false;
                        self.trigger('FetchFail',"Fail to retrieve the Persons list!");

                    }
                });
            },
            fetchNextCollection:function(){
                var self = this;

                self.trigger('FetchStart');

                self.collection.fetch({
                    data:{  limit:self.limit,
                            offset:self.offset},
                    reset: true,
                    type: 'GET',
                    success: function () {
                        self.offset+=self.limit;
                        self.isLoading = false;
                        self.trigger('FetchSuccess');
                    },
                    error: function () {
                        self.isLoading = false;
                        self.trigger('FetchFail',"Fail to retrieve the Persons list!");
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