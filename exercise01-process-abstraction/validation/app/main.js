Vue.component("test-results", {
    props: ["test"],
    template: "#results-component",
    methods: {
        hasFailed(results) {
            return results.map(result => result.successful.expected === result.successful.achieved).includes(false);
        }
    }
});

const app = new Vue({
    el: "#main",
    data: {
        orderTest: {
            results: [],
            tableHeaders: ["Order", "Goal", "Status"],
            idKey: "orderDescription",
            message: "",
            title: "End-2-End Order Check Results",
            validationFinished: false
        },
        marketingSrvEndpoint: "http://localhost:8030/marketing-mails",
        orderSrvEndpoint: "http://localhost:8020/orders"
    },

    created() {
        axios.get("http://localhost:8041/healthcheck").then(response => {
            // Change orderSrv endpoint URL for the pattern version
            console.log("Pattern version! Changing OrderSrv URL...");
            this.orderSrvEndpoint = "http://localhost:8040/order-process";
        }).catch(e => {
            console.log("Non-pattern version! OrderSrv URL stays as is.");
        });
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
                            axios.get(this.marketingSrvEndpoint).then(result => {
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