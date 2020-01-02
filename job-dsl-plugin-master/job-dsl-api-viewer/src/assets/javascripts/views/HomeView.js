App.HomeView = Marionette.LayoutView.extend({

    template: 'home',

    regions: {
        contextRegion: '.context-methods-section'
    },

    initialize: function(options) {
        this.config = options.config;
        this.settings = options.settings;
        this.dsl = options.dsl;
        this.plugins = options.plugins;
        this.listenTo(this.settings, 'change', this.render);
    },

    onRender: function() {
        var pathInfo = this.dsl.getPathInfo();
        var methodNode = pathInfo.methodNode;
        var signatures = this.dsl.getContextSignatures(methodNode.contextClass);
        signatures = _.filter(signatures, function(sig) {
            return !this.settings.isPluginsExcluded(sig.plugins);
        }, this);
        var contextView = new App.ContextView({signatures: signatures});
        this.contextRegion.show(contextView);
    },

    serializeData: function() {
        return {plugins: this.plugins, config: this.config};
    }
});
