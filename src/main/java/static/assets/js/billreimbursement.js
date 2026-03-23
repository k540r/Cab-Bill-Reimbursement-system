document.addEventListener("DOMContentLoaded", function () {
    const billForm = document.querySelector("form");
    const travelingFrom = document.getElementById("travelingFrom");
    const travelingTo = document.getElementById("travelingTo");
    const project = document.getElementById("project");
    const fromDate = document.getElementById("fromDate");
    const whomToVisit = document.getElementById("whomToVisit");
    const billAmount = document.getElementById("billAmount");
    const billFile = document.getElementById("billFile");

    billForm.addEventListener("submit", function (e) {
        let errors = [];
        const letterSpaceRegex = /^[A-Za-z\s]+$/; // Regex for letters and spaces only

        // Traveling From validation
        if (travelingFrom.value.trim() === "") {
            errors.push("Traveling From field is required.");
        }

        // Traveling To validation
        if (travelingTo.value.trim() === "") {
            errors.push("Traveling To field is required.");
        }

        // Project validation (letters and spaces only)
        if (!letterSpaceRegex.test(project.value.trim())) {
            errors.push("Please enter a valid project name (letters and spaces only).");
        }

        // Date of Journey validation
        if (fromDate.value === "") {
            errors.push("Date of Journey is required.");
        }

        // Whom to Visit validation (letters and spaces only)
        if (!letterSpaceRegex.test(whomToVisit.value.trim())) {
            errors.push("Please enter a valid name for 'Whom to Visit' (letters and spaces only).");
        }

        // Bill Amount validation (positive numbers only)
        if (isNaN(billAmount.value) || billAmount.value <= 0) {
            errors.push("Please enter a valid bill amount (positive numbers only).");
        }

        // Bill File validation (PDF only and size check)
        if (billFile.files.length > 0) {
            const file = billFile.files[0];
            if (!file.name.endsWith(".pdf")) {
                errors.push("Only PDF files are allowed.");
            }
            if (file.size > 200 * 1024) { // 200 KB limit
                errors.push("File size must be less than 200 KB.");
            }
        } else {
            errors.push("Please upload a bill in PDF format.");
        }

        // If there are errors, prevent form submission and display them sequentially
        if (errors.length > 0) {
            e.preventDefault();

            // Function to show errors one by one
            let delay = 0;
            errors.forEach(function (error) {
                setTimeout(function () {
                    alert(error);
                }, delay);
                delay += 1000; // Add a 1 second delay between alerts
            });
        }
    });
});
