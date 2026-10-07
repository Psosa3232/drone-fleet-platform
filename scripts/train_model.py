"""
train_model.py - Machine Learning pipeline for battery consumption prediction.

Reads historical telemetry data from PostgreSQL, performs feature engineering,
trains a Random Forest Regressor to predict battery levels based on flight
parameters, and saves the trained model for future inference.
"""

import pandas as pd
import psycopg2
import joblib
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error, r2_score
from config import DB_CONFIG


def load_telemetry_data():
    """
    Connects to PostgreSQL and loads historical telemetry data into a Pandas DataFrame.
    
    Returns:
        pd.DataFrame: The telemetry dataset.
    """
    print("Connecting to PostgreSQL to load telemetry data...")
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        query = "SELECT * FROM drone_telemetry"
        df = pd.read_sql_query(query, conn)
        conn.close()
        print(f"Loaded {len(df)} records successfully.")
        return df
    except Exception as e:
        print(f"Error loading data: {e}")
        raise


def prepare_features(df):
    """
    Prepares the features (X) and target variable (y) for training.
    
    Features: speed, altitude, temperature, distance_from_start
    Target: battery_level
    
    Args:
        df (pd.DataFrame): Raw telemetry data.
        
    Returns:
        tuple: (X, y) features and target arrays.
    """
    print("Preparing features and target variable...")
    
    # Define features and target
    features = ['speed', 'altitude', 'temperature', 'distance_from_start']
    target = 'battery_level'
    
    # Drop rows with missing values in critical columns
    df_clean = df[features + [target]].dropna()
    
    X = df_clean[features]
    y = df_clean[target]
    
    print(f"Training set size: {len(X)} records.")
    return X, y


def train_and_evaluate(X, y):
    """
    Splits the data, trains a Random Forest model, and evaluates its performance.
    
    Args:
        X (pd.DataFrame): Feature matrix.
        y (pd.Series): Target vector.
        
    Returns:
        RandomForestRegressor: The trained model.
    """
    print("Splitting data into training and testing sets (80/20)...")
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    
    print("Training Random Forest Regressor...")
    model = RandomForestRegressor(n_estimators=100, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)
    
    # Evaluate
    predictions = model.predict(X_test)
    mae = mean_absolute_error(y_test, predictions)
    r2 = r2_score(y_test, predictions)
    
    print(f"\nModel Evaluation Metrics:")
    print(f"   Mean Absolute Error (MAE): {mae:.4f}")
    print(f"   R-squared (R2): {r2:.4f}")
    
    return model


def save_model(model):
    """
    Saves the trained model to a file using joblib.
    
    Args:
        model: The trained scikit-learn model.
    """
    model_filename = "battery_prediction_model.pkl"
    joblib.dump(model, model_filename)
    print(f"\nModel successfully saved to {model_filename}")


def main():
    """
    Main execution pipeline for the Machine Learning workflow.
    """
    print("=" * 70)
    print("DRONE FLEET - MACHINE LEARNING PIPELINE")
    print("=" * 70)
    print()
    
    # 1. Load Data
    df = load_telemetry_data()
    
    # 2. Prepare Features
    X, y = prepare_features(df)
    
    # 3. Train and Evaluate
    model = train_and_evaluate(X, y)
    
    # 4. Save Model
    save_model(model)
    
    print("\n" + "=" * 70)
    print("ML PIPELINE COMPLETED")
    print("=" * 70)


if __name__ == "__main__":
    main()