# Location-MetaData-API

Metadata are dynamic variables related to an entity. We assumed the entity is an user. 
All the metadata are stored in a column called 'metadata' in a table having structure of 

    user (
        userId: string (pk), 
        domain: string (pk), 
        status: string, 
        metadata: json
    )

'status' is a special type of meta hence it is handled specially.

### Constraints

We ensure to support 10,000 delivery 1 day. Say, morning 1hr, noon 1hr, night 1hr traffic. 
(10,000 / 3 / 60 / 60) = 1 req/sec.

### API Endpoints

1. **POST** /api/v1/users/meta
   
   **Description:** Create/Replace User
   
   **Payload** 
        
        {
            "userId": "13123", 
            "status": "online", 
            "metadata": {
                "meta1": v1,
                "meta2": "v2"
            }
        }

2. **PATCH** /api/v1/users/{userId}/meta
   
   **Description:** Merge the given user metadata with 
   existing value.
   
   **Payload** 
        
        {
            "metadata": {               // Optional
                "carrying": [
                    "bag",
                    "laptop",
                    "pen",
                    "rice"
                ]
            },
            "status": "online"          // Optional
        }
 

3. **GET** /api/v1/users
      
   **Description:** Returns all the users page by page.
   
   **QueryString:** `page=1&size:100`
   The query params are optional. If not specified only the 
   first 100 will be served.
   
   **ResponseBody:** Contains a list of users.
      
4. **GET** /api/v1/users?userIds=
      
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
           
5. **POST** /api/v1/users/meta/search
   
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

There are three supported types of search query available. MATCH, EXCEPT and RANGE.

#### 1. MATCH:
Match query takes the value as key, value pairs.
    
    "match": {
        "key1": "value1",
        "key2": ["value2, "value3"]
    }

Keys are the field name of the json in metadata column. For a match query every key
must satisfies the value. The values are usually objects of primitive types or arrays 
of primitive types. If values are of primitive types then the json fields are just 
checked if they are matched. 

If they are arrays then the corresponding json field must 
also be array. If not then they will not be in the result. And also all the items in the 
array must be available in the json field. 

It is also possible to search if at least one item in the array is matched. Then this must
be specified using an 'ANY' clause. By default it is assumed as "ALL" clause.
    
    "match": {
        "carrying": {
            "ANY": ["abul", "mofiz"]
        }
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
Except query takes the value as key, value pairs just like the match query.
    
    "except": {
        "key1": "value1",
        "key2": ["value2, "value3"]
    }

For an except query every key must must not satisfies the value. It is just the opposite of
match. If they match then they will be removed from the result in an except query.

Except query can also contain "ANY" or "ALL" clause. Default is "ALL" if nothing specified.

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
    
In case you need to filter according to 'status' column or other (like user_id) then you
have to mention them as query parameters. Query params are treated as key value pairs.
keys are the column name and values are the row values.

### Start db in dev:
    docker-compose up -d

### Start the application:
    ./gradlew bootRun
    
## Release Notes:
#### 1.0.1: 
Improved error response
#### 1.0.0: 
Released first production version