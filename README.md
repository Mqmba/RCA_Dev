# capstone.api
# js.api




## Recycling-depot /recycling-depot	

### Get 	/get-list-recycling-depot-by-paging

```bash 
curl -X GET "http://localhost:8080/recycling-depot/get-list-recycling-depot-by-paging?page=0&size=10" -H "Accept: application/json"
```
### Post	/create-recycling-depot
```bash
curl -X POST "http://localhost:8080/recycling-depot/create-recycling-depot" \
     -H "Content-Type: application/json" \
     -d '{
           "depotName": "New Depottt",
           "location": "New Location2",
           "isWorking": true,
           "user": {"userId": 2}  
         }'
```
### Post	/change-is-working-status
```bash
curl -X POST "http://localhost:8080/recycling-depot/change-is-working-status?id=1&isWorking=false" \
     -H "Content-Type: application/json"
```
### Post	/change-working-date
```bash
curl -X POST "http://localhost:8080/recycling-depot/change-is-working-status?id=1&isWorking=false" \
     -H "Content-Type: application/json"
```
### Get	    /get-list-active-recycling-depot-by-paging
```bash
curl -X GET "http://localhost:8080/recycling-depot/get-list-active-recycling-depot-by-paging?page=0&size=10" -H "Accept: application/json"
```
## Apartment /apartments

### Put	    /update-apartment
```bash
curl -X 'PUT' \
  'http://localhost:8080/apartments/update-apartment' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "apartmentId": 1,
  "name": "new name",
  "description": "new description"
}'
```
### Post	    /create-apartment
```bash
curl -X 'POST' \
  'http://localhost:8080/apartments/create-apartment' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Hello",
  "description": "hello"
}'
```
### Get	    /get-list-apartment-by-paging
```bash
curl -X 'GET' \
  'http://localhost:8080/apartments/get-list-apartment-by-paging?page=0&size=10' \
  -H 'accept: */*'
```
## Resident /residents

### Get	    /get-list-residents-by-paging
```bash
curl -X 'GET' \
  'http://localhost:8080/residents/get-list-residents-by-paging?page=0&size=10' \
  -H 'accept: */*'
```
### Post	    /create-resident
```bash
curl -X 'POST' \
  'http://localhost:8080/residents/create-resident' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "Kongou",
  "password": "123123",
  "email": "kongou@gmail.com",
  "firstName": "Chan",
  "lastName": "Nguyen",
  "phoneNumber": "093385713",
  "rewardPoints": 0,
  "apartmentId": 1
}'
```

## Collector /collector

### Put	    /change-is-working-status
```bash
curl -X 'PUT' \
  'http://localhost:8080/collector/change-is-working-status?isWorking=false&collectorId=1' \
  -H 'accept: */*'
```

### Get	    /get-list-collector-by-paging
```bash
curl -X 'GET' \
  'http://localhost:8080/collector/get-list-collector-by-paging?page=0&size=10' \
  -H 'accept: */*'
```

## Point /point

### Post	    collector/point/update-point-by-user
```bash
curl -X 'POST' \
  'http://localhost:8080/collector/point/update-point-by-user' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrb25nb3UiLCJpYXQiOjE3MzEyNDY5NTAsImV4cCI6MTczMTI1MDU1MH0.mF7cUvCrZlFRKVNahpv_4FVXqTJfos2tzpj2_n6XgOk' \
  -H 'Content-Type: application/json' \
  -d '{
  "senderId": 2,
  "receiverId": 2,
  "amount": 20
}'
```

### Get	    public/point/get-point-by-user
```bash
curl -X 'GET' \
  'http://localhost:8080/public/point/get-point-by-user' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJodGtvbmdvdUBnbWFpbC5jb20iLCJpYXQiOjE3MzEyNDY0OTEsImV4cCI6MTczMTI1MDA5MX0.vxDaUhYzLRg47Q1oJDwVRboqaiMUTBs-A1sAWlJj-WU'
```

## Reward /Reward
### Post	    reward/create-reward
```bash
curl -X 'POST' \
  'http://localhost:8080/reward/create-reward' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "point": 20,
  "description": "sdfsdf",
  "name": "circle k voucher"
}'
```

### Get	    /get-list-reward-by-paging
```bash
curl -X 'GET' \
  'http://localhost:8080/reward/get-list-reward-by-paging?page=0&size=10' \
  -H 'accept: */*'
```


