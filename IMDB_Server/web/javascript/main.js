require.config({
    baseUrl:'/javascript/',
    paths:{
        jquery          :'libs/jquery-2.1.1.min',
        underscore      :'libs/underscore-min',
        backbone        :'libs/backbone-min',
        backboneSubRoute:'libs/backbone.subroute',
        bootstrap       :'libs/bootstrap.min',
        tpl             :'libs/tpl',
        text            :'libs/text',
        routes          :'routers',
        models          :'models',
        collections     :'collections',
        views           :'views',
        templates       :'templates'
    },
    shim:{
        jquery:{
            exports: "$"
        },
        underscore: {
            exports: '_'
        },

        backbone: {
            exports: "SubRoute",
            deps: ["underscore", "jquery"]

        },
        backboneSubRoute: {
            deps: ["underscore", "jquery",'backbone']
        },
        bootstrap:{
            deps: ['jquery']
        }
    }
});

require([ 'application','jquery','bootstrap']
    , function(App,$){
        $(".alert").alert();
        var MainApp = new App;
        MainApp.init();
    });