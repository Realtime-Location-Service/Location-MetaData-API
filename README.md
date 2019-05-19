# Location-MetaData-API

1. Support for 10,000 delivery 1 day. Say, morning 1hr, noon 1hr, night 1hr traffic. (10,000 / 3 / 60 / 60) = 1 req/sec

2. **Table**: 
    
        user (
            user_id: string (pk), 
            domain: string (pk), 
            status: string, 
            metadata: json
        )

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

4. **PATCH** /api/v1/users/{user_id}/meta
   
   **Description:** Merge the given user metadata with 
   existing value.
   
   **Payload** 
        
        {
            "metadata": {               // Optional
                "carying": [
                    "bag",
                    "laptop",
                    "pen",
                    "rice"
                ]
            },
            "status": "online"          // Optional
        }
 

5. **GET** /api/v1/users
      
   **Description:** Returns all the users page by page.
   
   **QueryString:** `page=1&size:100`
   The query params are optional. If not specified only the 
   first 100 will be served.
      
6. **GET** /api/v1/users?user_ids=
      
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
           
7. **POST** /api/v1/users/meta/search
   
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
