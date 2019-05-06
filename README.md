# Location-MetaData-API

Location-MetaData-API is used for managing extra data related to live location pings.

#### Suggested query patterns
 - Match: 
    -  Json: `["match": {"name": "mofiz", "city": ["Dhaka", "Khulna"]}]`
    -  `name`/`city` refers to `key` in saved JSON.
    -  value can be either single value or array.
    -  Execute query based on match.
 - Except: 
    -  Json: `["except": {"name": "abul", "city": ["Chittagong", "Sylhet"]}]`
    -  Structure same as `match`.
    -  Execute query as NOT.
 - Range: 
    -  JSON: `["range": {"age": {"lt": 42, "gt": 42}]`
    -  `age` refers to `key` saved in JSON.
    -  Support `lt`/`lte`/`gt`/`gte` operators.
