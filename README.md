# Location-MetaData-API

1. Support for 10,000 delivery 1 day. Say, morning 1hr, noon 1hr, night 1hr traffic. (10,000 / 3 / 60 / 60) = 1 req/sec

2. **Table**: resource (id: auto inc, user_id: string, domain: string, metadata: json)

3. **POST** for setting single metadata key
   
   **Endpoint** /api/v1/metadata
   
   **Payload** {user_id: ‘’, metadata: {}}

4. **PATCH** for setting/updating metadata key of a user
   
   **Endpoint** /api/v1/metadata
   
   **Payload** {user_id: ‘’, metadata: {}}

5. **POST** get user matching query
   
   **Endpoint** /api/v1/metadata/search
   
   **Payload** 
    
        "match": {
            "name": "abul",
            "city": ["Dhaka", "Khulna", "Tangail"],
            "carrying": ["abul", "mofiz"]
        },
        "except": {
            "name": "mofiz",
            "city": ["Sylhet", "Chittagong"]
        },
        "range": {
            "age": {
                "lt/lte": 1004,
                "gt/gte": 12132
              }
        }


### Start db in dev:
1. docker-compose up -d
