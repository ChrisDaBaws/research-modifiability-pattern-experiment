const app = new Vue({
    el: ".main",
    data: {
        message1: "",
        validationFinished1: false,
        message2: "",
        validationFinished2: false,
        creditRatingCheckResults: [],
        productAvailabilityCheckResults: []
    },
    created() {
        axios.get("app/data/credit-rating-checks.json").then(response => {
            this.creditRatingCheckResults = response.data;
        });
        axios.get("app/data/product-availability-checks.json").then(response => {
            this.productAvailabilityCheckResults = response.data;
        });
    },
    methods: {

        startValidation() {
            const productSrvEndpoint = "http://localhost:8000/products";
            const customerSrvEndpoint = "http://localhost:8010/customers";
            const orderSrvEndpoint = "http://localhost:8020/orders";

            // test the customer credit rating check adjustments
            this.validationFinished1 = false;
            this.message1 = "Validation started...";
            const customerSrvPromises = [];
            this.creditRatingCheckResults.forEach(check => {
                customerSrvPromises.push(axios.get(`${customerSrvEndpoint}/${check.customerId}/credit-rating-check`));
            });
            Promise.all(customerSrvPromises).then(results => {
                results.forEach((result, index) => {
                    this.creditRatingCheckResults[index].successful.achieved = result.data.acceptable;
                });
                this.message1 = "Validation finished!";
                this.validationFinished1 = true;
            });

            // test the product availability check adjustments
            this.validationFinished2 = false;
            this.message2 = "Validation started...";
            const productSrvPromises = [];
            this.productAvailabilityCheckResults.forEach(check => {
                productSrvPromises.push(axios.get(`${productSrvEndpoint}/${check.productId}/availability?amount=${check.amount}`));
            });
            Promise.all(productSrvPromises).then(results => {
                results.forEach((result, index) => {
                    this.productAvailabilityCheckResults[index].successful.achieved = result.data.available;
                });
                this.message2 = "Validation finished!";
                this.validationFinished2 = true;
            });
        },

        hasFailed(results) {
            return results.map(result => result.successful.expected === result.successful.achieved).includes(false);
        }

    }
});