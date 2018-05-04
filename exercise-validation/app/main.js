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
            test: {
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
                this.test.validationFinished = false;
                this.test.message = "Validation started...";
                const promises = [];
                testSpecs.forEach(check => {
                    promises.push(axios.post(this.orderSrvEndpoint, check.order).catch(e => e));
                });
                Promise.all(promises).then(results => {
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
                    this.test.results = testSpecs;
                    this.test.message = "Validation finished!";
                    this.test.validationFinished = true;
                });
            });
        }
    }
});

const exercise02 = Vue.component("exercise02", {
    template: "#exercise02",
    data: function () {
        return {
            test: {
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
                this.test.validationFinished = false;
                this.test.message = "Validation started...";
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
                    this.test.results = testSpecs;
                    this.test.message = "Validation finished!";
                    this.test.validationFinished = true;
                });
            });
        }
    }
});

const exercise03 = Vue.component("exercise03", {
    template: "#exercise03",
    data: function () {
        return {
            test: {
                results: [],
                idKey: "id",
                message: "",
                title: "New Product Notification Check Results",
                validationFinished: false
            },
            notificationSrvEndpoint: "http://localhost:8010",
            warehouseSrvEndpoint: "http://localhost:8070"
        }
    },

    methods: {
        startValidation() {
            // test service decomposition end-2-end
            axios.get("app/data/new-product-checks.json").then(response => {
                const testSpecs = response.data;
                this.test.validationFinished = false;
                this.test.message = "Validation started...";
                const promises = [];
                testSpecs.forEach(check => {
                    promises.push(axios(check.config).catch(e => e));
                });
                Promise.all(promises).then(results => {
                    results.forEach((result, index) => {
                        if ((result.response && result.response.status > 380) || result.data === undefined) {
                            // http error
                            testSpecs[index].successful.achieved = false;
                        } else {
                            // http success
                            const newProductId = result.data.message.substring(result.data.message.indexOf("ID ") + 3, result.data.message.indexOf(" succ"));
                            Promise.all([
                                axios.get(`${this.notificationSrvEndpoint}/new-products/${newProductId}`).catch(e => e),
                                axios.get(`${this.notificationSrvEndpoint}/product-mails`).catch(e => e),
                                axios.get(`${this.warehouseSrvEndpoint}/products/${newProductId}/availability?amount=8`).catch(e => e)
                            ]).then(results2 => {
                                if (
                                    // new product has been added
                                    results2[0].data && results2[0].data.id === newProductId &&
                                    // new product mail has been sent
                                    this.findProductMailRequest(results2[1].data, newProductId) &&
                                    // available amount for new product has been ordered
                                    this.checkAvailabilityResponse(results2[2].data)
                                ) {
                                    testSpecs[index].successful.achieved = true;
                                } else {
                                    testSpecs[index].successful.achieved = false;
                                }
                            });
                        }
                    });
                    this.test.results = testSpecs;
                    this.test.message = "Validation finished!";
                    this.test.validationFinished = true;
                });
            });
        },

        findProductMailRequest(data, newProductId) {
            let valid = false;

            for (mail in data) {
                if (mail.product.id === newProductId) {
                    valid = true;
                }
            }

            return valid;
        },

        checkAvailabilityResponse(data) {
            if (data === undefined) {
                return false;
            }

            if (this.$patternVersion) {
                return data.availableAmount === 10;
            } else {
                return data.available;
            }
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
    },
    {
        path: "/exercise03",
        component: exercise03
    }
];

const router = new VueRouter({
    routes
});

const app = new Vue({
    el: "#main",
    router
});