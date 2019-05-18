# Location-MetaData-API

1. Support for 10,000 delivery 1 day. Say, morning 1hr, noon 1hr, night 1hr traffic. (10,000 / 3 / 60 / 60) = 1 req/sec

2. **Table**: resource (id: auto inc, user_id: string, domain: string, metadata: json)

3. **POST** /api/v1/users/meta
   
   **Description:** Create/Replace User
   
   **Payload** 
        
        {
            "user_id": "13123", 
            "status": "online", 
            "metadata": {
                "meta1": v1,
                "meta2": "v2"
            }
        }

4. **PATCH** /api/v1/users/meta/{user_id}
   
   **Description:** Merge the given user metadata with 
   existing value.
   
   **Payload** 
        
        {
             "meta3": "v3",
         }
 
5. **POST** /api/v1/users/meta/{user_id}
   
   **Description:** Replaces the entire user metadata 
   with the new value.
   
   **Payload** 
        
        {
             "meta1": "v1",
        }
         
6. **GET** /api/v1/users
      
   **Description:** Returns all the users.
   
   **QueryString:** `page=1&size:100`
   The query params are optional. If not specified only the 
   first 100 will be served.
      
7. **GET** /api/v1/users?user_ids=
      
   **Description:** Returns all users having user_ids.
  
   **QueryString:** `user_ids=1,2,3,...`
   
   **ResponseBody:**
   
       {
           "1": {
               "id": "vivasoft.com_1",
               "user_id": "1",
               "status": "online",
               "domain": "vivasoft.com",
               "metadata": {
                   "carying": [
                       "bag",
                       "laptop",
                       "pen",
                       "rice"
                   ],
                   "destination": {
                       "lat": 10,
                       "lon": 20
                   }
               }
           },
           "2": null,
           "3": {
               "id": "vivasoft.com_3",
               "user_id": "3",
               "status": null,
               "domain": "vivasoft.com",
               "metadata": {
                   "carying": [
                       "bag",
                       "laptop",
                       "pen",
                       "rice"
                   ]
               }
           }
       }
           
8. **POST** /api/v1/user/meta/search
   
   **Description** Gets users satisfying the query.
   
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
