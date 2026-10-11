// API Base URL
const API_BASE_URL = '/api/v1';

// Helper function to get auth headers with JWT token
function getAuthHeaders() {
    const token = localStorage.getItem('jwt_token');
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
}

// Check authentication on page load
function checkAuth() {
    const token = localStorage.getItem('jwt_token');
    const role = localStorage.getItem('user_role');
    
    if (!token) {
        window.location.href = '/login.html';
        return;
    }
    
    // Show Admin tab only if the user has the ADMIN role
    if (role === 'ADMIN') {
        const adminNavItem = document.getElementById('admin-nav-item');
        if (adminNavItem) {
            adminNavItem.classList.remove('d-none');
        }
    }
}

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
    const targetSection = document.getElementById(`${sectionName}-section`);
    if (targetSection) {
        targetSection.classList.remove('d-none');
    }
    
    // Update active nav link
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    const activeLink = document.querySelector(`[data-section="${sectionName}"]`);
    if (activeLink) {
        activeLink.classList.add('active');
    }
    
    // Load section data
    if (sectionName === 'drones') {
        loadDrones();
    } else if (sectionName === 'maintenance') {
        loadMaintenanceDrones();
    } else if (sectionName === 'telemetry') {
        loadDroneSelect();
    } else if (sectionName === 'admin') {
        loadAdminDrones();
    }
}

// Drones Section
async function loadDrones() {
    try {
        const response = await fetch(`${API_BASE_URL}/drones`, {
            headers: getAuthHeaders()
        });
        
        if (response.status === 401) {
            localStorage.removeItem('jwt_token');
            localStorage.removeItem('user_role');
            window.location.href = '/login.html';
            return;
        }
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const drones = await response.json();
        
        // Update counters
        document.getElementById('total-drones').textContent = drones.length;
        document.getElementById('available-drones').textContent = 
            drones.filter(d => d.status === 'AVAILABLE').length;
        document.getElementById('mission-drones').textContent = 
            drones.filter(d => d.status === 'IN_MISSION').length;
        
        // Load maintenance count
        const maintResponse = await fetch(`${API_BASE_URL}/drones/maintenance-needed`, {
            headers: getAuthHeaders()
        });
        
        if (maintResponse.ok) {
            const maintDrones = await maintResponse.json();
            document.getElementById('maintenance-drones').textContent = maintDrones.length;
        }
        
        // Populate table
        const tbody = document.getElementById('drones-table-body');
        tbody.innerHTML = '';
        
        if (drones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No drones found</td></tr>';
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
        console.error('Error loading drones:', error);
        document.getElementById('drones-table-body').innerHTML = 
            '<tr><td colspan="6" class="text-center text-danger">Error loading drones</td></tr>';
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
        const response = await fetch(`${API_BASE_URL}/drones/maintenance-needed`, {
            headers: getAuthHeaders()
        });
        
        if (response.status === 401) {
            localStorage.removeItem('jwt_token');
            localStorage.removeItem('user_role');
            window.location.href = '/login.html';
            return;
        }
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const drones = await response.json();
        
        const tbody = document.getElementById('maintenance-table-body');
        tbody.innerHTML = '';
        
        if (drones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No drones need maintenance</td></tr>';
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
        document.getElementById('maintenance-table-body').innerHTML = 
            '<tr><td colspan="6" class="text-center text-danger">Error loading data</td></tr>';
    }
}

// Telemetry Section
async function loadDroneSelect() {
    try {
        const response = await fetch(`${API_BASE_URL}/drones`, {
            headers: getAuthHeaders()
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
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
        const response = await fetch(`/api/realtime/drones/${serialNumber}`, {
            headers: getAuthHeaders()
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
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
            headers: getAuthHeaders(),
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
                    <strong>Error:</strong> ${data.error || 'Unknown error'}
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

// ==========================================
// ADMIN SECTION FUNCTIONS
// ==========================================

// Admin Section: Load drones with action buttons
async function loadAdminDrones() {
    try {
        const response = await fetch(`${API_BASE_URL}/drones`, {
            headers: getAuthHeaders()
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const drones = await response.json();
        
        const tbody = document.getElementById('admin-drones-table-body');
        tbody.innerHTML = '';
        
        if (drones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No drones found</td></tr>';
            return;
        }
        
        drones.forEach(drone => {
            const isMaintenance = drone.status === 'MAINTENANCE';
            const btnClass = isMaintenance ? 'btn-secondary' : 'btn-warning';
            const btnText = isMaintenance ? 'In Maintenance' : 'Send to Maintenance';
            const btnDisabled = isMaintenance ? 'disabled' : '';
            
            const row = `
                <tr>
                    <td>${drone.droneId}</td>
                    <td>${drone.serialNumber}</td>
                    <td>${drone.droneModel?.name || 'N/A'}</td>
                    <td><span class="badge bg-${getStatusColor(drone.status)}">${drone.status}</span></td>
                    <td>${drone.batteryLevel}%</td>
                    <td>
                        <button class="btn btn-sm ${btnClass}" ${btnDisabled} 
                                onclick="sendToMaintenance(${drone.droneId}, '${drone.serialNumber}')">
                            <i class="bi bi-tools"></i> ${btnText}
                        </button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading admin drones:', error);
        document.getElementById('admin-drones-table-body').innerHTML = 
            '<tr><td colspan="6" class="text-center text-danger">Error loading data</td></tr>';
    }
}

// Admin Section: Add new drone
const addDroneForm = document.getElementById('add-drone-form');
if (addDroneForm) {
    addDroneForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const payload = {
            droneModelId: parseInt(document.getElementById('drone-model-id').value),
            status: document.getElementById('drone-status').value,
            batteryLevel: parseFloat(document.getElementById('drone-battery').value),
            totalFlightHours: parseFloat(document.getElementById('drone-hours').value)
        };

        try {
            const response = await fetch('/api/admin/drones', {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                alert('Drone added successfully!');
                document.getElementById('add-drone-form').reset();
                loadAdminDrones(); // Reload table
                loadDrones();      // Update main counters
            } else {
                const err = await response.json();
                alert('Error: ' + (err.message || 'Failed to add drone'));
            }
        } catch (error) {
            console.error('Error adding drone:', error);
            alert('Connection error');
        }
    });
}

// Admin Section: Send to maintenance
async function sendToMaintenance(droneId, serialNumber) {
    const description = prompt(`Enter maintenance description for drone ${serialNumber}:`, "Routine check");
    if (!description) return; // Cancelled by user

    // We use ID 1 as default technician (ensure you have a user with ID 1 in your DB)
    const technicianId = 1; 

    try {
        const response = await fetch(`/api/admin/drones/${droneId}/send-to-maintenance?description=${encodeURIComponent(description)}&technicianId=${technicianId}`, {
            method: 'PATCH',
            headers: getAuthHeaders()
        });

        if (response.ok) {
            alert('Drone sent to maintenance successfully!');
            loadAdminDrones();
            loadDrones();
            loadMaintenanceDrones();
        } else {
            alert('Failed to send drone to maintenance.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Connection error');
    }
}

// Load initial data
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    showSection('drones');
});