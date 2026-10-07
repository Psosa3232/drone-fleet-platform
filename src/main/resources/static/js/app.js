// API Base URL
const API_BASE_URL = '/api/v1';

// Navigation
document.querySelectorAll('.nav-link').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const section = link.getAttribute('data-section');
        showSection(section);
    });
});

function showSection(sectionName) {
    // Hide all sections
    document.querySelectorAll('.section').forEach(section => {
        section.classList.add('d-none');
    });
    
    // Show selected section
    document.getElementById(`${sectionName}-section`).classList.remove('d-none');
    
    // Update active nav link
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    document.querySelector(`[data-section="${sectionName}"]`).classList.add('active');
    
    // Load section data
    if (sectionName === 'drones') {
        loadDrones();
    } else if (sectionName === 'maintenance') {
        loadMaintenanceDrones();
    } else if (sectionName === 'telemetry') {
        loadDroneSelect();
    }
}

// Drones Section
async function loadDrones() {
    try {
        const response = await fetch(`${API_BASE_URL}/drones`);
        const drones = await response.json();
        
        // Update counters
        document.getElementById('total-drones').textContent = drones.length;
        document.getElementById('available-drones').textContent = 
            drones.filter(d => d.status === 'AVAILABLE').length;
        document.getElementById('mission-drones').textContent = 
            drones.filter(d => d.status === 'IN_MISSION').length;
        
        // Load maintenance count
        const maintResponse = await fetch(`${API_BASE_URL}/drones/maintenance-needed`);
        const maintDrones = await maintResponse.json();
        document.getElementById('maintenance-drones').textContent = maintDrones.length;
        
        // Populate table
        const tbody = document.getElementById('drones-table-body');
        tbody.innerHTML = '';
        
        drones.forEach(drone => {
            const row = `
                <tr>
                    <td>${drone.droneId}</td>
                    <td>${drone.serialNumber}</td>
                    <td>${drone.droneModel?.name || 'N/A'}</td>
                    <td><span class="badge bg-${getStatusColor(drone.status)}">${drone.status}</span></td>
                    <td>${drone.batteryLevel}%</td>
                    <td>${drone.totalFlightHours}h</td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading drones:', error);
    }
}

function getStatusColor(status) {
    switch(status) {
        case 'AVAILABLE': return 'success';
        case 'IN_MISSION': return 'warning';
        case 'MAINTENANCE': return 'danger';
        default: return 'secondary';
    }
}

// Maintenance Section
async function loadMaintenanceDrones() {
    try {
        const response = await fetch(`${API_BASE_URL}/drones/maintenance-needed`);
        const drones = await response.json();
        
        const tbody = document.getElementById('maintenance-table-body');
        tbody.innerHTML = '';
        
        if (drones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">No drones need maintenance</td></tr>';
            return;
        }
        
        drones.forEach(drone => {
            const row = `
                <tr>
                    <td>${drone.droneId}</td>
                    <td>${drone.serialNumber}</td>
                    <td>${drone.droneModel?.name || 'N/A'}</td>
                    <td><span class="badge bg-${getStatusColor(drone.status)}">${drone.status}</span></td>
                    <td>${drone.batteryLevel}%</td>
                    <td>${drone.totalFlightHours}h</td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading maintenance drones:', error);
    }
}

// Telemetry Section
async function loadDroneSelect() {
    try {
        const response = await fetch(`${API_BASE_URL}/drones`);
        const drones = await response.json();
        
        const select = document.getElementById('drone-select');
        select.innerHTML = '<option value="">Choose a drone...</option>';
        
        drones.forEach(drone => {
            select.innerHTML += `<option value="${drone.serialNumber}">${drone.serialNumber}</option>`;
        });
        
        select.addEventListener('change', (e) => {
            if (e.target.value) {
                loadTelemetry(e.target.value);
            }
        });
    } catch (error) {
        console.error('Error loading drone select:', error);
    }
}

async function loadTelemetry(serialNumber) {
    try {
        const response = await fetch(`/api/realtime/drones/${serialNumber}`);
        const data = await response.json();
        
        const telemetryDiv = document.getElementById('telemetry-data');
        telemetryDiv.innerHTML = `
            <div class="metric">
                <div class="metric-label">Latitude</div>
                <div class="metric-value">${data.latitude || 'N/A'}</div>
            </div>
            <div class="metric">
                <div class="metric-label">Longitude</div>
                <div class="metric-value">${data.longitude || 'N/A'}</div>
            </div>
            <div class="metric">
                <div class="metric-label">Altitude</div>
                <div class="metric-value">${data.altitude || 'N/A'} m</div>
            </div>
            <div class="metric">
                <div class="metric-label">Speed</div>
                <div class="metric-value">${data.speed || 'N/A'} m/s</div>
            </div>
            <div class="metric">
                <div class="metric-label">Battery Level</div>
                <div class="metric-value">${data.battery_level || 'N/A'}%</div>
            </div>
            <div class="metric">
                <div class="metric-label">Temperature</div>
                <div class="metric-value">${data.temperature || 'N/A'} °C</div>
            </div>
            <div class="metric">
                <div class="metric-label">Last Update</div>
                <div class="metric-value">${data.last_update || 'N/A'}</div>
            </div>
        `;
    } catch (error) {
        console.error('Error loading telemetry:', error);
        document.getElementById('telemetry-data').innerHTML = 
            '<p class="text-danger">No real-time data available for this drone</p>';
    }
}

// ML Predictions Section
document.getElementById('prediction-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const speed = parseFloat(document.getElementById('speed').value);
    const altitude = parseFloat(document.getElementById('altitude').value);
    const temperature = parseFloat(document.getElementById('temperature').value);
    const distance = parseFloat(document.getElementById('distance').value);
    
    const predictionDiv = document.getElementById('prediction-result');
    predictionDiv.innerHTML = '<p class="text-muted">Calculating prediction...</p>';
    
    try {
        const response = await fetch('/api/v1/ml/predict', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                speed: speed,
                altitude: altitude,
                temperature: temperature,
                distance_from_start: distance
            })
        });
        
        const data = await response.json();
        
        if (data.status === 'success') {
            predictionDiv.innerHTML = `
                <div class="prediction-box">
                    <p class="text-muted">Predicted Battery Level</p>
                    <div class="prediction-value">${data.predicted_battery_level}%</div>
                    <p class="text-muted mt-3">Based on:</p>
                    <ul class="list-unstyled">
                        <li>Speed: ${speed} m/s</li>
                        <li>Altitude: ${altitude} m</li>
                        <li>Temperature: ${temperature} °C</li>
                        <li>Distance: ${distance} m</li>
                    </ul>
                </div>
            `;
        } else {
            predictionDiv.innerHTML = `
                <div class="alert alert-danger">
                    <strong>Error:</strong> ${data.error}
                </div>
            `;
        }
    } catch (error) {
        predictionDiv.innerHTML = `
            <div class="alert alert-danger">
                <strong>Error:</strong> Could not connect to ML service. Make sure it's running on port 5000.
            </div>
        `;
    }
});

// Load initial data
document.addEventListener('DOMContentLoaded', () => {
    showSection('drones');
});