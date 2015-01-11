define(
    ['underscore','backbone','tpl!templates/index','jquery','collections/results','views/search'],
    function(_,Backbone,template,$,Searchcollection,SearchView) {
        return Backbone.View.extend({
            tagName: "div",
            id: "",
            className: "",
            tpl: template,
            initialize: function () {
                this.collection.on('reset', this.render, this);
                this.searchView = new SearchView({collection: new Searchcollection()});
                this.searchView.on('FetchStart', this.showSpinner, this);
                this.searchView.on('FetchSuccess', this.hideSpinner, this);
                this.searchView.on('FetchFail', this.addError, this);


            },
            render: function () {
                var Result = this.collection.toJSON();
                var templateHTML = this.tpl({collection: Result});

                this.$el.html(templateHTML);
                this.$el.find("#searchCont").append(this.searchView.$el);

                this.searchView.$el.niceScroll();
                this.searchView.delegateEvents();

                this.delegateEvents();

                return this;
            },
            resetCollection: function(){
                var self = this;

                self.trigger('FetchStart');

                this.collection.fetch({
                    reset: true,
                    type: 'GET',
                    success: function () {
                        self.trigger('FetchSuccess');
                    },
                    error: function () {
                        self.trigger('FetchFail',"Fail to retrieve the Top Movie list!");
                        self.collection.reset();
                    }
                });
            },
            events: {
                'keyup input[name=search]': 'search'
            },
            search: function (event) {
                var caller = event.target || event.srcElement;
                if(event.keyCode == 13){
                    window.location.hash="#search/"+encodeURIComponent($(caller).val());
                }else{
                    this.searchView.updateSearch($(caller).val());
                }

            },
            showSpinner: function () {
                this.$el.find("#searchCont").addClass("fetchWait");
                console.log("Show Search Spinner");
            },
            hideSpinner: function () {
                this.$el.find("#searchCont").removeClass("fetchWait");
                console.log("Hide Search Spinner");
            },
            addError: function (err) {
                self.trigger('FetchFail',err);
            }

        });
    }
);