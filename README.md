# Location-MetaData-API

### API Endpoints

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
   
   **Description** Gets users satisfying the query. There are three supported
   types of query available. MATCH, EXCEPT and RANGE. for details see the 
   "Search Query Types" section.
   
   **Payload** 
    
        "match": {
            "name": "abul",
            "city": ["Dhaka", "Khulna", "Tangail"],
            "carrying": {
                "all": ["abul", "mofiz"]
            }
        },
        "except": {
            "name": "mofiz",
            "city": ["Sylhet", "Chittagong"]
            "carrying": {
                "all": ["abul", "mofiz"]
            }
        },
        "range": {
            "age": {
                "lt/lte": 1004,
                "gt/gte": 12132
              }
        }


### Search Query Types

There are three supported types of query available. MATCH, EXCEPT and RANGE.

#### 1. MATCH:
Match query returns all the items that matches the required terms. The value of 
MATCH can either be a primitive types or an array of primitive types or an 
"ANY" or "ALL" query object.
    
    "except": {
        "carrying": {
            "ANY": ["abul", "mofiz"]
        },
        "name": "mofiz"
    }

##### i. ANY:
The value of "ANY" must be a list type. Satisfies if any of the item of the list
is found. 

    "match": {
        "carrying": {
            "ANY": ["abul", "mofiz"]
        }
    }
    
##### ii. ALL:
The value of "ANY" must be a list type. Satisfies if all of the item of the list
is found. This is the default behaviour if ALL or ANY nothing is specified for an array.

    "match": {
        "carrying": {
            "ALL": ["abul", "mofiz"]
        }
    }
    
#### 1. EXCEPT:
Except query returns all the items that does not matches the required terms. The value of 
EXCEPT can either be a primitive types or an array of primitive types or an 
"ANY" or "ALL" query object.
    
    "except": {
        "carrying": {
            "ANY": ["abul", "mofiz"]
        },
        "name": "mofiz"
    }

##### i. ANY:
The value of "ANY" must be a list type. Satisfies if any of the item of the list
is found. 

    "except": {
        "carrying": {
            "ANY": ["abul", "mofiz"]
        }
    }
    
##### ii. ALL:
The value of "ALL" must be a list type. Satisfies if all of the item of the list
is found. This is the default behaviour if ALL or ANY nothing is specified for an array.

    "except": {
        "carrying": {
            "ALL": ["abul", "mofiz"]
        }
    }
    

### Start db in dev:
1. docker-compose up -d
