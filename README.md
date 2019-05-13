# Location-MetaData-API

1. Support for 10,000 delivery 1 day. Say, morning 1hr, noon 1hr, night 1hr traffic. (10,000 / 3 / 60 / 60) = 1 req/sec

2. **Table**: resource (id: auto inc, name: string, userId: string, company: string, phone: phone, email: email, metadata: json)

3. **POST** for setting single metadata key
   
   **Endpoint** /set-meta
   
   **Payload** {userId: ‘’, metakey: ‘’, metavalue: ‘’}

4. **GET** for getting all metadata of users
   
   **Endpoint** /get-meta
   
   **QueryString** userId=1,2,3

5. **POST** get user matching query
   
   **Endpoint** /search
   
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
