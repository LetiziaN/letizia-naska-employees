document.getElementById('csvFileInput').addEventListener('change', handleFileSelect);

function handleFileSelect(event) {
    const fileInput = event.target;

    if (fileInput.files.length > 0) {
        // Hide the response container
        document.getElementById('responseContainer').style.display = 'none';

        const selectedFile = fileInput.files[0];
        const reader = new FileReader();

        reader.onload = function (e) {
            const csvContent = e.target.result;

            // Parse CSV content into an array of rows and columns
            const rows = csvContent.split('\n');
            const header = rows[0].split(',');
            const data = rows.slice(1).map(row => row.split(','));

            // Display data in the table
            displayDataInTable(header, data);

            // Show the "Show Result" button
            document.getElementById('showResultBtn').style.display = 'block';
        };

        // Read the file as text
        reader.readAsText(selectedFile);
    }
}

function displayDataInTable(header, data) {
    const csvTable = document.getElementById('csvTable');

    // Clear existing table content
    csvTable.innerHTML = '';

    // Create table headers
    const headerRow = document.createElement('tr');
    header.forEach(column => {
        const headerCell = document.createElement('th');
        headerCell.textContent = column;
        headerRow.appendChild(headerCell);
    });
    csvTable.appendChild(headerRow);

    // Create table rows
    data.forEach(rowData => {
        const row = document.createElement('tr');
        rowData.forEach(column => {
            const cell = document.createElement('td');
            cell.textContent = column;
            row.appendChild(cell);
        });
        csvTable.appendChild(row);
    });
}

function fetchResult(file) {
    // Create FormData object and append the file
    const formData = new FormData();
    formData.append('file', file);

    // Send the file to the server
    fetch('http://localhost:8080/employees/upload', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            console.log('Server response:', data);

            // Display response from controller
            displayControllerResponse(JSON.stringify(data, null, 2));
        })
        .catch(error => {
            console.error('Error:', error);
            // Handle errors
        });
}

function showResult() {
    const fileInput = document.querySelector('input[type="file"]');
    const selectedFile = fileInput.files[0];

    // Fetch result and display it
    fetchResult(selectedFile);
}

function displayControllerResponse(response) {
    const responseContainer = document.getElementById('responseContainer');
    const controllerResponse = document.getElementById('controllerResponse');

    // Set response text
    controllerResponse.textContent = response;

    // Show the response container
    responseContainer.style.display = 'block';
}