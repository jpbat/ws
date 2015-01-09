define(
    ['underscore','backbone','tpl!templates/genres','jquery'],
    function(_,Backbone,template,$) {
        var View = Backbone.View.extend({
            tagName: "div",
            id: "genreView",
            className: "col-md-2",
            tpl:template,
            initialize: function() {
                this.collection.on('reset', this.render, this);
            },
            render: function() {
                var Result = this.collection.toJSON();

                var templateHTML = this.tpl({collection: Result});
                this.$el.html(templateHTML);

                return this;
            },
            resetCollection:function(){
                this.collection.fetch({reset: true, type: 'GET'});
            },
            events: {
                'change input[name=genres]': 'inputChange'
            },
            inputChange:function(event){
                var caller = event.target || event.srcElement;
                var genName = $(caller).val();
                var isChecked = $(caller).prop('checked');

                //var a =_.where(this.collection,{name:genName});

                var SelectedGenres = [];
                $("input[name=genres]:checked").each(function() {
                    SelectedGenres.push($(this).val());
                });
                console.log("Calling resetCollection");
                this.trigger('UpdateMovies', SelectedGenres);
            }


        });


        return View;
    }
);