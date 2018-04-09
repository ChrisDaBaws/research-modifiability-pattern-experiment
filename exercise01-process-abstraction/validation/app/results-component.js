Vue.component("test-results", {
    props: ["title", "results", "tableHeaders", "idKey"],
    template: "#results-component",
    data: function () {
        return {
            message: ""
        }
    },
    methods: {
        hasFailed(results) {
            return results.map(result => result.successful.expected === result.successful.achieved).includes(false);
        }
    }
});