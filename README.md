# Vehicle Listing Management App

This repository contains the source code of an Android application designed to manage vehicle brands, models, cities, and sale listings. The app uses web services available at the URL `http://argo.td.utfpr.edu.br/carros/ws/<resource>` to perform operations such as registration, querying, updating, and deleting data.

## Key Features

### 1. **Record Maintenance**
   - **Brands**: Registration and management of vehicle brands.
   - **Models**: Registration and management of vehicle models, linked to brands.
   - **Cities**: Registration and management of cities.
   - **Listings**: Registration and management of vehicle sale listings, linked to models and cities.

### 2. **Search for Vehicle Listings**
   - Filter listings by **model**, **year**, and **price**.
   - Optional parameters for advanced search:
     - `model`: Filters by a specific model.
     - `ano_inicial` and `ano_final`: Filters by a range of years.
     - `min` and `max`: Filters by a price range.

### 3. **Web Service Integration**
   - **POST**: Insertion of new records (brands, models, cities, listings) without needing to provide an ID, as the server generates it automatically.
   - **PUT**: Updating existing records while keeping the original ID.
   - **DELETE**: Deleting records.
   - **GET**: Querying records with custom filters (e.g., models by brand, cities by name, listings by model/year/price).

## Server-Side Class Structure
- **Brand**:
  ```json
  {
    "id": Long,
    "nome": String
  }

- **Model**:
  ```json
  {
    "id": Long,
    "nome": String,
    "idMarca": Long,
    "marca": Brand
  }

- **City**:
  ```json
  {
    "id": Long,
    "nome": String,
    "ddd": String
  }

- **Listing**:
  ```json
  {
    "id": Long,
    "modelo": Model,
    "cidade": City,
    "descricao": String,
    "valor": Double,
    "ano": Integer,
    "km": Integer,
    "idCidade": Long,
    "idModelo": Long
  }

## App Requirements
- **Responsive Layouts**: Different layouts for devices in portrait or landscape orientation.
- **Connection Check**: Upon launching the app, it checks for an internet connection. If there is no connection, it displays a warning that the app cannot be used offline.

## How to Use
1. Clone the repository:
   ```bash
   git clone https://github.com/mpGustavo06/Car-Store-in-Android-Studio.git
   
2. Open the project in Android Studio.

3. Run the app on an emulator or physical device.

## Project Structure
- **Models**: Entity classes such as Brand, Model, City, and Listing.
- **Services**: Integration with web services for CRUD operations.
- **Adapters**: Adapters for displaying lists (e.g., models, listings).
- **Activities**: App screens, such as the main screen, brand registration, model registration, city registration, and listing registration.
