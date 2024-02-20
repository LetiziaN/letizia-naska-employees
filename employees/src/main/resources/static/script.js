document.getElementById('csvFileInput').addEventListener('change', handleFileSelect);

function handleFileSelect(event) {
    const fileInput = event.target;

    if (fileInput.files.length > 0) {
        // Hide the response container
        document.getElementById('responseContainer').style.display = 'none';

        const selectedFile = fileInput.files[0];
        const formData = new FormData();
        formData.append('file', selectedFile);

        // Send the file to the server
        fetch('http://localhost:8080/employees/data', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data && data.length > 0) {
                // Display data in the table
                displayDataInTable(['EmpID', 'ProjectID', 'DateFrom', 'DateTo'], data);

                // Show the "Show Result" button
                document.getElementById('showResultBtn').style.display = 'block';
            } else {
                console.error('No data received from server.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // Handle errors
        });
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
        Object.values(rowData).forEach(value => {
            const cell = document.createElement('td');
            cell.textContent = value;
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