"""
ml_service.py - Flask microservice for ML model inference.

Loads the trained battery prediction model and exposes a REST API
for real-time predictions.
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import numpy as np

app = Flask(__name__)
CORS(app)  # Enable CORS for Spring Boot to call this service

# Load the trained model
print("Loading ML model...")
model = joblib.load('battery_prediction_model.pkl')
print("Model loaded successfully.")

@app.route('/predict', methods=['POST'])
def predict():
    """
    Predicts battery level based on flight parameters.
    
    Expected JSON input:
    {
        "speed": 45.5,
        "altitude": 80.0,
        "temperature": 25.0,
        "distance_from_start": 1500.0
    }
    """
    try:
        data = request.get_json()
        
        # Extract features
        features = np.array([[
            data['speed'],
            data['altitude'],
            data['temperature'],
            data['distance_from_start']
        ]])
        
        # Make prediction
        prediction = model.predict(features)[0]
        
        return jsonify({
            'predicted_battery_level': round(float(prediction), 2),
            'status': 'success'
        })
        
    except Exception as e:
        return jsonify({
            'error': str(e),
            'status': 'error'
        }), 400

@app.route('/health', methods=['GET'])
def health():
    """Health check endpoint."""
    return jsonify({'status': 'healthy', 'model_loaded': True})

if __name__ == '__main__':
    print("Starting ML Prediction Service on port 5000...")
    app.run(host='0.0.0.0', port=5000, debug=True)