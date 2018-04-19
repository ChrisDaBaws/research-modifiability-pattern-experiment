Vue.prototype.$patternVersion = undefined;

Vue.component("test-results", {
    props: ["test"],
    template: "#results-component",
    methods: {
        hasFailed(results) {
            return results.map(result => result.successful.expected === result.successful.achieved).includes(false);
        }
    }
});

const startPage = Vue.component("start-page", {
    template: "#startpage",
    data: function () {
        return {
            versionDetermined: false
        };
    },
    created() {
        axios.get("http://localhost:8021/healthcheck").then(response => {
            console.log("OrderProcessSrv detected... Pattern version!");
            Vue.prototype.$patternVersion = true;
            this.versionDetermined = true;
        }).catch(e => {
            console.log("No OrderProcessSrv detected... Non-pattern version!");
            Vue.prototype.$patternVersion = false;
            this.versionDetermined = true;
        });
    }
});

const exercise01 = Vue.component("exercise01", {
    template: "#exercise01",
    data: function () {
        return {
            orderTest: {
                results: [],
                idKey: "id",
                message: "",
                title: "End-2-End Order Check Results",
                validationFinished: false
            },
            notificationSrvEndpoint: "http://localhost:8010/marketing-mails",
            orderSrvEndpoint: this.$patternVersion ? "http://localhost:8020/order-process" : "http://localhost:8030/orders"
        }
    },

    methods: {
        startValidation() {
            // test order proccess adjustments end-2-end
            axios.get("app/data/order-checks.json").then(response => {
                const testSpecs = response.data;
                this.orderTest.validationFinished = false;
                this.orderTest.message = "Validation started...";
                const orderSrvPromises = [];
                testSpecs.forEach(check => {
                    orderSrvPromises.push(axios.post(this.orderSrvEndpoint, check.order).catch(e => e));
                });
                Promise.all(orderSrvPromises).then(results => {
                    results.forEach((result, index) => {
                        if (result.response && result.response.status > 380) {
                            // http error
                            testSpecs[index].successful.achieved = false;
                        } else {
                            // http success, further check for marketing mail
                            const orderId = parseInt(result.data.message.substring(result.data.message.indexOf("ID ") + 3, result.data.message.indexOf(" success")));
                            axios.get(this.notificationSrvEndpoint).then(result => {
                                result.data = result.data || [];
                                testSpecs[index].successful.achieved = result.data.find(mail => mail.order.id === orderId) !== undefined;
                            });
                        }
                    });
                    this.orderTest.results = testSpecs;
                    this.orderTest.message = "Validation finished!";
                    this.orderTest.validationFinished = true;
                });
            });
        }
    }
});

const exercise02 = Vue.component("exercise02", {
    template: "#exercise02",
    data: function () {
        return {
            decompositionTest: {
                results: [],
                idKey: "id",
                message: "",
                title: "Decomposition Check Results",
                validationFinished: false
            }
        }
    },

    methods: {
        startValidation() {
            // test service decomposition end-2-end
            axios.get("app/data/decomposition-checks.json").then(response => {
                const testSpecs = response.data;
                this.decompositionTest.validationFinished = false;
                this.decompositionTest.message = "Validation started...";
                const promises = [];
                testSpecs.forEach(check => {
                    if (check.config.url === "order") {
                        check.config.url = this.$patternVersion ? "http://localhost:8020/order-process" : "http://localhost:8030/orders";
                    }
                    promises.push(axios(check.config).catch(e => e));
                });
                Promise.all(promises).then(results => {
                    results.forEach((result, index) => {
                        if ((result.response && result.response.status > 380) || result.data === undefined) {
                            // http error
                            testSpecs[index].successful.achieved = false;
                        } else {
                            // http success
                            testSpecs[index].successful.achieved = true;
                        }
                    });
                    this.decompositionTest.results = testSpecs;
                    this.decompositionTest.message = "Validation finished!";
                    this.decompositionTest.validationFinished = true;
                });
            });
        }
    }
});

const routes = [{
        path: "/",
        component: startPage
    },
    {
        path: "/exercise01",
        component: exercise01
    },
    {
        path: "/exercise02",
        component: exercise02
    }
];

const router = new VueRouter({
    routes
});

const app = new Vue({
    el: "#main",
    router
});