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
        creditRatingTest: {
            results: [],
            tableHeaders: ["Customer ID", "Goal", "Status"],
            idKey: "customerId",
            message: "",
            title: "Credit Rating Check Results",
            validationFinished: false
        },
        productAvailabilityTest: {
            results: [],
            tableHeaders: ["Product ID", "Goal", "Status"],
            idKey: "productId",
            message: "",
            title: "Product Availability Check Results",
            validationFinished: false
        },
        orderTest: {
            results: [],
            tableHeaders: ["Order", "Goal", "Status"],
            idKey: "orderDescription",
            message: "",
            title: "End-2-End Order Check Results",
            validationFinished: false
        }
    },

    methods: {

        startValidation() {
            const productSrvEndpoint = "http://localhost:8000/products";
            const customerSrvEndpoint = "http://localhost:8010/customers";
            const orderSrvEndpoint = "http://localhost:8020/orders";
            const marketingSrvEndpoint = "http://localhost:8030/marketing-mails";

            // test the customer credit rating check adjustments
            axios.get("app/data/credit-rating-checks.json").then(response => {
                const testSpecs = response.data;
                this.creditRatingTest.validationFinished = false;
                this.creditRatingTest.message = "Validation started...";
                const customerSrvPromises = [];
                testSpecs.forEach(check => {
                    customerSrvPromises.push(axios.get(`${customerSrvEndpoint}/${check.customerId}/credit-rating-check`));
                });
                Promise.all(customerSrvPromises).then(results => {
                    results.forEach((result, index) => {
                        testSpecs[index].successful.achieved = result.data.acceptable;
                    });
                    this.creditRatingTest.results = testSpecs;
                    this.creditRatingTest.message = "Validation finished!";
                    this.creditRatingTest.validationFinished = true;
                });
            });


            // test the product availability check adjustments
            axios.get("app/data/product-availability-checks.json").then(response => {
                const testSpecs = response.data;
                this.productAvailabilityTest.validationFinished = false;
                this.productAvailabilityTest.message = "Validation started...";
                const productSrvPromises = [];
                testSpecs.forEach(check => {
                    productSrvPromises.push(axios.get(`${productSrvEndpoint}/${check.productId}/availability?amount=${check.amount}`));
                });
                Promise.all(productSrvPromises).then(results => {
                    results.forEach((result, index) => {
                        testSpecs[index].successful.achieved = result.data.available;
                    });
                    this.productAvailabilityTest.results = testSpecs;
                    this.productAvailabilityTest.message = "Validation finished!";
                    this.productAvailabilityTest.validationFinished = true;
                });
            });

            // test order proccess adjustments end-2-end
            axios.get("app/data/order-checks.json").then(response => {
                const testSpecs = response.data;
                this.orderTest.validationFinished = false;
                this.orderTest.message = "Validation started...";
                const orderSrvPromises = [];
                testSpecs.forEach(check => {
                    orderSrvPromises.push(axios.post(orderSrvEndpoint, check.order).catch(e => e));
                });
                Promise.all(orderSrvPromises).then(results => {
                    results.forEach((result, index) => {
                        if (result.response && result.response.status > 380) {
                            // http error
                            testSpecs[index].successful.achieved = false;
                        } else {
                            // http success, further check for marketing mail
                            const orderId = parseInt(result.data.message.substring(result.data.message.indexOf("ID ") + 3, result.data.message.indexOf(" success")));
                            axios.get(marketingSrvEndpoint).then(result => {
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