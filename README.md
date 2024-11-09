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